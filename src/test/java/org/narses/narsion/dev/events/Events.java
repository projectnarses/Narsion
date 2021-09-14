package org.narses.narsion.dev.events;

import com.moandjiezana.toml.Toml;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.event.trait.CancellableEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.abilities.Ability;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.dev.player.DevPlayer;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.world.narsionworlddata.NarsionRegions;
import org.narses.narsion.player.NarsionPlayer;

import java.util.Objects;
import java.util.UUID;

public class Events {

    private final @NotNull DevServer server;

    private final Pos respawnPoint;

    public Events(@NotNull DevServer server) {
        this.server = server;

        // Get respawn point from config
        final Toml config = server.getConfig();
        final double worldSpawnX = config.getDouble("World.SpawnX");
        final double worldSpawnY = config.getDouble("World.SpawnY");
        final double worldSpawnZ = config.getDouble("World.SpawnZ");

        this.respawnPoint = new Pos(worldSpawnX, worldSpawnY, worldSpawnZ);
    }

    public void registerAll(@NotNull EventNode<Event> eventNode) {
        eventNode.addListener(PlayerLoginEvent.class, this::handlePlayerLoginEvent);
        eventNode.addListener(PlayerSpawnEvent.class, this::handlePlayerSpawnEvent);
        eventNode.addListener(ItemDropEvent.class, this::handleItemDropEvent);
        eventNode.addListener(PlayerSwapItemEvent.class, this::handlePlayerSwapItemEvent);
        eventNode.addListener(PlayerChangeHeldSlotEvent.class, this::handlePlayerChangeHeldSlotEvent);
        eventNode.addListener(PlayerBlockBreakEvent.class, this::cancelEvent);
        eventNode.addListener(PlayerBlockPlaceEvent.class, this::cancelEvent);
        eventNode.addListener(PlayerUseItemEvent.class, this::handlePlayerUseItemEvent);
    }

    public void handlePlayerLoginEvent(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        event.setSpawningInstance(server.getPrimaryInstance());
        player.setRespawnPoint(respawnPoint);
        player.setPermissionLevel(4);
    }

    public void handlePlayerSpawnEvent(PlayerSpawnEvent event) {
        Player player = event.getPlayer();

        player.setItemInMainHand(
                server.getItemStackProvider()
                        .create(
                                "THE_END",
                                new UUID(0, 0),
                                null
                        )
        );

        for (final Region region : NarsionRegions.values()) {
            region.addViewer(player);
        }
    }

    public void handleItemDropEvent(ItemDropEvent event) {
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            return;
        }

        event.setCancelled(true);

        // scroll to the left of abilities
        DevPlayer devPlayer = server.wrap(player);
        devPlayer.setAbilitySlot((byte) (devPlayer.getAbilitySlot() - 1));
    }

    public void handlePlayerSwapItemEvent(PlayerSwapItemEvent event) {
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            return;
        }

        event.setCancelled(true);

        // scroll to the left of abilities
        DevPlayer devPlayer = server.wrap(player);
        devPlayer.setAbilitySlot((byte) (devPlayer.getAbilitySlot() + 1));
    }

    public void handlePlayerChangeHeldSlotEvent(PlayerChangeHeldSlotEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        event.setCancelled(true);

        if (player.getHeldSlot() == event.getSlot()) {
            return;
        }

        // Update ability slot
        DevPlayer devPlayer = server.wrap(player);
        final String selectionMode = devPlayer.getTag(NarsionPlayer.TAG_ABILITY_SELECTION_MODE);
        Objects.requireNonNull(selectionMode);

        switch (selectionMode) {
            case "SCROLL" -> {
                int slot = player.getHeldSlot() - event.getSlot();
                if (event.getSlot() == 8) {
                    slot = 1;
                }
                devPlayer.setAbilitySlot((byte) (devPlayer.getAbilitySlot() - slot));
            }
            case "SET" -> devPlayer.setAbilitySlot(event.getSlot());
        }
    }

    public void handlePlayerUseItemEvent(PlayerUseItemEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) {
            return;
        }

        DevPlayer devPlayer = server.wrap(player);

        if (!devPlayer.activateAbility()) {
            Ability ability = devPlayer.getSelectedAbility();
            int seconds =  ability.getCooldown() - devPlayer.getAbilityCooldown(devPlayer.getAbilitySlot());
            seconds /= 1000;
            player.sendMessage("Ability: " + devPlayer.getSelectedAbility() + " has " + seconds + " seconds until it can be used.");
        }
    }

    public void cancelEvent(CancellableEvent event) {
        event.setCancelled(true);
    }
}

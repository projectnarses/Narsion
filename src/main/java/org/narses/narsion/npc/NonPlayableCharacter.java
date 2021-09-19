package org.narses.narsion.npc;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class NonPlayableCharacter extends LivingEntity implements NavigableEntity {

    private final @NotNull Navigator navigator = new Navigator(this);
    private final @NotNull String id;
    private final @NotNull Pos homePosition;
    private final @NotNull String name;
    private final @NotNull PlayerInfoPacket PLAYER_ADD_INFO;
    private final @NotNull PlayerInfoPacket PLAYER_HIDE_INFO;

    public NonPlayableCharacter(
            @NotNull UUID uuid,
            @NotNull String id,
            @NotNull Pos homePosition,
            @NotNull TextComponent displayName
    ) {
        super(EntityType.PLAYER, uuid);
        this.id = id;
        this.homePosition = homePosition;
        final String name = displayName.content();
        this.name = name.substring(0, Math.min(name.length(), 16));
        this.PLAYER_ADD_INFO = generatePlayerAddInfo();
        this.PLAYER_HIDE_INFO = generatePlayerHideInfo();
        this.setCustomName(displayName);
    }

    /**
     * Gets the internal id of this npc
     */
    public @NotNull String id() {
        return id;
    };

    /**
     * Gets the home position of this npc
     */
    public @NotNull Pos homePosition() {
        return homePosition;
    };

    /**
     * This is run when a player interacts with this npc
     */
    public void onInteract(@NotNull NarsionServer server, PlayerEntityInteractEvent event) {
    }

    @NotNull
    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void update(long time) {
        super.update(time);

        // Path finding
        this.navigator.tick(getAttributeValue(Attribute.MOVEMENT_SPEED));
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        this.navigator.setPathFinder(new HydrazinePathFinder(navigator.getPathingEntity(), instance.getInstanceSpace()));
        return super.setInstance(instance, spawnPosition);
    }

    private @NotNull PlayerInfoPacket generatePlayerAddInfo() {
        PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER);
        packet.playerInfos = List.of(new PlayerInfoPacket.AddPlayer(uuid, name, GameMode.CREATIVE, 0));
        return packet;
    }

    private @NotNull PlayerInfoPacket generatePlayerHideInfo() {
        PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER);
        packet.playerInfos = List.of(new PlayerInfoPacket.RemovePlayer(uuid));
        return packet;
    }

    @Override
    public boolean addViewer0(@NotNull Player player) {
        final PlayerConnection connection = player.getPlayerConnection();
        connection.sendPacket(PLAYER_ADD_INFO);
        // Hide npc from tablist
        // This needs to be delayed, otherwise the player does not render.
        this.scheduleNextTick((ignored) -> connection.sendPacket(PLAYER_HIDE_INFO));
        return super.addViewer0(player);
    }

    public static interface NPCSupplier {
        public @NotNull NonPlayableCharacter get(
                @NotNull UUID uuid,
                @NotNull String id,
                @NotNull Pos homePosition,
                @NotNull TextComponent displayName
        );
    }
}

package org.narses.narsion.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.item.ClickableInventory;
import org.narses.narsion.npc.NonPlayableCharacter;

public class Events {

    private final @NotNull NarsionServer server;

    public Events(@NotNull NarsionServer server) {
        this.server = server;
    }

    public void registerAll(@NotNull EventNode<Event> eventNode) {
        eventNode.addListener(PlayerEntityInteractEvent.class, this::handlePlayerEntityInteractEvent);
        eventNode.addListener(InventoryPreClickEvent.class, this::handleInventoryPreClickEvent);
        eventNode.addListener(InventoryClickEvent.class, this::handleInventoryClickEvent);
    }

    private void handlePlayerEntityInteractEvent(PlayerEntityInteractEvent event) {
        Entity target = event.getTarget();

        if (!(target instanceof NonPlayableCharacter npc)) {
            return;
        }

        npc.onInteract(server, event);
    }

    private void handleInventoryPreClickEvent(InventoryPreClickEvent event) {
        if (event.getInventory() instanceof ClickableInventory inventory) {
            inventory.preClickEvent(event);
        }
    }

    private void handleInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory() instanceof ClickableInventory inventory) {
            inventory.clickEvent(event);
        }
    }
}

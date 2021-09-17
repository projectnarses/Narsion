package org.narses.narsion.events;

import com.moandjiezana.toml.Toml;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.npc.NonPlayableCharacter;

public class Events {

    private final @NotNull NarsionServer server;

    public Events(@NotNull NarsionServer server) {
        this.server = server;
    }

    public void registerAll(@NotNull EventNode<Event> eventNode) {
        eventNode.addListener(PlayerEntityInteractEvent.class, this::handlePlayerEntityInteractEvent);
    }

    private void handlePlayerEntityInteractEvent(PlayerEntityInteractEvent event) {
        Entity target = event.getTarget();

        if (!(target instanceof NonPlayableCharacter npc)) {
            return;
        }

        npc.onInteract(server, event);
    }
}

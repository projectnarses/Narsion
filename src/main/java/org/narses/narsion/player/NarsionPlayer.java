package org.narses.narsion.player;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NarsionPlayer {
    protected final @NotNull NarsionServer server;

    // Player object
    protected final @NotNull Player player;

    // Player data
    private @NotNull PlayerClass playerClass;

    protected NarsionPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        this.player = player;
        this.server = server;
        this.playerClass = server.getPlayerClasses().WARRIOR;
        // TODO: Read player data from database
    }

    public void setPlayerClass(@NotNull PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public @NotNull PlayerClass getPlayerClass() {
        return playerClass;
    }

    public @NotNull Player getPlayer() {
        return player;
    }
}

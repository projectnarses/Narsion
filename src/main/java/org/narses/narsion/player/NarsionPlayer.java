package org.narses.narsion.player;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NarsionPlayer {
    // The tags to use when interfacing with the player


    // Map to hold all NarsionPlayer instances
    private static final Map<Player, NarsionPlayer> playerNarsionPlayerMap = new ConcurrentHashMap<>();

    /**
     * Gets the NarsionPlayer instance of the specified {@link Player}, creates a new one if it doesn't exist
     * @param player the player to generate the wrapper for
     * @return narsionPlayer the player wrapper
     */
    public static NarsionPlayer of(NarsionServer server, Player player) {
        // Ensure player is in map
        playerNarsionPlayerMap.computeIfAbsent(player, (somePlayer) -> new NarsionPlayer(server, somePlayer));

        // Return player from map
        return playerNarsionPlayerMap.get(player);
    }

    private final @NotNull NarsionServer server;

    // Player object
    private final @NotNull Player player;

    // Player data
    private @NotNull PlayerClass playerClass;

    private NarsionPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        this.player = player;
        this.server = server;
        this.playerClass = server.getPlayerClasses().DEVELOPMENT;
        // TODO: Read player data from database
    }

    public void setPlayerClass(@NotNull PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public @NotNull PlayerClass getPlayerClass() {
        return playerClass;
    }
}

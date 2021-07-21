package org.narses.narsion.player;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.classes.PlayerClasses;
import org.narses.narsion.classes.abilities.PlayerClass;

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

    private final NarsionServer server;

    // Player object
    private final Player player;

    // Player data
    private PlayerClass playerClass;

    private NarsionPlayer(NarsionServer server, Player player) {
        this.player = player;
        this.server = server;
        this.playerClass = server.getPlayerClasses().DEVELOPMENT;
        // TODO: Read player data from database
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }
}

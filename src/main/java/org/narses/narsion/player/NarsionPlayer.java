package org.narses.narsion.player;

import net.minestom.server.entity.Player;

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
    public static NarsionPlayer of(Player player) {
        playerNarsionPlayerMap.computeIfAbsent(player, NarsionPlayer::new);

        return playerNarsionPlayerMap.get(player);
    }

    private final Player player;

    private NarsionPlayer(Player player) {
        this.player = player;
    }
}

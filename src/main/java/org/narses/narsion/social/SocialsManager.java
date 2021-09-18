package org.narses.narsion.social;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SocialsManager {

    private final @NotNull NarsionServer server;

    public SocialsManager(@NotNull NarsionServer server) {
        this.server = server;
    }

    private final Map<UUID, Guild> guildUuidToGuild = new HashMap<>();
    private final Map<UUID, Nation> nationUuidToNation = new HashMap<>();
    private final Map<UUID, Guild> playerToGuild = new HashMap<>();

    public @NotNull Guild createGuild(@NotNull String name, @NotNull Player leader, Player... players) {
        UUID[] uuids = Arrays.stream(players).map(Player::getUuid).toArray(UUID[]::new);
        return createGuild(name, leader.getUuid(), uuids);
    }

    public @NotNull Guild createGuild(@NotNull String name, @NotNull UUID leader, UUID... players) {
        Guild guild = new Guild(server, name, UUID.randomUUID(), leader, players);

        guildUuidToGuild.put(guild.getUuid(), guild);
        playerToGuild.put(leader, guild);
        for (UUID player : players) {
            playerToGuild.put(player, guild);
        }

        return guild;
    }

    /**
     * Gets the guild object associated with the player
     * @param player player to get the guild object from
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromPlayer(@NotNull UUID player) {
        return playerToGuild.get(player);
    }

    /**
     * Gets the guild object associated with the player
     * @param player player to get the guild object from
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromPlayer(@NotNull Player player) {
        return getGuildFromPlayer(player.getUuid());
    }

    /**
     * Gets the guild associated with this uuid
     * @param guildUuid the uuids
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromUuid(@NotNull UUID guildUuid) {
        return guildUuidToGuild.get(guildUuid);
    }

    /**
     * Gets the nation associated with this uuid
     * @param nationUuid the uuids
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromUuid(@NotNull UUID nationUuid) {
        return nationUuidToNation.get(nationUuid);
    }
}

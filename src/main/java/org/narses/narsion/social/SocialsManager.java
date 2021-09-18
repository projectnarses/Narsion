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
    private final Map<UUID, Nation> guildToNation = new HashMap<>();
    private final Map<UUID, Guild> playerToGuild = new HashMap<>();

    public @Nullable SocialRank getRank(@NotNull Player player) {
        Guild someGuild = getGuildFromPlayer(player);
        if (someGuild == null) {
            return null;
        }
        return someGuild.getRank(player);
    }

    public @Nullable SocialRank getRank(@NotNull Guild guild) {
        Nation someNation = this.getNationFromGuild(guild);
        if (someNation == null) {
            return null;
        }
        return someNation.getRank(guild);
    }

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
     * @param guildUuid the guild uuid
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromUuid(@NotNull UUID guildUuid) {
        return guildUuidToGuild.get(guildUuid);
    }

    /**
     * Gets the nation associated with this uuid
     * @param nationUuid the nation uuid
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromUuid(@NotNull UUID nationUuid) {
        return nationUuidToNation.get(nationUuid);
    }

    /**
     * Gets the nation associated with this guild
     * @param guild the guild
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromGuild(@NotNull Guild guild) {
        return getNationFromGuild(guild.getUuid());
    }

    /**
     * Gets the nation associated with this guild
     * @param guildUuid the guild uuid
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromGuild(@NotNull UUID guildUuid) {
        return guildToNation.get(guildUuid);
    }

    /**
     * Removes this player from their guild.
     * @param player the player to remove
     * @return true if successful, false otherwise
     */
    public boolean removePlayerFromGuild(@NotNull Player player) {
        return removePlayerFromGuild(player.getUuid());
    }

    /**
     * Removes this player from their guild.
     * @param player the player to remove
     * @return true if successful, false otherwise
     */
    public boolean removePlayerFromGuild(@NotNull UUID player) {
        Guild guild = getGuildFromPlayer(player);
        if (guild == null) {
            return false;
        }
        if (guild.remove(player)) {
            this.playerToGuild.remove(player);
        }
        return false;
    }

    /**
     * Adds this player to the guild.
     * @param player the player to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addPlayerToGuild(@NotNull Player player, @NotNull Guild guild) {
        return addPlayerToGuild(player.getUuid(), guild.getUuid());
    }

    /**
     * Adds this player to the guild.
     * @param player the player to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addPlayerToGuild(@NotNull UUID player, @NotNull UUID guild) {
        Guild guildObject = getGuildFromUuid(guild);
        if (guildObject == null) {
            return false;
        }
        if (getGuildFromPlayer(player) != null) {
            return false;
        }
        if (guildObject.add(player)) {
            this.playerToGuild.put(player, guildObject);
        }
        return false;
    }
}

package org.narses.narsion.social;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;

import java.util.*;

public class SocialsManager {

    private final @NotNull NarsionServer server;

    public SocialsManager(@NotNull NarsionServer server) {
        this.server = server;
    }

    // Social ranks and members
    private final Map<UUID, SocialRank> socialRanks = new HashMap<>();

    // Loaded member groups
    private final Map<UUID, Guild> loadedGuilds = new HashMap<>();
    private final Map<UUID, Nation> loadedNations = new HashMap<>();

    // Cached relationships
    private final Map<UUID, UUID> player2Guild = new HashMap<>();
    private final Map<UUID, UUID> guild2Nation = new HashMap<>();

    /**
     * Gets the rank of the social member
     * @param member the member to get the rank of
     * @return the rank of the member
     */
    public @Nullable SocialRank getRank(@NotNull SocialMember member) {
        return this.getRank(member.getUuid());
    }

    /**
     * Gets the rank associated with the social member
     * @param member the member to get the rank of
     * @return the rank of the member
     */
    public @Nullable SocialRank getRank(@NotNull UUID member) {
        return socialRanks.get(member);
    }

    /**
     * Creates a new guild and uploads it to the database
     * @param name the name of the guild
     * @param leader the leader of the guild
     * @param players the players of the guild
     * @return the guild object
     */
    public @NotNull Guild createGuild(@NotNull String name, @NotNull Player leader, Player... players) {
        UUID[] uuids = Arrays.stream(players).map(Player::getUuid).toArray(UUID[]::new);
        return loadGuild(UUID.randomUUID(), name, leader.getUuid(), uuids);
    }

    /**
     * Loads a new guild into the manager
     * @param guildUuid the guild uuid
     * @param name the guild name
     * @param leader the guild leader
     * @param players the guild players
     * @return the guild object
     */
    public @NotNull Guild loadGuild(@NotNull UUID guildUuid, @NotNull String name, @NotNull UUID leader, UUID... players) {
        Guild guild = new Guild(server, name, guildUuid, leader, players);
        loadedGuilds.put(guildUuid, guild);

        // Add leader
        addPlayerToGuild(leader, guildUuid, SocialRank.LEADER);

        // Add members
        for (UUID player : players) {
            addPlayerToGuild(player, guildUuid, getDefaultRank());
        }
        return guild;
    }

    /**
     * Gets the default social rank
     * @return the default social rank
     */
    public @NotNull SocialRank getDefaultRank() {
        return SocialRank.MEMBER;
    }

    /**
     * Gets the guild object associated with the member
     * @param member member to get the guild object from
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromMember(@NotNull SocialMember member) {
        return getGuildFromMember(member.getUuid());
    }

    /**
     * Gets the guild object associated with the member
     * @param member member to get the guild object from
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromMember(@NotNull UUID member) {
        UUID guild = player2Guild.get(member);
        if (guild == null) {
            return null;
        }
        return loadedGuilds.get(guild);
    }

    /**
     * Gets the guild associated with this uuid
     * @param guild the guild uuid
     * @return guild if found, null if else
     */
    public @Nullable Guild getGuildFromUuid(@NotNull UUID guild) {
        return loadedGuilds.get(guild);
    }

    /**
     * Gets the nation associated with this uuid
     * @param nation the nation
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromUuid(@NotNull UUID nation) {
        return loadedNations.get(nation);
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
     * @param guild the guild uuid
     * @return nation if found, null if else
     */
    public @Nullable Nation getNationFromGuild(@NotNull UUID guild) {
        UUID nation = guild2Nation.get(guild);
        if (nation == null) {
            return null;
        }
        return loadedNations.get(nation);
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
        Guild guild = getGuildFromMember(player);
        if (guild == null) {
            return false;
        }
        if (guild.remove(player)) {
            this.player2Guild.remove(player);
            this.socialRanks.remove(player);
        }
        return false;
    }

    /**
     * Adds this player to the guild.
     * @param player the player to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addPlayerToGuild(@NotNull Player player, @NotNull Guild guild, @NotNull SocialRank rank) {
        return addPlayerToGuild(player.getUuid(), guild.getUuid(), rank);
    }

    /**
     * Adds this player to the guild.
     * @param player the player to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addPlayerToGuild(@NotNull UUID player, @NotNull UUID guild, @NotNull SocialRank rank) {
        Guild guildObject = getGuildFromUuid(guild);
        if (guildObject == null) {
            return false;
        }
        if (getGuildFromMember(player) != null) {
            return false;
        }
        if (guildObject.add(player)) {
            this.player2Guild.put(player, guild);
            setRank(player, rank);
            return true;
        }
        return false;
    }

    /**
     * Sets the social rank of this player
     * @param member the member to set the rank of
     * @param rank the rank to set
     * @return true if rank was changed, false otherwise
     */
    public boolean setRank(UUID member, SocialRank rank) {
        return socialRanks.put(member, rank) == rank;
    }

    /**
     * Sets the social rank of this player
     * @param member the member to set the rank of
     * @param rank the rank to set
     * @return true if rank was changed, false otherwise
     */
    public boolean setRank(SocialMember member, SocialRank rank) {
        return this.setRank(member.getUuid(), rank);
    }
}

package org.narses.narsion.social;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.util.StringUtils;

import java.net.SocketAddress;
import java.util.*;
import java.util.stream.Collectors;

public class SocialsManager {

    private final @NotNull NarsionServer server;

    public SocialsManager(@NotNull NarsionServer server) {
        this.server = server;
    }

    // Social ranks and members
    private final Map<UUID, SocialRank> socialRanks = new HashMap<>();

    // Loaded social groups
    private final Map<UUID, SocialGroup> loadedGroups = new HashMap<>();

    // Cached relationships
    private final Map<UUID, UUID> member2Group = new HashMap<>();

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
    public @NotNull Guild createGuild(@NotNull String name, @NotNull NarsionPlayer leader, NarsionPlayer... players) {
        UUID[] uuids = Arrays.stream(players).map(NarsionPlayer::getUuid).toArray(UUID[]::new);
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
        Guild guild = new Guild(server, name, guildUuid, leader);
        loadedGroups.put(guildUuid, guild);

        // Add leader
        addMemberToGroup(leader, guildUuid, SocialRank.EMPEROR);

        // Add members
        for (UUID player : players) {
            addMemberToGroup(player, guildUuid, getDefaultRank());
        }
        return guild;
    }

    /**
     * Gets the default social rank
     * @return the default social rank
     */
    public @NotNull SocialRank getDefaultRank() {
        return SocialRank.PEASANT;
    }

    /**
     * Gets the guild object associated with the member
     * @param member member to get the guild object from
     * @return guild if found, null if else
     */
    public @Nullable <E extends SocialGroup> E getGroupFromMember(@NotNull SocialMember member) {
        return getGroupFromMember(member.getUuid());
    }

    /**
     * Gets the guild object associated with the member
     * @param member member to get the guild object from
     * @return guild if found, null if else
     */
    @SuppressWarnings("unchecked")
    public @Nullable <E extends SocialGroup> E getGroupFromMember(@NotNull UUID member) {
        UUID group = member2Group.get(member);
        if (group == null) {
            return null;
        }
        Object groupObj = loadedGroups.get(group);
        if (groupObj == null) {
            return null;
        }
        return (E) groupObj;
    }

    /**
     * Gets the group associated with this uuid
     * @param group the group uuid
     * @return group if found, null if else
     */
    @SuppressWarnings("unchecked")
    public @Nullable <E extends SocialGroup> E getGroupFromUuid(@NotNull UUID group) {
        Object groupObj = loadedGroups.get(group);
        if (groupObj == null) {
            return null;
        }
        return (E) groupObj;
    }

    /**
     * Removes this member from their group.
     * @param member the member to remove
     * @return true if successful, false otherwise
     */
    public boolean kickMemberFromGroup(@NotNull UUID member) {
        SocialGroup group = getGroupFromMember(member);
        if (group == null) {
            return false;
        }
        if (group.remove(member)) {
            member2Group.remove(member);
            setRank(member, null);
            group.onLeave(new SocialGroup.Leave(member));
        }
        return false;
    }

    /**
     * Adds this player to the guild.
     * @param member the player to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addMemberToGroup(@NotNull SocialMember member, @NotNull Guild guild, @NotNull SocialRank rank) {
        return addMemberToGroup(member.getUuid(), guild.getUuid(), rank);
    }

    /**
     * Adds this member to the guild.
     * @param member the member to add
     * @param guild the guild to add to
     * @return true if successful, false otherwise
     */
    public boolean addMemberToGroup(@NotNull UUID member, @NotNull UUID guild, @NotNull SocialRank rank) {
        Guild guildObject = getGroupFromUuid(guild);
        if (guildObject == null) {
            return false;
        }
        if (guildObject.add(member)) {
            this.member2Group.put(member, guild);
            setRank(member, rank);
            guildObject.onJoin(new SocialGroup.Join(member));
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

    /**
     * Gets all the guilds
     * @return all the guilds
     */
    public @NotNull Set<Guild> getGuilds() {
        return loadedGroups.values().stream()
                .filter(Guild.class::isInstance)
                .map(Guild.class::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all the nations
     * @return all the nations
     */
    public @NotNull Set<Nation> getNations() {
        return loadedGroups.values().stream()
                .filter(Nation.class::isInstance)
                .map(Nation.class::cast)
                .collect(Collectors.toSet());
    }

    /**
     * Gets the guild with the closest name to this name, within reason
     * @param name the name to search for
     * @return the guild if found, null if else
     */
    public @Nullable Guild getClosestGuild(@NotNull String name) {
        return StringUtils.getClosestNamed(name, getGuilds(), 0.5, Guild::getName);
    }

    /**
     * Invites a member to a group
     * @param member the member to invite
     * @param group the group to invite to
     */
    public void inviteMemberToGroup(UUID member, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(group);
        if (groupObject == null) {
            return;
        }

        if (groupObject.getInvites().add(member)) {
            groupObject.onInvite(new SocialGroup.Invite(member));
        }
    }

    /**
     * Remove an invite from a group
     * @param member the member to uninvite
     * @param group the group to uninvite from
     */
    public void uninviteMemberToGroup(UUID member, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(group);
        if (groupObject == null) {
            throw new IllegalArgumentException("Group does not exist");
        }
        if (groupObject.getInvites().remove(member)) {
            groupObject.onUninvite(new SocialGroup.Uninvite(member));
        }
    }

    /**
     * Bans the member from the group
     * @param member the member to ban
     */
    public void banMemberFromGroup(UUID member, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(member);
        if (groupObject == null) {
            return;
        }

        // Kick first
        kickMemberFromGroup(member);

        // Now try ban
        if (groupObject.onBan(new SocialGroup.Ban(member))) {
            groupObject.addBan(member);
        }
    }

    /**
     * Unbans the member from the group
     * @param member the member to unban
     * @param group the group to unban from
     */
    public void unbanMemberFromGroup(UUID member, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(group);
        if (groupObject == null) {
            return;
        }
        if (groupObject.onUnban(new SocialGroup.Unban(member))) {
            groupObject.removeBan(member);
        }
    }

    /**
     * Bans the ip from the group
     * @param ip the ip to ban
     * @param group the group to ban from
     */
    public void banIpFromGroup(SocketAddress ip, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(group);
        if (groupObject == null) {
            return;
        }
        if (groupObject.onBanip(new SocialGroup.Banip(ip))) {
            groupObject.addBanip(ip);
        }
    }

    /**
     * Unbans the ip from the group
     * @param ip the ip to unban
     * @param group the group to unban from
     */
    public void unbanIpFromGroup(SocketAddress ip, UUID group) {
        SocialGroup groupObject = getGroupFromUuid(group);
        if (groupObject == null) {
            return;
        }
        if (groupObject.onUnbanip(new SocialGroup.Unbanip(ip))) {
            groupObject.removeBanip(ip);
        }
    }
}

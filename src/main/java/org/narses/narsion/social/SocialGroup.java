package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * An interface that represents a social group.
 */
public interface SocialGroup {

    /**
     * Gets all members in this group, filtered by the given function.
     * @param filter the filter
     * @return the filtered members
     */
    public @NotNull Collection<@NotNull UUID> getMembers(@NotNull Predicate<UUID> filter);

    /**
     * Gets the info of this group.
     * @return the info
     */
    public @NotNull <I> I getInfo();

    /**
     * Adds a member to this group.
     * @param member the member to add
     * @return true if the member was added, false otherwise
     */
    public boolean add(@NotNull UUID member);

    /**
     * Removes a member from this group.
     * @param member the member to remove
     * @return true if the member was removed, false otherwise
     */
    public boolean remove(@NotNull UUID member);

    /**
     * Gets if this group contains the given member.
     * @param member the member to check
     * @return true if the member is in this group, false otherwise
     */
    boolean contains(UUID member);

    /**
     * Gets all invited members.
     * @return the invited members as a mutable list
     */
    @NotNull Set<UUID> getInvites();

    /**
     * Bans the given member from this group.
     * @param member the member to ban
     * @return true if the member was banned, false otherwise
     */
    boolean addBan(UUID member);

    /**
     * Unbans the given member from this group.
     * @param member the member to unban
     * @return true if the member was unbanned, false otherwise
     */
    boolean removeBan(UUID member);

    /**
     * Banips the given ip from this group.
     * @param address the address to ban
     * @return true if the member was banned, false otherwise
     */
    boolean addBanip(SocketAddress address);

    /**
     * Unbanips the given ip from this group.
     * @param address the address to unban
     * @return true if the member was unbanned, false otherwise
     */
    boolean removeBanip(SocketAddress address);


//    --------------------
//    -- Event handlers --
//    --------------------

    /**
     * This event is called when a member chats in the group chat.
     * @param chat the chat
     */
    void onChat(@NotNull SocialGroup.Chat chat);

    /**
     * This event is called when a member joins the group.
     * @param join the join
     */
    void onJoin(@NotNull SocialGroup.Join join);

    /**
     * This event is called when a member leaves the group.
     * @param leave the leave
     */
    void onLeave(@NotNull SocialGroup.Leave leave);

    /**
     * This event is called when a member is invited to the group.
     * @param invite the invite
     */
    void onInvite(@NotNull SocialGroup.Invite invite);

    /**
     * This event is called when a member is uninvited from the group.
     * @param uninvite the uninvite
     */
    void onUninvite(@NotNull SocialGroup.Uninvite uninvite);

    /**
     * This event is called when a member is promoted within the group.
     * @param promote the promotion
     */
    void onPromote(@NotNull SocialGroup.Promote promote);

    /**
     * This event is called when a member is demoted within the group.
     * @param demote the demotion
     */
    void onDemote(@NotNull SocialGroup.Demote demote);

    /**
     * This event is called when a member is banned from the group.
     * @param ban the ban
     * @return true to allow the ban, false otherwise
     */
    boolean onBan(@NotNull SocialGroup.Ban ban);

    /**
     * This event is called when a member is unbanned from the group.
     * @param unban the unban
     * @return true to allow the unban, false otherwise
     */
    boolean onUnban(@NotNull SocialGroup.Unban unban);

    /**
     * This event is called when a member is baniped from the group.
     * @param banip the banip
     * @return true to allow the banip, false otherwise
     */
    boolean onBanip(@NotNull SocialGroup.Banip banip);

    /**
     * This event is called when a member is unbaniped from the group.
     * @param unbanip the unbanip
     * @return true to allow the unbanip, false otherwise
     */
    boolean onUnbanip(Unbanip unbanip);

    public record Chat(@NotNull UUID member, @NotNull String message) { };
    public record Join(@NotNull UUID member) { };
    public record Leave(@NotNull UUID member) { };
    public record Invite(@NotNull UUID member) { };
    public record Uninvite(@NotNull UUID member) { };
    public record Promote(@NotNull UUID member, @NotNull SocialRank rank) { };
    public record Demote(@NotNull UUID member, @NotNull SocialRank rank) { };
    public record Ban(@NotNull UUID member) { }
    public record Unban(@NotNull UUID member) { }
    public record Banip(@NotNull SocketAddress ip) { }
    public record Unbanip(@NotNull SocketAddress ip) { }
}

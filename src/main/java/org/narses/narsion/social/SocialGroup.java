package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * An interface that represents a social group.
 * @param <M> the elements that this group contains
 * @param <I> the info object of this group
 */
public interface SocialGroup<M extends SocialMember, I> {

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
    public @NotNull I getInfo();

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
     * Adds a member to this group.
     * @param member the member to add
     * @return true if the member was added, false otherwise
     */
    default boolean add(@NotNull M member) {
        return this.add(member.getUuid());
    };

    /**
     * Removes a member from this group.
     * @param member the member to remove
     * @return true if the member was removed, false otherwise
     */
    default boolean remove(@NotNull M member) {
        return this.remove(member.getUuid());
    }

    /**
     * Gets if this group contains the given member.
     * @param member the member to check
     * @return true if the member is in this group, false otherwise
     */
    boolean contains(UUID member);

    /**
     * Gets if this group contains the given member.
     * @param member the member to check
     * @return true if the member is in this group, false otherwise
     */
    default boolean contains(M member) {
        return this.contains(member.getUuid());
    };

    /**
     * Gets all invited members.
     * @return the invited members as a mutable list
     */
    @NotNull Set<UUID> getInvites();

    /**
     * Handles a member chatting in this group.
     * @param chat the group chat message
     */
    void onChat(@NotNull GroupChatMessage<M> chat);

    void onJoin(@NotNull UUID member);

    void onLeave(@NotNull UUID member);

    void onInvite(@NotNull UUID member);

    void onUninvite(@NotNull UUID member);

    public record GroupChatMessage<E extends SocialMember>(@NotNull E member, @NotNull String message) {
    };
}

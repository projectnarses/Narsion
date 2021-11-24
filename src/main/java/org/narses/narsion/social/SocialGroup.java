package org.narses.narsion.social;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An interface that represents a social group.
 * @param <E> the elements that this group contains
 * @param <I> the info object of this group
 */
public interface SocialGroup<E extends SocialMember, I> {

    /**
     * Gets all online members in this group, filtered by the given function.
     * @param filter the filter
     * @return the filtered players
     */
    public @NotNull Collection<@NotNull E> getOnlineMembers(@NotNull Predicate<E> filter);

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
    default boolean add(@NotNull E member) {
        return this.add(member.getUuid());
    };

    /**
     * Removes a member from this group.
     * @param member the member to remove
     * @return true if the member was removed, false otherwise
     */
    default boolean remove(@NotNull E member) {
        return this.remove(member.getUuid());
    }
}

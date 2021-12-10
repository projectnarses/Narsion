package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SocialRank {
    EMPEROR(
            Component.text("Emperor"),
            true,
            true,
            true,
            true
    ),
    KING(
            Component.text("King"),
            true,
            true,
            false,
            false
    ),
    CONQUEROR(
            Component.text("Conqueror"),
            true,
            true,
            false,
            false
    ),
    ADVENTURER(
            Component.text("Adventurer"),
            true,
            true,
            false,
            false
    ),
    PEASANT(
            Component.text("Peasant"),
            false,
            false,
            false,
            false
    );

    public static final Map<String, SocialRank> name2Rank = Arrays.stream(values())
            .collect(Collectors.toMap(
                    rank -> rank.displayName.content(),
                    rank -> rank
            ));

    public static final Map<SocialRank, String> rank2Name = Arrays.stream(values())
            .collect(Collectors.toMap(
                    rank -> rank,
                    rank -> rank.displayName.content()
            ));

    private final TextComponent displayName;
    private final boolean hasClaimPerms;
    private final boolean hasModPerms;
    private final boolean hasAdminPerms;
    private final boolean hasWarPerms;

    SocialRank(TextComponent displayName, boolean hasClaimPerms, boolean hasModPerms, boolean hasAdminPerms, boolean hasWarPerms) {
        this.displayName = displayName;
        this.hasClaimPerms = hasClaimPerms;
        this.hasModPerms = hasModPerms;
        this.hasAdminPerms = hasAdminPerms;
        this.hasWarPerms = hasWarPerms;
    }

    /**
     * Gets the display name of this rank.
     * @return The display name of this rank.
     */
    public @NotNull TextComponent getDisplayName() {
        return this.displayName;
    }

    /**
     * Gets whether this rank has claim permissions.
     * @return Whether this rank has claim permissions.
     */
    public boolean hasClaimPerms() {
        return this.hasClaimPerms;
    }

    /**
     * Gets whether this rank has moderator permissions.
     * @return Whether this rank has moderator permissions.
     */
    public boolean hasModPerms() {
        return this.hasModPerms;
    }

    /**
     * Gets whether this rank has administrative permissions.
     * @return Whether this rank has administrative permissions.
     */
    public boolean hasAdminPerms() {
        return this.hasAdminPerms;
    }

    /**
     * Gets whether this rank has war permissions.
     * @return Whether this rank has war permissions.
     */
    public boolean hasWarPerms() {
        return this.hasWarPerms;
    }

    /**
     * Gets the permission level of this rank.
     * @return The permission level of this rank.
     */
    public int getPermissionLevel() {
        return this.ordinal();
    }

    /**
     * Returns whether this rank is the leader of a group
     * @return Whether this rank is the leader of a group
     */
    public boolean isLeader() {
        return this == EMPEROR;
    }

    /**
     * Returns whether the specified rank is higher than this rank
     * @param rank The rank to check
     * @return Whether the specified rank is higher than this rank
     */
    public boolean isHigherThan(@NotNull SocialRank rank) {
        return this.getPermissionLevel() < rank.getPermissionLevel();
    }

    /**
     * Returns whether the specified rank is lower than this rank
     * @param rank The rank to check
     * @return Whether the specified rank is lower than this rank
     */
    public boolean isLowerThan(@NotNull SocialRank rank) {
        return this.getPermissionLevel() > rank.getPermissionLevel();
    }

    /**
     * Returns whether the specified rank is equal to this rank
     * @param rank The rank to check
     * @return Whether the specified rank is equal to this rank
     */
    public boolean isEqualTo(@NotNull SocialRank rank) {
        return this.getPermissionLevel() == rank.getPermissionLevel();
    }

    public static @NotNull Component getDisplayNameOfNullable(@Nullable SocialRank rank) {
        if (rank == null) {
            return Component.text("None");
        }
        return rank.displayName;
    }
}

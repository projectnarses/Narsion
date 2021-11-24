package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SocialRank(
        int permissionLevel,
        Component displayName,
        boolean hasClaimPerms,
        boolean hasModPerms,
        boolean hasAdminPerms,
        boolean hasWarPerms
) implements Comparable<SocialRank> {
    public static final SocialRank EMPEROR = new SocialRank(
            0,
            Component.text("Emperor"),
            true,
            true,
            true,
            true
    );
    public static final SocialRank KING = new SocialRank(
            1,
            Component.text("King"),
            true,
            true,
            false,
            false
    );

    public static final SocialRank CONQUEROR = new SocialRank(
            2,
            Component.text("Conqueror"),
            true,
            true,
            false,
            false
    );

    public static final SocialRank ADVENTURER = new SocialRank(
            3,
            Component.text("Adventurer"),
            true,
            true,
            false,
            false
    );

    public static final SocialRank PEASANT = new SocialRank(
            4,
            Component.text("Peasant"),
            false,
            false,
            false,
            false
    );

    @Override
    public int compareTo(@NotNull SocialRank o) {
        return this.permissionLevel - o.permissionLevel;
    }

    public static @NotNull Component getDisplayName(@Nullable SocialRank rank) {
        if (rank == null) {
            return Component.text("None");
        }
        return rank.displayName;
    }

    public static SocialRank[] values() {
        return new SocialRank[] { EMPEROR, KING, CONQUEROR, ADVENTURER, PEASANT };
    }

    public boolean isLeader() {
        return this == EMPEROR;
    }
}

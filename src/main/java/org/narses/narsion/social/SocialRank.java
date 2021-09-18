package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;

public record SocialRank(
        int permissionLevel,
        boolean banPerms,
        boolean kickPerms,
        boolean mutePerms,
        boolean warnPerms,
        boolean votePerms
) implements Comparable<SocialRank> {
    public static final SocialRank LEADER = new SocialRank(0, true, true, true, true, false);
    public static final SocialRank OFFICER = new SocialRank(1, false, true, true, true, true);
    public static final SocialRank MEMBER = new SocialRank(2, false, false, false, false, false);

    @Override
    public int compareTo(@NotNull SocialRank o) {
        return this.permissionLevel - o.permissionLevel;
    }
}

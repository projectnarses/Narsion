package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface SocialMember {
    /**
     * Gets the rank of this member.
     * @return the rank of this member.
     */
    public @Nullable SocialRank getRank();

    /**
     * Gets the uuid of this member.
     * @return the uuid of this member.
     */
    public @NotNull UUID getUuid();
}

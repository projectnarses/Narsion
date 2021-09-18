package org.narses.narsion.region;

import org.jetbrains.annotations.NotNull;

/**
 * Type of regions sorted by size
 * 
 * @author Krystilize
 *
 */
public record RegionType(
        @NotNull String cleanName,
        @NotNull String id,
        boolean claimable
) {
    public static final RegionType NEUTRAL = new RegionType("Neutral", "NEUTRAL", false);
    public static final RegionType CITY = new RegionType("City", "CITY", true);
    public static final RegionType TOWN = new RegionType("Town", "TOWN", true);
    public static final RegionType FORTRESS = new RegionType("Fortress", "FORTRESS", true);
    public static final RegionType RESOURCE_LAND = new RegionType("Resource Land", "RESOURCE_LAND", true);
}

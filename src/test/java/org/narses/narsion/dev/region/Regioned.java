package org.narses.narsion.dev.region;


import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents an object that uses regions
 * @author Krystilize
 *
 */
public interface Regioned {
    /**
     * Gets the set of regions that the object is in
     *
     * @return regiondata the object's current regions
     */
    @NotNull Set<Region> getRegions();

    /**
     * Gets the position this regioned object is at
     *
     * @return the posigion of this regioned object
     */
    @NotNull Point getPosition();
}

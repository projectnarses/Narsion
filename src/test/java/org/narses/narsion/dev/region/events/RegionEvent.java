package org.narses.narsion.dev.region.events;

import net.minestom.server.event.trait.InstanceEvent;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.region.Region;

public interface RegionEvent extends InstanceEvent {

    /**
     * Gets the region associated with this event
     * @return the region
     */
    @NotNull Region getRegion();
}

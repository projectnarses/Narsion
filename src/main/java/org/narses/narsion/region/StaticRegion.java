package org.narses.narsion.region;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface StaticRegion extends Region, Supplier<Region> {
    @Override
    public default Region get() {
        return this;
    }

    @Override
    public default boolean addViewer(@NotNull Player player) {
        return getViewers().add(player);
    }

    @Override
    public default boolean removeViewer(@NotNull Player player) {
        return getViewers().remove(player);
    }
}

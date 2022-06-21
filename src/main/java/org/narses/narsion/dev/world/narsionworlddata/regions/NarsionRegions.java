package org.narses.narsion.dev.world.narsionworlddata.regions;

import org.jetbrains.annotations.NotNull;
import org.narses.narsion.region.Region;
import org.narses.narsion.util.Equivalent;

import java.util.function.Supplier;

public enum NarsionRegions implements Equivalent<Region> {
    ELSINORE(Elsinore.INSTANCE),
    ELSINORE_SOUTHERN_MARKETS(ElsinoreMarkets.South.INSTANCE),
    ELSINORE_NORTHERN_MARKETS(ElsinoreMarkets.North.INSTANCE),
    ;

    private final @NotNull Region region;

    NarsionRegions(@NotNull Supplier<Region> regionSupplier) {
        this.region = regionSupplier.get();
    }

    @Override
    public @NotNull Region getEquivalent() {
        return region;
    }
}
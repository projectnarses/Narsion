package org.narses.narsion.dev.region;

import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.region.regions.Elsinore;
import org.narses.narsion.dev.region.regions.ElsinoreDockTower;

import java.util.function.Supplier;

public enum Regions {
    ELSINORE(Elsinore::new),
    ELSINORE_DOCK_TOWER(ElsinoreDockTower::new),
    ;

    private final @NotNull Region region;

    Regions(@NotNull Supplier<Region> regionSupplier) {
        this.region = regionSupplier.get();
    }

    public @NotNull Region getRegion() {
        return region;
    }
}

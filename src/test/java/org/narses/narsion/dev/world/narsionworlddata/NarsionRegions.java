package org.narses.narsion.dev.world.narsionworlddata;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.math.geometry.Area3dPolygon;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.region.RegionType;

import java.util.Set;
import java.util.function.Supplier;

public enum NarsionRegions implements Region {
    ELSINORE(Elsinore.INSTANCE),
    ELSINORE_SOUTHERN_MARKETS(ElsinoreMarkets.South.INSTANCE),
    ELSINORE_NORTHERN_MARKETS(ElsinoreMarkets.North.INSTANCE),
    ;

    private final @NotNull Region region;

    NarsionRegions(@NotNull Supplier<Region> regionSupplier) {
        this.region = regionSupplier.get();
    }

    @Override
    public @NotNull String getName() {
        return region.getName();
    }

    @Override
    public boolean getClaimable() {
        return region.getClaimable();
    }

    @Override
    public @NotNull RegionType getType() {
        return region.getType();
    }

    @Override
    public @NotNull Area3dPolygon getPolygon() {
        return region.getPolygon();
    }

    @Override
    public boolean addViewer(@NotNull Player player) {
        return region.addViewer(player);
    }

    @Override
    public boolean removeViewer(@NotNull Player player) {
        return region.removeViewer(player);
    }

    @Override
    public @NotNull Set<Player> getViewers() {
        return region.getViewers();
    }
}

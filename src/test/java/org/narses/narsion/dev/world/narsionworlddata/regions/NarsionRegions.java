package org.narses.narsion.dev.world.narsionworlddata.regions;

import dev.emortal.rayfast.area.area3d.Area3d;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.region.Region;
import org.narses.narsion.region.RegionType;

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
    public @NotNull Area3d getArea() {
        return region.getArea();
    }

    @Override
    public void onPlayerEnter(@NotNull NarsionServer server, @NotNull Player player) {
        region.onPlayerEnter(server, player);
    }

    @Override
    public void onPlayerExit(@NotNull NarsionServer server, @NotNull Player player) {
        region.onPlayerExit(server, player);
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

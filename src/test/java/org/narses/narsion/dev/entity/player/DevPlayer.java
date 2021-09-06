package org.narses.narsion.dev.entity.player;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.region.Regioned;
import org.narses.narsion.player.NarsionPlayer;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class DevPlayer extends NarsionPlayer implements Regioned {

    public DevPlayer(@NotNull NarsionServer server, @NotNull Player player) {
        super(server, player);
    }

    // Regioned
    private final Set<Region> regionSet = new HashSet<>();

    @Override
    public @NotNull Set<Region> getRegions() {
        return regionSet;
    }

    @Override
    public @NotNull Point getPosition() {
        return player.getPosition();
    }
}

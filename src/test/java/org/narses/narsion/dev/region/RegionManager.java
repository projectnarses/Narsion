package org.narses.narsion.dev.region;

import dev.emortal.rayfast.grid.GridCast;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.utils.PacketUtils;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.math.MathUtil;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class RegionManager {

    private final @NotNull NarsionServer server;

    private Region[] allRegions = new Region[] {};

    public RegionManager(@NotNull NarsionServer server) {
        this.server = server;

        // Add the player tick listener
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerTickEvent.class, this::updatePlayer);

        // Schedule debug particles every second
        MinecraftServer.getSchedulerManager()
                .buildTask(() -> {
                    for (Region region : allRegions) {
                        showParticles(region);
                    }
                })
                .repeat(1, TimeUnit.SECOND)
                .schedule();
    }

    /**
     * Adds a region to the region manager
     * <br><br>
     * Note: This is an expensive operation and should only be done on startup
     * @param regions the region to add
     */
    public void addRegion(@NotNull Region... regions) {
        Set<Region> regionSet = Arrays.stream(allRegions).collect(Collectors.toSet());

        regionSet.addAll(List.of(regions));

        this.allRegions = regionSet.toArray(Region[]::new);
    }

    /**
     * Shows the walls of the region data
     */
    private void showParticles(Region region) {
        if (region.getViewers().size() <= 0) {
            return;
        }

        for (Pos[] face : region.getPolygon().getFaces()) {
            for (Pair<Pos, Pos> pair : MathUtil.makePairs(face)) {
                Pos first = pair.getFirst();

                Pos second = pair.getSecond();

                Vec direction = new Vec(second.x() - first.x(), second.y() - first.y(), second.z() - first.z());

                double length = direction.length();
                direction = direction.normalize();

                Iterator<double[]> iterator = GridCast.createExactGridIterator(
                        // Start
                        first.x(), first.y(), first.z(),

                        // Direction
                        direction.x(), direction.y(), direction.z(),

                        // Gridsize
                        5.0,

                        // Length
                        length
                );

                while (iterator.hasNext()) {
                    double[] position = iterator.next();
                    Point point = new Pos(position[0], position[1], position[2]);

                    ParticlePacket packet = ParticleCreator.createParticlePacket(Particle.LARGE_SMOKE,
                            point.x(), point.y(), point.z(),
                            0, 0, 0,
                            1
                    );

                    PacketUtils.sendGroupedPacket(region.getViewers(), packet, (player) -> player.getPosition().distance(point) < 60);
                }
            }
        }
    }

    // Called once every time a player gets ticked
    private void updatePlayer(PlayerTickEvent event) {
        NarsionPlayer playerWrapper = server.player(event.getPlayer());

        if (!(playerWrapper instanceof Regioned regioned)) {
            return;
        }

        // Update all regions
        Set<Region> regionSet = regioned.getRegions();
        Point position = regioned.getPosition();

        for (Region region : allRegions) {
            boolean inside = region.getPolygon().isPointInside3DPolygon(position);

            if (inside) {
                if (regionSet.add(region)) {
                    playerWrapper.getPlayer().sendMessage("Entering Region: " + region.getName());
                }
            } else {
                if (regionSet.remove(region)) {
                    playerWrapper.getPlayer().sendMessage("Exiting Region: " + region.getName());
                }
            }
        }
    }

}

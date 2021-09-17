package org.narses.narsion.dev.region;

import dev.emortal.rayfast.casting.grid.GridCast;
import dev.emortal.rayfast.vector.Vector3d;
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
import org.narses.narsion.dev.math.ArrayUtil;
import org.narses.narsion.dev.math.geometry.Area3dPolygon;
import org.narses.narsion.dev.world.narsionworlddata.NarsionRegions;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.util.Pair;

import java.util.*;

public class RegionManager {

    private final @NotNull NarsionServer server;

    private final @NotNull Region[] allRegions = NarsionRegions.values();

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
     * Shows the walls of the region data
     */
    private void showParticles(Region region) {
        if (region.getViewers().size() <= 0) {
            return;
        }

        if (!(region.getArea() instanceof Area3dPolygon polygon)) {
            return;
        }

        for (Pos[] face : polygon.getFaces()) {
            for (Pair<Pos, Pos> pair : ArrayUtil.makePairs(face)) {
                Pos first = pair.getFirst();

                Pos second = pair.getSecond();

                Vec direction = new Vec(second.x() - first.x(), second.y() - first.y(), second.z() - first.z());

                double length = direction.length();
                direction = direction.normalize();

                Iterable<Vector3d> iterable = GridCast.createExactGridIterator(
                        // Start
                        first.x(), first.y(), first.z(),

                        // Direction
                        direction.x(), direction.y(), direction.z(),

                        // Gridsize
                        5.0,

                        // Length
                        length
                );

                for (Vector3d position : iterable) {
                    Point point = new Pos(position.x(), position.y(), position.z());

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
        Player player = event.getPlayer();
        NarsionPlayer playerWrapper = server.wrap(player);

        if (!(playerWrapper instanceof Regioned regioned)) {
            return;
        }

        // Update all regions
        Set<Region> regionSet = regioned.getRegions();
        Point position = regioned.getPosition();

        for (Region region : allRegions) {
            boolean inside = region.getArea().containsPoint(position.x(), position.y(), position.z());

            if (inside) {
                if (regionSet.add(region)) {
                    region.onPlayerEnter(server, player);
                }
            } else {
                if (regionSet.remove(region)) {
                    region.onPlayerExit(server, player);
                }
            }
        }
    }

}

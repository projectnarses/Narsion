package org.narses.narsion.npc;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class NonPlayableCharacter extends LivingEntity implements NavigableEntity {

    private final Navigator navigator = new Navigator(this);

    protected NonPlayableCharacter(@NotNull UUID uuid) {
        super(EntityType.PLAYER, uuid);
    }

    /**
     * Gets the internal id of this npc
     */
    public abstract @NotNull String id();

    /**
     * Gets the home position of this npc
     */
    public abstract @NotNull Pos homePosition();

    /**
     * This is run when a player interacts with this npc
     */
    public void onInteract(@NotNull NarsionServer server, PlayerEntityInteractEvent event) {
    }

    @NotNull
    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void update(long time) {
        super.update(time);

        // Path finding
        this.navigator.tick(getAttributeValue(Attribute.MOVEMENT_SPEED));
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        this.navigator.setPathFinder(new HydrazinePathFinder(navigator.getPathingEntity(), instance.getInstanceSpace()));
        return super.setInstance(instance, spawnPosition);
    }
}

package org.narses.narsion.npc;

import com.extollit.gaming.ai.path.HydrazinePathFinder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NonPlayableCharacter extends LivingEntity implements NavigableEntity {

    private final @NotNull Navigator navigator = new Navigator(this);
    private final @NotNull String id;
    private final @NotNull Pos homePosition;
    private final @NotNull String name;
    private final @NotNull PlayerInfoPacket PLAYER_ADD_INFO;
    private final @NotNull PlayerInfoPacket PLAYER_HIDE_INFO;

    public NonPlayableCharacter(
            @NotNull UUID uuid,
            @NotNull String id,
            @NotNull Pos homePosition,
            @NotNull TextComponent displayName
    ) {
        super(EntityType.PLAYER, uuid);
        this.id = id;
        this.homePosition = homePosition;
        final String name = displayName.content();
        this.name = name.substring(0, Math.min(name.length(), 16));
        this.PLAYER_ADD_INFO = generatePlayerAddInfo();
        this.PLAYER_HIDE_INFO = generatePlayerHideInfo();
        this.setCustomName(displayName);
    }

    /**
     * Gets the internal id of this npc
     */
    public @NotNull String id() {
        return id;
    };

    /**
     * Gets the home position of this npc
     */
    public @NotNull Pos homePosition() {
        return homePosition;
    };

    /**
     * This is run whenever a player interacts with this npc
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
        this.navigator.tick();
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        this.navigator.setPathFinder(new HydrazinePathFinder(navigator.getPathingEntity(), instance.getInstanceSpace()));
        return super.setInstance(instance, spawnPosition);
    }

    private @NotNull PlayerInfoPacket generatePlayerAddInfo() {
        Component customName = getCustomName();
        Component displayName = customName == null ? Component.text(name) : customName;

        return new PlayerInfoPacket(
                PlayerInfoPacket.Action.ADD_PLAYER,
                List.of(new PlayerInfoPacket.AddPlayer(uuid, name, List.of(), GameMode.CREATIVE, 0, displayName, null))
        );
    }

    private @NotNull PlayerInfoPacket generatePlayerHideInfo() {
        return new PlayerInfoPacket(
                PlayerInfoPacket.Action.REMOVE_PLAYER,
                new PlayerInfoPacket.RemovePlayer(uuid)
        );
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        final PlayerConnection connection = player.getPlayerConnection();
        connection.sendPacket(PLAYER_ADD_INFO);
        // Hide npc from tablist
        // This needs to be delayed, otherwise the player does not render.
        this.scheduleNextTick((ignored) -> connection.sendPacket(PLAYER_HIDE_INFO));
        super.updateNewViewer(player);
    }

    public interface NPCSupplier {
        @NotNull NonPlayableCharacter create(
                @NotNull UUID uuid,
                @NotNull String id,
                @NotNull Pos homePosition,
                @NotNull TextComponent displayName
        );
    }
}

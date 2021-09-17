package org.narses.narsion;

import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.PlayerClasses;
import org.narses.narsion.events.Events;
import org.narses.narsion.item.NarsionItemDataProvider;
import org.narses.narsion.item.NarsionItemStackProvider;
import org.narses.narsion.npc.NonPlayableCharacter;
import org.narses.narsion.npc.NonPlayableCharacterSource;
import org.narses.narsion.player.NarsionPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The entrypoint of the NarsionServer
 *
 * This class is designed to be extended to provide a specific flavour of the narses game logic
 */
public abstract class NarsionServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NarsionServer.class);

    private static final File configFile = new File("Config.toml");

    protected final @NotNull Toml config = new Toml().read(configFile);
    protected final @NotNull ItemStackProvider itemStackProvider;
    protected final @NotNull ItemDataProvider itemDataProvider;
    protected final @NotNull Function<Player, ? extends NarsionPlayer> playerWrapperFunction;
    protected final @NotNull PlayerClasses playerClasses;
    protected final @NotNull Collection<NonPlayableCharacterSource> nonPlayableCharacterSources;
    private final @NotNull PlayerInfoPacket npcAddInfoPacket;
    private final @NotNull PlayerInfoPacket npcHideInfoPacket;

    public NarsionServer(
            @NotNull MinecraftServer server,
            @NotNull EventNode<Event> eventNode,
            @NotNull NarsionItemDataProvider itemDataProvider,
            @NotNull BiFunction<NarsionServer, Player, ? extends NarsionPlayer> playerWrapperFunction,
            @NotNull PlayerClasses playerClasses,
            @NotNull Collection<NonPlayableCharacterSource> nonPlayableCharacterSources
    ) {
        // Item data + provider
        this.itemDataProvider = itemDataProvider;
        this.itemStackProvider = new NarsionItemStackProvider(itemDataProvider);

        // Player wrapper function wrapper (one too many wrappers)
        this.playerWrapperFunction = (player) -> {
            // Ensure player is in map
            playerNarsionPlayerMap.computeIfAbsent(player, (somePlayer) -> playerWrapperFunction.apply(this, somePlayer));

            // Return player from map
            return playerNarsionPlayerMap.get(player);
        };


        // Player classes
        this.playerClasses = playerClasses;

        // Register events
        new Events(this).registerAll(eventNode);

        // Npc sources
        this.nonPlayableCharacterSources = nonPlayableCharacterSources;

        {
            // Remove player from player map on leave
            EventListener<PlayerDisconnectEvent> listener = EventListener.builder(PlayerDisconnectEvent.class)
                    .ignoreCancelled(true)
                    .handler(event -> playerNarsionPlayerMap.remove(event.getPlayer()))
                    .build();
            MinecraftServer.getGlobalEventHandler().addListener(listener);
        }

        // Collect npc info
        {
            List<PlayerInfoPacket.PlayerInfo> playerAddInfoList = nonPlayableCharacterSources.stream()
                    .map(NonPlayableCharacterSource::generatePlayerAddInfo)
                    .collect(Collectors.toList());

            List<PlayerInfoPacket.PlayerInfo> playerHideInfoList = nonPlayableCharacterSources.stream()
                    .map(NonPlayableCharacterSource::generatePlayerHideInfo)
                    .collect(Collectors.toList());

            // Create packets
            {
                PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER);
                packet.playerInfos = playerAddInfoList;
                this.npcAddInfoPacket = packet;
            }
            {
                PlayerInfoPacket packet = new PlayerInfoPacket(PlayerInfoPacket.Action.REMOVE_PLAYER);
                packet.playerInfos = playerHideInfoList;
                this.npcHideInfoPacket = packet;
            }
        }
    }

    public @NotNull Toml getConfig() {
        return config;
    }

    public @NotNull ItemStackProvider getItemStackProvider() {
        return itemStackProvider;
    }

    public @NotNull ItemDataProvider getItemDataProvider() {
        return itemDataProvider;
    }

    public @NotNull Logger getLogger() {
        return LOGGER;
    }

    protected void spawnNpcs(@NotNull Instance instance) {
        // Spawn all npcs & create player info packet
        {
            // Spawn all characters
            List<NonPlayableCharacter> characters = nonPlayableCharacterSources.stream()
                    .map(npc -> npc.spawn(instance))
                    .toList();
        }
    }

    // Map to hold all NarsionPlayer instances
    private final Map<Player, NarsionPlayer> playerNarsionPlayerMap = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Wraps the specified player using the player wrapper function provided in the constructor.
     *
     * @param player the player to wrap
     * @param <P> the wrapped player object
     * @return the wrapped player object
     */
    @SuppressWarnings("unchecked")
    public @NotNull <P extends NarsionPlayer> P wrap(Player player) {
        return (P) playerWrapperFunction.apply(player);
    }

    public @NotNull PlayerClasses getPlayerClasses() {
        return playerClasses;
    };

    public @NotNull PlayerInfoPacket getNpcAddInfoPacket() {
        return npcAddInfoPacket;
    }

    public @NotNull PlayerInfoPacket getNpcHideInfoPacket() {
        return npcHideInfoPacket;
    }
}

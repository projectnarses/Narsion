package org.narses.narsion;

import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.classes.PlayerClasses;
import org.narses.narsion.item.NarsionItemDataProvider;
import org.narses.narsion.item.NarsionItemStackProvider;
import org.narses.narsion.player.NarsionPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    public NarsionServer(
            @NotNull MinecraftServer server,
            @NotNull NarsionItemDataProvider itemDataProvider,
            @NotNull BiFunction<NarsionServer, Player, ? extends NarsionPlayer> playerWrapperFunction,
            @NotNull PlayerClasses playerClasses
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

    // Map to hold all NarsionPlayer instances
    private static final Map<Player, NarsionPlayer> playerNarsionPlayerMap = new ConcurrentHashMap<>();

    /**
     * Wraps the specified player using the player wrapper function provided in the constructor.
     *
     * @param player the player to wrap
     * @param <P> the wrapped player object
     * @return the wrapped player object
     */
    @SuppressWarnings("unchecked")
    public @NotNull <P extends NarsionPlayer> P player(Player player) {
        return (P) playerWrapperFunction.apply(player);
    }

    public PlayerClasses getPlayerClasses() {
        return playerClasses;
    };
}

package org.narses.narsion;

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

import java.util.function.Function;

/**
 * The entrypoint of the NarsionServer
 *
 * This class is designed to be extended to provide a specific flavour of the narses game logic
 */
public abstract class NarsionServer<P extends NarsionPlayer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NarsionServer.class);

    private final @NotNull ItemStackProvider itemStackProvider;
    private final @NotNull ItemDataProvider itemDataProvider;
    private final @NotNull Function<Player, P> playerWrapperFunction;
    private final @NotNull PlayerClasses playerClasses;

    public NarsionServer(
            @NotNull MinecraftServer server,
            @NotNull NarsionItemDataProvider itemDataProvider,
            @NotNull Function<Player, P> playerWrapperFunction,
            @NotNull PlayerClasses playerClasses
    ) {
        // Item data + provider
        this.itemDataProvider = itemDataProvider;
        this.itemStackProvider = new NarsionItemStackProvider(itemDataProvider);

        // Player wrapper
        this.playerWrapperFunction = playerWrapperFunction;

        // Player classes
        this.playerClasses = playerClasses;
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

    public @NotNull P getPlayerWrapper(Player player) {
        return playerWrapperFunction.apply(player);
    }
}

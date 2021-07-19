package org.narses.narsion;

import net.minestom.server.MinecraftServer;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.item.NarsionItemDataProvider;
import org.narses.narsion.item.NarsionItemStackProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entrypoint of the NarsionServer
 *
 * This class is designed to be extended to provide a specific flavour of the narses game logic
 */
public class NarsionServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NarsionServer.class);
    private final @NotNull ItemStackProvider itemStackProvider;
    private final @NotNull ItemDataProvider itemDataProvider;

    public NarsionServer(@NotNull MinecraftServer server, @NotNull NarsionItemDataProvider itemDataProvider) {
        // Item data + provider
        this.itemDataProvider = itemDataProvider;
        this.itemStackProvider = new NarsionItemStackProvider(itemDataProvider);
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
}

package org.narses.narsion.dev;

import net.minestom.server.MinecraftServer;
import org.itemize.ItemStackProvider;
import org.itemize.data.ItemDataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The entrypoint of the NarsionServer
 *
 * This class is designed to be extended to provide a specific flavour of the narses game logic
 */
public class NarsionServer {

    private final @NotNull ItemStackProvider itemStackProvider;
    private final @NotNull ItemDataProvider itemDataProvider;

    NarsionServer(@NotNull MinecraftServer server, @NotNull ItemDataProvider itemDataProvider) {
        // Item data + provider
        this.itemDataProvider = itemDataProvider;
        this.itemStackProvider = new ItemStackProvider(itemDataProvider);
    }

    public @NotNull ItemStackProvider getItemStackProvider() {
        return itemStackProvider;
    }

    public @NotNull ItemDataProvider getItemDataProvider() {
        return itemDataProvider;
    }
}

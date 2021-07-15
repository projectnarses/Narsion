package org.narses.narsion;

import net.minestom.server.Bootstrap;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * This class is responsible for setting up mixins and the startConsumer before initializing the server
 */
public class BootstrapDevServer {
    public static void main(String[] args) {
        // Initialize the server
        Bootstrap.bootstrap("org.narses.narsion.DevServer", args);
    }
}

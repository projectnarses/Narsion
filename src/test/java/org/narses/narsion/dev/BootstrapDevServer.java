package org.narses.narsion.dev;

import net.minestom.server.Bootstrap;

/**
 * This class is responsible for setting up mixins and the startConsumer before initializing the dev server
 */
public class BootstrapDevServer {
    public static void main(String[] args) {
        // Initialize the server
        Bootstrap.bootstrap("org.narses.narsion.dev.DevServer", args);
    }
}

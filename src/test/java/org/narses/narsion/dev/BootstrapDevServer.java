package org.narses.narsion.dev;

/**
 * This class is responsible for setting up mixins before initializing the dev server
 */
public class BootstrapDevServer {
    public static void main(String[] args) {
        // Initialize the server
        // Bootstrap.bootstrap("org.narses.narsion.dev.DevServer", args);
        DevServer.main(args);
    }
}

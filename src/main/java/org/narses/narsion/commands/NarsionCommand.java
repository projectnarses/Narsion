package org.narses.narsion.commands;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

public abstract class NarsionCommand<S extends NarsionServer> extends Command {

    protected S server;

    public NarsionCommand(@NotNull S server, @NotNull String name, @NotNull String... alias) {
        super(name, alias);
        this.server = server;
    }
}

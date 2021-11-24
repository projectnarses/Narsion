package org.narses.narsion.dev.commands;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;

import java.util.function.Function;

// An enum of all the narsion commands
public enum NarsionCommands {
    CLASS(ClassCommand::new),
    DEBUG(DebugCommand::new),
    GAMEMODE(GamemodeCommand::new),
    GUILD(GuildCommand::new),
    ITEM(ItemCommand::new),
    ;

    private final Function<DevServer, Command> commandFunction;

    NarsionCommands(@NotNull Function<DevServer, Command> commandFunction) {
        this.commandFunction = commandFunction;
    }

    public @NotNull Command getCommand(@NotNull DevServer server) {
        return commandFunction.apply(server);
    }
}

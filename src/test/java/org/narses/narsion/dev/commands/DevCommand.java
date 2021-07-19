package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DevCommand extends Command {


    public DevCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        this.addSyntax(DevCommand::usage,
            ArgumentType.StringArray("args")
                .setSuggestionCallback(DevCommand::handleSuggestion)
        );
    }

    private static void handleSuggestion(CommandSender sender, CommandContext context, Suggestion suggestion) {
        String[] args = context.get("args");

        // Code on tab complete
    }

    private static void usage(CommandSender sender, CommandContext context) {
        String[] args = context.get("args");

        // Code on command execute
    }
}

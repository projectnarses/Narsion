package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.commands.NarsionCommand;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.player.NarsionPlayer;

public class DebugCommand extends NarsionCommand<DevServer> {

    public DebugCommand(@NotNull DevServer server) {
        super(server, "debug");
        this.server = server;

        this.addSyntax(this::usage,
            ArgumentType.StringArray("args")
                .setSuggestionCallback(this::handleSuggestion)
        );
        this.setDefaultExecutor(this::usage);
    }

    private void handleSuggestion(CommandSender sender, CommandContext context, Suggestion suggestion) {
        String[] args = context.get("args");

        // Code on tab complete

    }

    private void usage(CommandSender sender, CommandContext context) {
        Player player = (Player) sender;
        NarsionPlayer narsionPlayer = server.wrap(player);

        server.getSocialsManager().createGuild("EXAMPLE", narsionPlayer);
    }
}

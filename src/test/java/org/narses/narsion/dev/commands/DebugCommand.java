package org.narses.narsion.dev.commands;

import kotlin.random.Random;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.entity.Player;
import net.minestom.server.map.MapColors;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class DebugCommand extends Command {

    private final @NotNull DevServer server;

    public DebugCommand(@NotNull DevServer server) {
        super("debug");
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
    }
}

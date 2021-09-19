package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.dev.inventory.MerchantInventory;

public class DebugCommand extends Command {

    private final @NotNull DevServer server;

    public DebugCommand(@NotNull DevServer server) {
        super("debug");
        this.server = server;

        this.addSyntax(DebugCommand::usage,
            ArgumentType.StringArray("args")
                .setSuggestionCallback(DebugCommand::handleSuggestion)
        );
    }

    private static void handleSuggestion(CommandSender sender, CommandContext context, Suggestion suggestion) {
        String[] args = context.get("args");

        // Code on tab complete
    }

    private static void usage(CommandSender sender, CommandContext context) {
        String[] args = context.get("args");

        // Code on command execute
        System.out.println("debug");
        sender.asPlayer().openInventory(new MerchantInventory());
    }
}

package org.narses.narsion.dev.commands;

import kotlin.random.Random;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.Suggestion;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.dev.inventory.MerchantInventory;
import org.narses.narsion.item.data.NarsionItems;

import java.util.Map;
import java.util.UUID;

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
        String[] args = context.get("args");

        int length = Random.Default.nextInt(20);

        if (args != null && args.length > 0) {
            length = Integer.parseInt(args[0]);
        }

        // Code on command execute
        for (int i = 0; i < length; i++) {
            MinecraftServer.getCommandManager().execute(sender, "item IRON");
            MinecraftServer.getCommandManager().execute(sender, "item COW_LEATHER");
            MinecraftServer.getCommandManager().execute(sender, "item COAL");
        }
    }
}

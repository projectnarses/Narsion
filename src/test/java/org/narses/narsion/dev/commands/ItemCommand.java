package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.NarsionServer;

import java.util.Set;
import java.util.UUID;

public class ItemCommand extends Command {

    private final NarsionServer server;

    public ItemCommand(NarsionServer server) {
        super("item");
        this.server = server;

        String[] itemIDs = server.getItemDataProvider()
                .getItemData()
                .keySet()
                .toArray(String[]::new);

        this.addSyntax(
                this::usage,
                Word("item_id")
                        .from(itemIDs)
        );
    }

    private void usage(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player)) {
            // TODO: Error message
            return;
        }

        // Get item ID & generate item
        String itemID = context.get("item_id");

        ItemStack item = server.getItemStackProvider()
                .create(itemID, new UUID(0, 0), null);

        // Add item to player
        sender.asPlayer().getInventory().addItemStack(item);
    }
}

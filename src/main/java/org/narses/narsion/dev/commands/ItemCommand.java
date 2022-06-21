package org.narses.narsion.dev.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.commands.NarsionCommand;

import java.util.UUID;

import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

public class ItemCommand extends NarsionCommand<NarsionServer> {

    public ItemCommand(NarsionServer server) {
        super(server, "item");
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
                .create(
                        itemID,
                        new UUID(0, 0),
                        null
                );

        // Add item to player
        sender.asPlayer().getInventory().addItemStack(item);

        server.getLogger().info("Player " + sender.asPlayer().getUsername() + " was given item: " + item.getMeta().toSNBT());
    }
}

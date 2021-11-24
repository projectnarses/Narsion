package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

class GuildCreateCommand extends GuildlessRequiredCommand {
    public GuildCreateCommand(@NotNull NarsionServer server) {
        super(server, "create");

        // /guild create <name>
        this.addSyntax(
                this::handle,
                Word("name")
        );
    }

    private void handle(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            throw new IllegalArgumentException("The sender must be a player");
        }
        NarsionPlayer narsionPlayer = server.wrap(player);


        String name = context.get("name");

        if (name.length() > 16) {
            player.sendMessage("The name must be shorter than 16 characters");
            return;
        }

        // Create the guild
        server.getSocialsManager().createGuild(name, narsionPlayer);
        player.sendMessage("Guild created");
    }
}

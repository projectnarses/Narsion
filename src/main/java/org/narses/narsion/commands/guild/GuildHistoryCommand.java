package org.narses.narsion.commands.guild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

class GuildHistoryCommand extends Command {
    private final NarsionServer server;

    public GuildHistoryCommand(NarsionServer server) {
        super("history");
        this.server = server;
        this.setDefaultExecutor(this::handleDefault);
    }

    private void handleDefault(CommandSender sender, CommandContext context) {
        // TODO: Handle database history lookup

        // Ensure only players can use this command
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be executed by a player");
            return;
        }

        NarsionPlayer narsionPlayer = server.wrap(player);


        TextComponent.Builder builder = Component.text();

        builder.append(Component.text("Guild History: \n"));
        builder.append(Component.text("\n"));
        builder.append(Component.text("Current Guild: " + narsionPlayer.getGuild()));

        player.sendMessage(builder.build());

    }
}

package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

class GuildLeaveCommand extends GuildRequiredCommand {
    public GuildLeaveCommand(@NotNull NarsionServer server) {
        super(server, "leave");
        this.addSyntax(
                this::handle
        );
    }

    private void handle(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            throw new IllegalArgumentException("Only players can use this command.");
        }
        server.getSocialsManager().removePlayerFromGuild(player.getUuid());
        player.sendMessage("You left the guild.");
    }
}

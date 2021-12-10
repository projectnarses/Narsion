package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.social.SocialRank;

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

        NarsionPlayer narsionPlayer = server.wrap(player);

        if (narsionPlayer.getRank() != null && narsionPlayer.getRank().isLeader()) {
            player.sendMessage("You can't leave the guild because you are the guild's leader.");
            return;
        }

        server.getSocialsManager().kickMemberFromGroup(player.getUuid());
        player.sendMessage("You left the guild.");
    }
}

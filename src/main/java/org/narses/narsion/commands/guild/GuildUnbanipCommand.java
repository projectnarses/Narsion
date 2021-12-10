package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialRank;

import java.util.UUID;

import static net.minestom.server.command.builder.arguments.ArgumentType.Entity;
import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

class GuildUnbanipCommand extends GuildRequiredCommand {

    public GuildUnbanipCommand(@NotNull NarsionServer server) {
        super(server, "unbanip");
        this.setRankPredicate(SocialRank::hasAdminPerms);
        this.addSyntax(
                this::handlePlayer,
                Entity("player")
                        .onlyPlayers(true)
                        .singleEntity(true)
        );
    }

    private void handlePlayer(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);
        EntityFinder finder = context.get("player");
        var manager = server.getSocialsManager();
        Player playerToUnban = finder.findFirstPlayer(sender);

        if (playerToUnban == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerToUnbanUuid = playerToUnban.getUuid();

        if (!guild.contains(playerToUnbanUuid)) {
            sender.sendMessage("Player is not in your guild.");
            return;
        }

        SocialRank banRank = manager.getRank(playerToUnbanUuid);

        if (banRank == null) {
            throw new IllegalStateException("Player rank is null");
        }

        if (banRank.isLeader()) {
            sender.sendMessage("You can't unban a leader.");
            return;
        }

        if (sender instanceof Player player) {
            SocialRank rank = manager.getRank(player.getUuid());

            if (rank == null) {
                throw new IllegalStateException("Rank is null");
            }

            if (!banRank.isLowerThan(rank)) {
                sender.sendMessage("You can't unbanip a player with a rank higher or equal to yours.");
                return;
            }

            manager.unbanIpFromGroup(playerToUnban.getPlayerConnection().getRemoteAddress(), guild.getUuid());
            playerToUnban.sendMessage("You have been unbanip from the guild " + guild.getName() + " by " + player.getUsername());
        } else {
            manager.unbanIpFromGroup(playerToUnban.getPlayerConnection().getRemoteAddress(), guild.getUuid());
            playerToUnban.sendMessage("You have been unbanip from the guild " + guild.getName() + ".");
        }
    }
}

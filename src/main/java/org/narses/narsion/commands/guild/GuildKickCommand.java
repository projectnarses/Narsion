package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialRank;

import java.util.UUID;

import static net.minestom.server.command.builder.arguments.ArgumentType.Entity;

class GuildKickCommand extends GuildRequiredCommand {

    public GuildKickCommand(@NotNull NarsionServer server) {
        super(server, "kick");
        this.setRankPredicate(SocialRank::hasModPerms);
        this.addSyntax(
                this::handleString,
                Entity("player")
                        .onlyPlayers(true)
                        .singleEntity(true)
        );
    }

    private void handleString(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);
        EntityFinder finder = context.get("player");
        var manager = server.getSocialsManager();
        Player playerToKick = finder.findFirstPlayer(sender);

        if (playerToKick == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerToKickUuid = playerToKick.getUuid();

        if (!guild.contains(playerToKickUuid)) {
            sender.sendMessage("Player is not in your guild.");
            return;
        }

        SocialRank kickRank = manager.getRank(playerToKickUuid);

        if (kickRank == null) {
            throw new IllegalStateException("Player rank is null");
        }

        if (kickRank.isLeader()) {
            sender.sendMessage("You can't kick a leader.");
            return;
        }

        manager.kickMemberFromGroup(playerToKickUuid);

        if (sender instanceof Player player) {
            SocialRank rank = manager.getRank(player.getUuid());

            if (rank == null) {
                throw new IllegalStateException("Rank is null");
            }
            if (!kickRank.isLowerThan(rank)) {
                sender.sendMessage("You can't kick a player with a rank higher or equal to yours.");
            }

            playerToKick.sendMessage("You have been kicked from the guild " + guild.getName() + " by " + player.getUsername());
        } else {
            playerToKick.sendMessage("You have been invited to the guild " + guild.getName() + ".");
        }
    }
}

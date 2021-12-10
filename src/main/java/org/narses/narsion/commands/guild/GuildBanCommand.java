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

class GuildBanCommand extends GuildRequiredCommand {

    public GuildBanCommand(@NotNull NarsionServer server) {
        super(server, "ban");
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
        Player playerToBan = finder.findFirstPlayer(sender);

        if (playerToBan == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerToBanUuid = playerToBan.getUuid();

        if (!guild.contains(playerToBanUuid)) {
            sender.sendMessage("Player is not in your guild.");
            return;
        }

        SocialRank kickRank = manager.getRank(playerToBanUuid);

        if (kickRank == null) {
            throw new IllegalStateException("Player rank is null");
        }

        if (kickRank.isLeader()) {
            sender.sendMessage("You can't ban a leader.");
            return;
        }

        manager.banMemberFromGroup(playerToBanUuid, guild.getUuid());

        if (sender instanceof Player player) {
            SocialRank rank = manager.getRank(player.getUuid());

            if (rank == null) {
                throw new IllegalStateException("Rank is null");
            }

            if (!kickRank.isLowerThan(rank)) {
                sender.sendMessage("You can't ban a player with a rank higher or equal to yours.");
            }

            playerToBan.sendMessage("You have been ban from the guild " + guild.getName() + " by " + player.getUsername());
        } else {
            playerToBan.sendMessage("You have been ban from the guild " + guild.getName() + ".");
        }
    }
}

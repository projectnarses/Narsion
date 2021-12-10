package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialGroup;
import org.narses.narsion.social.SocialRank;

import java.util.UUID;

import static net.minestom.server.command.builder.arguments.ArgumentType.Entity;
import static net.minestom.server.command.builder.arguments.ArgumentType.Enum;

class GuildPromoteCommand extends GuildRequiredCommand {

    public GuildPromoteCommand(@NotNull NarsionServer server) {
        super(server, "promote");
        this.setRankPredicate(SocialRank::hasModPerms);
        this.addSyntax(
                this::handle,
                Entity("player")
                        .onlyPlayers(true)
                        .singleEntity(true),
                Enum("rank", SocialRank.class)
        );
    }

    private void handle(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);

        // Find the rank to promote to
        @NotNull SocialRank promotionRank = context.get("rank");

        // Find the player to promote
        EntityFinder finder = context.get("player");
        Player target = finder.findFirstPlayer(sender);

        var manager = server.getSocialsManager();

        if (target == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID targetUuid = target.getUuid();

        if (!guild.contains(targetUuid)) {
            sender.sendMessage("Player is not in your guild.");
            return;
        }

        SocialRank targetRank = manager.getRank(targetUuid);

        if (targetRank == null) {
            throw new IllegalStateException("Player rank is null");
        }

        if (targetRank.isLeader()) {
            sender.sendMessage("You can't promote a leader.");
            return;
        }

        if (!promotionRank.isHigherThan(targetRank)) {
            sender.sendMessage(
                    "Going from rank " + targetRank.getDisplayName().content() +
                            " to rank " + promotionRank.getDisplayName().content() +
                            " is not a promotion."
            );
            return;
        }

        if (sender instanceof Player player) {
            SocialRank rank = manager.getRank(player.getUuid());

            if (rank == null) {
                throw new IllegalStateException("Rank is null");
            }

            if (!targetRank.isLowerThan(rank)) {
                sender.sendMessage("You can't promote a player with a rank higher or equal to yours.");
                return;
            }

            if (!promotionRank.isLowerThan(rank)) {
                sender.sendMessage("You cannot promote to a rank equal to or higher then yours.");
                return;
            }
        }

        manager.setRank(targetUuid, promotionRank);
        guild.onPromote(new SocialGroup.Promote(targetUuid, promotionRank));
        target.sendMessage("You have been promoted to " + promotionRank.getDisplayName().content() + ".");
    }
}

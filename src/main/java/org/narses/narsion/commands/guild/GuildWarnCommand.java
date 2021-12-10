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
import static net.minestom.server.command.builder.arguments.ArgumentType.StringArray;

class GuildWarnCommand extends GuildRequiredCommand {

    public GuildWarnCommand(@NotNull NarsionServer server) {
        super(server, "warn");
        this.setRankPredicate(SocialRank::hasModPerms);
        this.addSyntax(
                this::handleString,
                Entity("player")
                        .onlyPlayers(true)
                        .singleEntity(true),
                StringArray("reason")
        );
    }

    private void handleString(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);
        EntityFinder finder = context.get("player");
        String[] reason = context.get("reason");
        var manager = server.getSocialsManager();
        Player playerToInvite = finder.findFirstPlayer(sender);

        if (playerToInvite == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerToWarnUuid = playerToInvite.getUuid();

        if (!guild.contains(playerToWarnUuid)) {
            sender.sendMessage("Player is not in your guild.");
            return;
        }

        SocialRank warnRank = manager.getRank(playerToWarnUuid);

        if (warnRank == null) {
            throw new IllegalStateException("Player rank is null");
        }

        if (warnRank.isLeader()) {
            sender.sendMessage("You can't warn a leader.");
            return;
        }

        if (sender instanceof Player player) {
            SocialRank rank = manager.getRank(player.getUuid());

            if (rank == null) {
                throw new IllegalStateException("Rank is null");
            }

            if (!warnRank.isLowerThan(rank)) {
                sender.sendMessage("You can't warn a player with a rank higher or equal to yours.");
                return;
            }

            playerToInvite.sendMessage("[Guild] You have been warned by " + player.getUsername() + ": " + String.join(" ", reason));
        } else {
            playerToInvite.sendMessage("[Guild] You have been warned: " + String.join(" ", reason));
        }
    }
}

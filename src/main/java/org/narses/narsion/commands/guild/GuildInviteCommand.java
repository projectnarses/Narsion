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

class GuildInviteCommand extends GuildRequiredCommand {

    public GuildInviteCommand(@NotNull NarsionServer server) {
        super(server, "invite");
        this.setRankPredicate(SocialRank::hasModPerms);
        this.addSyntax(
                this::handle,
                Entity("player")
                        .onlyPlayers(true)
                        .singleEntity(true)
        );
    }

    private void handle(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);
        EntityFinder finder = context.get("player");
        Player playerToInvite = finder.findFirstPlayer(sender);

        if (playerToInvite == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        UUID playerToInviteUuid = playerToInvite.getUuid();

        if (guild.contains(playerToInviteUuid)) {
            sender.sendMessage("Player is already in the guild.");
            return;
        }

        if (guild.getInvites().contains(playerToInviteUuid)) {
            sender.sendMessage("Player is already invited.");
            return;
        }

        server.getSocialsManager().inviteMemberToGroup(playerToInviteUuid, guild.getUuid());

        if (sender instanceof Player player) {
            playerToInvite.sendMessage("You have been invited to the guild " + guild.getName() + " by " + player.getUsername());
        } else {
            playerToInvite.sendMessage("You have been invited to the guild " + guild.getName() + ".");
        }
    }
}

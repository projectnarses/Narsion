package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialsManager;

import static net.minestom.server.command.builder.arguments.ArgumentType.Word;

class GuildJoinCommand extends GuildlessRequiredCommand {
    public GuildJoinCommand(@NotNull NarsionServer server) {
        super(server, "join");

        // /guild create <name>
        this.addSyntax(
                this::handle,
                Word("guild_name")
        );
    }

    private void handle(CommandSender sender, CommandContext context) {
        if (!(sender instanceof Player player)) {
            throw new IllegalArgumentException("The sender must be a player");
        }
        SocialsManager manager = server.getSocialsManager();

        String name = context.get("guild_name");

        if (name.length() > 16) {
            player.sendMessage("The name must be shorter than 16 characters");
            return;
        }

        Guild guild = server.getSocialsManager().getClosestGuild(name);

        if (guild == null) {
            sender.sendMessage("Guild not found");
            return;
        }

        // Find if the guild has invited you
        if (!guild.getInvites().contains(player.getUuid())) {
            player.sendMessage("You are not invited to this guild");
            return;
        }

        // Finally, join the guild
        manager.addPlayerToGuild(player, guild, manager.getDefaultRank());
        player.sendMessage("You joined the guild " + guild.getName());
    }
}

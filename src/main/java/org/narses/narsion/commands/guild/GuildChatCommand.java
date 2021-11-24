package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialGroup;

class GuildChatCommand extends GuildRequiredCommand {
    public GuildChatCommand(@NotNull NarsionServer server) {
        super(server, "chat");
        this.addSyntax(
                this::handleString,
                ArgumentType.StringArray("chat")
        );
    }

    private void handleString(CommandSender sender, CommandContext context) {
        Guild guild = getGuild(sender);

        if (!(sender instanceof Player player)) {
            throw new IllegalArgumentException("Only players can use this command.");
        }
        NarsionPlayer narsionPlayer = server.wrap(player);

        String[] chat = context.get("chat");

        guild.onChat(new SocialGroup.GroupChatMessage<>(
                narsionPlayer,
                String.join(" ", chat)
        ));
    }
}

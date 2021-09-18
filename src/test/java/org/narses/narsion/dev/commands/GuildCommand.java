package org.narses.narsion.dev.commands;

import kotlin.collections.ArraysKt;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.dev.player.DevPlayer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.PlayerGroup;
import org.narses.narsion.social.SocialsManager;

import java.util.UUID;

public class GuildCommand extends Command {

    private final @NotNull DevServer server;
    private final @NotNull SocialsManager SOCIALS_MANAGER;

    public GuildCommand(@NotNull DevServer server) {
        super("guild", "g", "gld");

        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();

        this.addSubcommand(new CreateCommand());

        this.addConditionalSyntax(
                this::playerHasGuild,
                this::usageInfo,
                Literal("info")
        );
    }

    private boolean playerHasGuild(CommandSender sender, String str) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        return SOCIALS_MANAGER.getGuildFromPlayer(player) != null;
    }

    // /guild info
    private void usageInfo(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();

        Guild guild = SOCIALS_MANAGER.getGuildFromPlayer(player);

        if (guild == null) {
            player.sendMessage("You have no guild."); // TODO: Move to config
            return;
        }

        for (@NotNull Component component : guild.getInfo().clean()) {
            player.sendMessage(component);
        }
    }

    // /guild create exampleName
    private class CreateCommand extends Command {
        private CreateCommand() {
            super("create");
            this.setCondition(this::isAllowed);
            this.addSyntax(this::usage, Word("name"));
        }

        private boolean isAllowed(CommandSender sender, String str) {
            return !playerHasGuild(sender, str);
        }

        private void usage(CommandSender sender, CommandContext context) {
            Player player = sender.asPlayer();
            String guildName = context.get("name");

            Guild guild = SOCIALS_MANAGER.createGuild(guildName, player);

            for (Component component : guild.getInfo().clean()) {
                player.sendMessage(component);
            }

            player.refreshCommands();
        }
    }

}

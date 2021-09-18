package org.narses.narsion.dev.commands;

import kotlin.collections.ArraysKt;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.DevServer;
import org.narses.narsion.dev.player.DevPlayer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialRank;
import org.narses.narsion.social.SocialsManager;

public class GuildCommand extends Command {

    private final @NotNull DevServer server;
    private final @NotNull SocialsManager SOCIALS_MANAGER;

    public GuildCommand(@NotNull DevServer server) {
        super("guild", "g", "gld");

        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();

        this.addConditionalSyntax(
                this::playerHasNoGuild,
                this::usageCreate,
                Literal("create")
        );

        this.addSyntax(
                this::usageInfo,
                Literal("info")
        );

        this.addConditionalSyntax(
                this::playerHasGuild,
                this::usageLeave,
                Literal("leave")
        );
    }

    private void usageLeave(@NotNull CommandSender sender, @NotNull CommandContext context) {
        Player player = sender.asPlayer();

        if (SOCIALS_MANAGER.removePlayerFromGuild(player)) {
            player.sendMessage("You successfully left the guild."); // TODO: Move to config
            return;
        };

        player.sendMessage("We weren't able to remove you from the guild."); // TODO: Move to config
    }

    private CommandCondition hasEquivalentRank(@NotNull SocialRank rank) {
        return (sender, str) -> {
            SocialRank socialRank = SOCIALS_MANAGER.getRank(sender.asPlayer());
            if (socialRank == null) {
                return false;
            }
            return socialRank.permissionLevel() >= rank.permissionLevel();
        };
    }

    private boolean playerHasGuild(CommandSender sender, String str) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        return SOCIALS_MANAGER.getGuildFromPlayer(player) != null;
    }

    private boolean playerHasNoGuild(CommandSender sender, String str) {
        if (sender instanceof Player) {
            return !playerHasGuild(sender, str);
        }
        return false;
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
    private void usageCreate(CommandSender sender, CommandContext context) {
        Player player = sender.asPlayer();
        String guildName = context.get("name");

        Guild guild = SOCIALS_MANAGER.createGuild(guildName, player);

        for (Component component : guild.getInfo().clean()) {
            player.sendMessage(component);
        }

        player.refreshCommands();
    }

}

package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.commands.NarsionCommand;
import org.narses.narsion.player.NarsionPlayer;
import org.narses.narsion.social.Guild;
import org.narses.narsion.social.SocialRank;
import org.narses.narsion.social.SocialsManager;

import java.util.function.Predicate;

class GuildRequiredCommand extends NarsionCommand<NarsionServer> {

    private @Nullable Predicate<SocialRank> rankPredicate;
    protected final SocialsManager manager;

    public GuildRequiredCommand(@NotNull NarsionServer server, @NotNull String name, @NotNull String... alias) {
        super(server, name, alias);
        this.manager = server.getSocialsManager();
        this.setCondition(this::hasGuildCondition);
    }

    protected void setRankPredicate(@Nullable Predicate<SocialRank> predicate) {
        this.rankPredicate = predicate;
    }

    private boolean hasGuildCondition(CommandSender sender, String commandString) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        NarsionPlayer narsionPlayer = server.wrap(player);
        Guild guild = narsionPlayer.getGuild();

        if (guild == null) {
            return false;
        }

        SocialRank playerRank = narsionPlayer.getRank();

        if (playerRank == null) {
            throw new IllegalStateException("Player rank is null, it should not be as the player is in a guild");
        }

        if (rankPredicate == null) {
            return true;
        }

        return rankPredicate.test(playerRank);
    }

    public @NotNull Guild getGuild(@NotNull CommandSender sender) {
        if (!(sender instanceof Player player)) {
            throw new IllegalArgumentException("Sender must be a player");
        }
        NarsionPlayer narsionPlayer = server.wrap(player);
        Guild guild = narsionPlayer.getGuild();
        assert guild != null;
        return guild;
    }
}

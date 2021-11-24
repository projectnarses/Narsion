package org.narses.narsion.commands.guild;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.commands.NarsionCommand;
import org.narses.narsion.player.NarsionPlayer;

class GuildlessRequiredCommand extends NarsionCommand<NarsionServer> {

    public GuildlessRequiredCommand(@NotNull NarsionServer server, @NotNull String name, @NotNull String... alias) {
        super(server, name, alias);
        this.setCondition(this::hasNoGuildCondition);
    }

    private boolean hasNoGuildCondition(CommandSender sender, String commandString) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        NarsionPlayer narsionPlayer = server.wrap(player);
        return narsionPlayer.getGuild() == null;
    }
}

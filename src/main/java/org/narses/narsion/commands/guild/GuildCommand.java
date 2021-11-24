package org.narses.narsion.commands.guild;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.SocialsManager;

// TODO: Colorize the guild commands

// TODO: Finish the below sub-commands
/*

Guild command structure

// Global guild commands
guild {
    history - done
    lookup {
        creation <time>
        name <name>
        uuid <uuid>
        owner <player>
        members <player>
        online <guild>
        offline <guild>
    }
}

// When in a guild:
guild {
    // Global rank commands
    chat [message|boolean] - message done, boolean in progress
    leave - done

    // Claimable rank commands
    claim (pos)
    unclaim (pos)

    // Moderating rank commands
    kick <player> - done
    warn <player> - done
    invite <player> - done
    uninvite <player> - done

    // Administrative rank commands
    promote <player>
    demote <player>
    ban <player>
    unban <player>
    banip <player>
    unbanip <player>

    // Warmongering rank commands
    war {
        declare [socialgroup]
        surrender [socialgroup]
        info [socialgroup]
    }
}

// When not in a guild:
guild {
    create <name> - done
    join <guild> - done
}
 */

public class GuildCommand extends Command {

    private final @NotNull NarsionServer server;
    private final @NotNull SocialsManager SOCIALS_MANAGER;

    public GuildCommand(@NotNull NarsionServer server) {
        super("guild", "g", "gld");

        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();


        // Global guild commands
        this.addSubcommand(new GuildHistoryCommand(server));

        // When in a guild:

        // Global rank commands
        this.addSubcommand(new GuildChatCommand(server));
        this.addSubcommand(new GuildLeaveCommand(server));

        // TODO: Claimable rank commands

        // Moderating rank commands
        this.addSubcommand(new GuildKickCommand(server));
        this.addSubcommand(new GuildWarnCommand(server));
        this.addSubcommand(new GuildInviteCommand(server));
        this.addSubcommand(new GuildUninviteCommand(server));

        // TODO: Administrative rank commands

        // When not in a guild:
        this.addSubcommand(new GuildCreateCommand(server));
        this.addSubcommand(new GuildJoinCommand(server));
        // TODO: Lookup command
    }
}

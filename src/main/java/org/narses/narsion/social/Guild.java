package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

import java.util.*;
import java.util.function.Predicate;

public class Guild implements SocialGroup<NarsionPlayer, Guild.GuildInfo>, SocialMember {

    private final NarsionServer server;
    private final SocialsManager SOCIALS_MANAGER;

    // Guild info
    private @NotNull String name;
    private final long creationTime;
    private final @NotNull UUID uuid;

    // Guild members
    private final @NotNull UUID leader;
    private final @NotNull Set<UUID> members = new HashSet<>();
    private final @NotNull Set<UUID> invites = new HashSet<>();

    Guild(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull UUID leader) {
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.server = server;

        this.creationTime = System.currentTimeMillis();
        this.name = name;
        this.uuid = uuid;
        this.leader = leader;
    }

    @Override
    public @NotNull Collection<@NotNull UUID> getMembers(@NotNull Predicate<UUID> filter) {
        return members.stream().filter(filter).toList();
    }

    @Override
    public @NotNull Guild.GuildInfo getInfo() {
        Map<UUID, SocialRank> memberRanksByUuid = new HashMap<>(members.size());
        for (UUID member : members) {
            memberRanksByUuid.put(member, SOCIALS_MANAGER.getRank(member));
        }

        // TODO: Fetch actual leader history
        List<UUID> leaderList = List.of(leader);
        return new GuildInfo(
                name,
                creationTime,
                leaderList,
                memberRanksByUuid
        );
    }

    @Override
    public boolean add(@NotNull UUID member) {
        return members.add(member);
    }

    @Override
    public boolean remove(@NotNull UUID member) {
        return members.remove(member);
    }

    @Override
    public boolean contains(UUID member) {
        return members.contains(member);
    }

    @Override
    public void onChat(@NotNull GroupChatMessage<NarsionPlayer> chat) {
        var narsionPlayer = chat.member();
        Player sender = narsionPlayer.getPlayer();
        SocialRank rank = narsionPlayer.getRank();

        for (UUID member : members) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(member);

            if (player == null) {
                continue;
            }

            // TODO: Move this format to config using minimessage
            Component message = Component.text()
                    .append(
                            Component.text("[Guild] "),
                            sender.getName().color(NamedTextColor.GOLD),
                            Component.text(" ["),
                            SocialRank.getDisplayName(rank),
                            Component.text("] "),
                            Component.text(" -> "),
                            Component.text(chat.message())
                    ).build();
            player.sendMessage(message);
        }
    }

    @Override
    public void onJoin(@NotNull UUID member) {
        Player joiningPlayer = MinecraftServer.getConnectionManager().getPlayer(member);

        // TODO: Handle offline player names & remove this
        if (joiningPlayer == null) {
            return;
        }

        // Notify all guild members
        for (UUID guildMember : members) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(guildMember);
            if (player == null) {
                continue;
            }
            player.sendMessage("New Player: " + joiningPlayer.getUsername() + " joined the guild!");
        }

        joiningPlayer.refreshCommands();
    }

    @Override
    public void onLeave(@NotNull UUID member) {
        Player leavingPlayer = MinecraftServer.getConnectionManager().getPlayer(member);

        // TODO: Handle offline player names & remove this
        if (leavingPlayer == null) {
            return;
        }

        // Notify all guild members
        for (UUID guildMember : members) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(guildMember);
            if (player == null) {
                continue;
            }
            player.sendMessage("Player: " + leavingPlayer.getUsername() + " left the guild :(");
        }
    }

    @Override
    public @NotNull Set<UUID> getInvites() {
        return invites;
    }

    @Override
    public @Nullable SocialRank getRank() {
        return SOCIALS_MANAGER.getRank(uuid);
    }

    @Override
    public @NotNull UUID getUuid() {
        return uuid;
    }

    public @NotNull String getName() {
        return name;
    }

    public record GuildInfo(
            @NotNull String name,
            long creationTime,
            @NotNull List<UUID> leaderHistory,
            @NotNull Map<UUID, SocialRank> members
    ) {
        public @NotNull Component[] clean() {
            return new Component[] {
                    Component.text(name).color(NamedTextColor.AQUA),
                    Component.text("    Creation Time: ").color(NamedTextColor.AQUA),
                    Component.text(new Date(creationTime).toString()).color(NamedTextColor.YELLOW),
                    Component.text("    Leader history: ").color(NamedTextColor.AQUA),
                    Component.text(leaderHistory.toString()).color(NamedTextColor.YELLOW),
                    Component.text("    Members: ").color(NamedTextColor.AQUA),
                    Component.text(members.toString()).color(NamedTextColor.YELLOW)
            };
        }
    }
}

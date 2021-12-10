package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

import java.net.SocketAddress;
import java.util.*;
import java.util.function.Predicate;

public class Guild implements SocialGroup, SocialMember {

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
    private final @NotNull Set<UUID> bannedMembers = new HashSet<>();
    private final @NotNull Set<SocketAddress> bannedIps = new HashSet<>();

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
    public void onChat(@NotNull SocialGroup.Chat chat) {
        UUID player = chat.member();

        Player sender = MinecraftServer.getConnectionManager().getPlayer(player);

        // TODO: Handle if player is offline
        if (sender == null) {
            throw new IllegalStateException("Player is offline");
        }

        NarsionPlayer narsionPlayer = server.wrap(sender);
        SocialRank rank = narsionPlayer.getRank();

        // TODO: Move this format to config using minimessage
        Component message = Component.text()
                .append(
                        Component.text("[Guild] "),
                        sender.getName().color(NamedTextColor.GOLD),
                        Component.text(" ["),
                        SocialRank.getDisplayNameOfNullable(rank),
                        Component.text("] "),
                        Component.text(" -> "),
                        Component.text(chat.message())
                ).build();

        for (UUID member : members) {
            Player listener = MinecraftServer.getConnectionManager().getPlayer(member);

            if (listener == null) {
                continue;
            }

            listener.sendMessage(message);
        }
    }

    @Override
    public void onJoin(@NotNull Join join) {
        Player joiningPlayer = MinecraftServer.getConnectionManager().getPlayer(join.member());

        // TODO: Handle offline player names & remove this
        if (joiningPlayer == null) {
            throw new IllegalStateException("Player is offline");
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
    public void onLeave(@NotNull Leave leave) {
        Player leavingPlayer = MinecraftServer.getConnectionManager().getPlayer(leave.member());

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
    public void onInvite(@NotNull Invite invite) {
        Player invitingPlayer = MinecraftServer.getConnectionManager().getPlayer(invite.member());

        if (invitingPlayer == null) {
            return;
        }

        // Notify all guild members
        for (UUID guildMember : members) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(guildMember);

            if (player == null) {
                continue;
            }

            player.sendMessage("Player: " + invitingPlayer.getUsername() + " was invited to the guild!");
        }
    }

    @Override
    public void onUninvite(@NotNull Uninvite uninvite) {

    }

    @Override
    public void onPromote(@NotNull Promote promote) {

    }

    @Override
    public void onDemote(@NotNull Demote demote) {

    }

    @Override
    public boolean onBan(@NotNull Ban ban) {
        return false;
    }

    @Override
    public boolean onUnban(@NotNull Unban unban) {
        return false;
    }

    @Override
    public boolean onBanip(@NotNull Banip banip) {
        return false;
    }

    @Override
    public boolean onUnbanip(Unbanip unbanip) {
        return false;
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

    @Override
    public boolean addBan(UUID member) {
        return bannedMembers.add(member);
    }

    @Override
    public boolean removeBan(UUID member) {
        return bannedMembers.remove(member);
    }

    @Override
    public boolean addBanip(SocketAddress address) {
        return bannedIps.add(address);
    }

    @Override
    public boolean removeBanip(SocketAddress address) {
        return bannedIps.remove(address);
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

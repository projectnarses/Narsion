package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.player.NarsionPlayer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Guild implements SocialGroup<NarsionPlayer, Guild.GuildInfo>, SocialMember {

    private final NarsionServer server;
    private final SocialsManager SOCIALS_MANAGER;

    // Guild info
    private @NotNull String name;
    private final long creationTime;
    private final @NotNull UUID uuid;

    // Guild members
    private final @NotNull Set<UUID> members = new HashSet<>();
    private final @NotNull Set<NarsionPlayer> onlineMembers = new HashSet<>();
    private final @NotNull UUID leader;

    Guild(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull UUID leader, UUID... players) {
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.server = server;

        this.creationTime = System.currentTimeMillis();
        this.name = name;
        this.uuid = uuid;
        this.leader = leader;
        this.members.add(leader);
        members.addAll(Arrays.asList(players));
    }

    @Override
    public @NotNull Collection<@NotNull NarsionPlayer> getOnlineMembers(@NotNull Predicate<NarsionPlayer> filter) {
        return onlineMembers.stream().filter(filter).toList();
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
    public boolean add(@NotNull NarsionPlayer member) {
        boolean added = members.add(member.getUuid());
        onlineMembers.add(member);

        return added;
    }

    @Override
    public boolean remove(@NotNull NarsionPlayer member) {
        boolean removed = members.remove(member.getUuid());
        onlineMembers.remove(member);

        return removed;
    }

    @Override
    public @Nullable SocialRank getRank() {
        return SOCIALS_MANAGER.getRank(uuid);
    }

    @Override
    public @NotNull UUID getUuid() {
        return uuid;
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

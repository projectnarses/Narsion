package org.narses.narsion.social;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.NarsionServer;

import java.util.*;

public class Guild implements SocialGroup<Player, Guild.GuildInfo> {

    private final NarsionServer server;
    private final SocialsManager SOCIALS_MANAGER;
    private @NotNull String name;
    private final long creationTime;
    private final @NotNull Map<UUID, SocialRank> players = new HashMap<>();
    private final @NotNull UUID uuid;
    private final @NotNull UUID leader;

    Guild(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull UUID leader, UUID... players) {
        this.creationTime = System.currentTimeMillis();
        this.name = name;
        this.uuid = uuid;
        this.leader = leader;
        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.players.put(leader, SocialRank.LEADER);

        for (UUID player : players) {
            this.players.put(player, SocialRank.MEMBER);
        }
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    @Override
    public <T> @NotNull Collection<T> getPlayers(@NotNull PlayerFilter<T> filter) {
        return filter.uuidsFunction().apply(players.keySet());
    }

    @Override
    public @NotNull Guild.GuildInfo getInfo() {
        return new GuildInfo(name, creationTime, List.of(leader), players);
    }

    @Override
    public @Nullable SocialRank getRank(@NotNull UUID player) {
        return this.players.get(player);
    }

    @ApiStatus.Internal
    @Override
    public boolean add(@NotNull UUID player) {
        if (this.players.containsKey(player)) {
            return false;
        }
        this.players.put(player, SocialRank.MEMBER);
        return true;
    }

    @ApiStatus.Internal
    @Override
    public boolean remove(@NotNull UUID player) {
        if (this.players.containsKey(player)) {
            this.players.remove(player);
            return true;
        }
        return false;
    }

    @Override
    @ApiStatus.Internal
    public @NotNull UUID uuidOf(@NotNull Player element) {
        return element.getUuid();
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

package org.narses.narsion.social;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.PlayerGroup;

import java.util.*;

public class Guild implements PlayerGroup<Guild.GuildInfo> {

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

    public boolean addPlayer(@NotNull UUID player) {
        if (this.players.containsKey(player)) {
            return false;
        }

        this.players.put(player, SocialRank.MEMBER);

        return true;
    }

    @Override
    public <T> @NotNull Collection<T> getPlayers(@NotNull PlayerFilter<T> filter) {
        return filter.uuidsFunction().apply(players.keySet());
    }

    @Override
    public @NotNull Guild.GuildInfo getInfo() {
        return new GuildInfo(name, creationTime, List.of(leader), players);
    }

    public record GuildInfo(
            @NotNull String name,
            long creationTime,
            @NotNull List<UUID> leaderHistory,
            @NotNull Map<UUID, SocialRank> members
    ) { }
}

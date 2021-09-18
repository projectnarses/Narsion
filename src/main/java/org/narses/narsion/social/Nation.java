package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.social.PlayerGroup;

import java.util.*;

class Nation implements PlayerGroup<Nation.NationInfo> {

    private final NarsionServer server;
    private final SocialsManager SOCIALS_MANAGER;
    private final @NotNull UUID uuid;
    private final @NotNull Map<Guild, SocialRank> guilds = new HashMap<>();
    private final long creationTime;
    private @NotNull Guild leader;
    private @NotNull String name;

    Nation(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull Guild leader) {
        this.leader = leader;
        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.name = name;
        this.uuid = uuid;
        this.creationTime = System.currentTimeMillis();

        guilds.put(leader, SocialRank.LEADER);
    }

    @Override
    public <T> @NotNull Collection<T> getPlayers(@NotNull PlayerFilter<T> filter) {
        List<T> elements = new ArrayList<>();

        for (Guild guild : guilds.keySet()) {
            elements.addAll(guild.getPlayers(filter));
        }

        return elements;
    }

    @Override
    public @NotNull Nation.NationInfo getInfo() {
        return new NationInfo(name, creationTime, List.of(leader), guilds);
    }

    public record NationInfo(
            @NotNull String name,
            long creationTime,
            @NotNull List<Guild> leaderHistory,
            @NotNull Map<Guild, SocialRank> guilds
    ) { }
}

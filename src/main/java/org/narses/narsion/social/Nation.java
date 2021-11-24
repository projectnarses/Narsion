package org.narses.narsion.social;

import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

import java.util.*;
import java.util.function.Predicate;

class Nation implements SocialGroup<Guild, Nation.NationInfo> {

    private final NarsionServer server;
    private final SocialsManager SOCIALS_MANAGER;

    // Nation info
    private final @NotNull UUID uuid;
    private final long creationTime;
    private @NotNull Guild leader;
    private @NotNull String name;

    private final @NotNull Set<Guild> members = new HashSet<>();

    Nation(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull Guild leader) {
        this.leader = leader;
        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.name = name;
        this.uuid = uuid;
        this.creationTime = System.currentTimeMillis();
        members.add(leader);
    }

    @Override
    public @NotNull Collection<@NotNull Guild> getOnlineMembers(@NotNull Predicate<Guild> filter) {
        return members.stream().filter(filter).toList();
    }

    @Override
    public @NotNull Collection<@NotNull UUID> getMembers(@NotNull Predicate<UUID> filter) {
        return members.stream().map(Guild::getUuid).filter(filter).toList();
    }

    @Override
    public @NotNull Nation.NationInfo getInfo() {
        Map<Guild, SocialRank> guildRanksByGuild = new HashMap<>(members.size());
        for (Guild guild : members) {
            guildRanksByGuild.put(guild, guild.getRank());
        }

        // TODO: Fetch actual leader history
        List<Guild> leaderList = List.of(leader);
        return new NationInfo(
                name,
                creationTime,
                leaderList,
                guildRanksByGuild
        );
    }

    @Override
    public boolean add(@NotNull UUID member) {
        return false;
    }

    @Override
    public boolean remove(@NotNull UUID member) {
        return false;
    }


    public record NationInfo(
            @NotNull String name,
            long creationTime,
            @NotNull List<Guild> leaderHistory,
            @NotNull Map<Guild, SocialRank> guilds
    ) { }
}

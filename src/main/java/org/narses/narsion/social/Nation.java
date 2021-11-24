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
    private @NotNull UUID leader;
    private @NotNull String name;

    private final @NotNull Set<UUID> members = new HashSet<>();
    private final @NotNull Set<UUID> invites = new HashSet<>();

    Nation(@NotNull NarsionServer server, @NotNull String name, @NotNull UUID uuid, @NotNull UUID leader) {
        this.leader = leader;
        this.server = server;
        this.SOCIALS_MANAGER = server.getSocialsManager();
        this.name = name;
        this.uuid = uuid;
        this.creationTime = System.currentTimeMillis();
    }

    @Override
    public @NotNull Collection<@NotNull UUID> getMembers(@NotNull Predicate<UUID> filter) {
        return members.stream().filter(filter).toList();
    }

    @Override
    public @NotNull Nation.NationInfo getInfo() {
        Map<Guild, SocialRank> guildRanksByGuild = new HashMap<>(members.size());
        for (UUID guild : members) {
            guildRanksByGuild.put(SOCIALS_MANAGER.getGuildFromUuid(guild), SOCIALS_MANAGER.getRank(guild));
        }

        // TODO: Fetch actual leader history
        List<Guild> leaderList = List.of(SOCIALS_MANAGER.getGuildFromUuid(leader));
        return new NationInfo(
                name,
                creationTime,
                leaderList,
                guildRanksByGuild
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
    public void onChat(@NotNull GroupChatMessage<Guild> chat) {

    }

    @Override
    public void onJoin(@NotNull UUID member) {

    }

    @Override
    public void onLeave(@NotNull UUID member) {

    }

    @Override
    public @NotNull Set<UUID> getInvites() {
        return invites;
    }

    public record NationInfo(
            @NotNull String name,
            long creationTime,
            @NotNull List<Guild> leaderHistory,
            @NotNull Map<Guild, SocialRank> guilds
    ) { }
}

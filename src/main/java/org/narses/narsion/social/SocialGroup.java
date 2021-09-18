package org.narses.narsion.social;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * An interface that represents a social group.
 * @param <E> the elements that this group contains
 * @param <I> the info object of this group
 */
public interface SocialGroup<E, I> {

    public @NotNull <T> Collection<T> getPlayers(@NotNull PlayerFilter<T> filter);
    public @NotNull I getInfo();
    public @Nullable SocialRank getRank(@NotNull UUID elementUuid);
    public boolean add(@NotNull UUID element);
    public boolean remove(@NotNull UUID element);
    public @NotNull UUID uuidOf(@NotNull E element);

    default @Nullable SocialRank getRank(@NotNull E element) {
        return this.getRank(uuidOf(element));
    };

    default boolean add(@NotNull E element) {
        return this.add(uuidOf(element));
    };

    default boolean remove(@NotNull E element) {
        return this.remove(uuidOf(element));
    };

    public record PlayerFilter<T>(@NotNull Function<Collection<UUID>, Collection<T>> uuidsFunction) {

        public static final PlayerFilter<Player> ONLINE = new PlayerFilter<>(
                uuids -> {
                    List<Player> returnValue = new ArrayList<>();

                    for (UUID uuid : uuids) {
                        Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);

                        if (player == null) {
                            continue;
                        }

                        returnValue.add(player);
                    }

                    return returnValue;
                }
        );

        public static final PlayerFilter<UUID> OFFLINE = new PlayerFilter<>(
                uuids -> {
                    List<UUID> returnValue = new ArrayList<>();

                    for (UUID uuid : uuids) {
                        Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);

                        if (player == null) {
                            continue;
                        }

                        returnValue.add(uuid);
                    }

                    return returnValue;
                }
        );
        public static final PlayerFilter<UUID> ALL = new PlayerFilter<>((uuids) -> uuids);
    }
}

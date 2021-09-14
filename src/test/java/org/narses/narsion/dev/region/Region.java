package org.narses.narsion.dev.region;

import java.util.*;

import net.minestom.server.Viewable;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.math.geometry.Area3dPolygon;

public interface Region extends Viewable {
	public static Region of(
			@NotNull final String name,
			final boolean claimable,
			@NotNull final RegionType type,
			@NotNull final Area3dPolygon polygon
	) {
		return new Region() {

			private final Set<Player> viewers = new HashSet<>();

			@Override
			public @NotNull String getName() {
				return name;
			}

			@Override
			public boolean getClaimable() {
				return claimable;
			}

			@Override
			public @NotNull RegionType getType() {
				return type;
			}

			@Override
			public @NotNull Area3dPolygon getPolygon() {
				return polygon;
			}

			@Override
			public boolean addViewer(@NotNull Player player) {
				return viewers.add(player);
			}

			@Override
			public boolean removeViewer(@NotNull Player player) {
				return viewers.remove(player);
			}

			@Override
			public @NotNull Set<Player> getViewers() {
				return viewers;
			}
		};
	}

	public @NotNull String getName();

	public boolean getClaimable();

	public @NotNull RegionType getType();

	public @NotNull Area3dPolygon getPolygon();

	public default void onPlayerEnter(@NotNull Player player) {
	}

	public default void onPlayerUpdate(long ms, @NotNull Player player) {
	}

	public default void onPlayerExit(@NotNull Player player) {
	}
}
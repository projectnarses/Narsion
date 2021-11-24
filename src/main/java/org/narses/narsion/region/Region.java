package org.narses.narsion.region;

import dev.emortal.rayfast.area.area3d.Area3d;
import net.minestom.server.Viewable;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

import java.util.HashSet;
import java.util.Set;

public interface Region extends Viewable {
	public static Region of(
			@NotNull final String name,
			final boolean claimable,
			@NotNull final RegionType type,
			@NotNull final Area3d polygon
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
			public @NotNull Area3d getArea() {
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

	public @NotNull Area3d getArea();

	public default void onPlayerEnter(@NotNull NarsionServer server, @NotNull Player player) {
	}

	public default void onPlayerExit(@NotNull NarsionServer server, @NotNull Player player) {
	}
}
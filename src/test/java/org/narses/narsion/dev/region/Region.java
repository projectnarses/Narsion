package org.narses.narsion.dev.region;

import java.time.Duration;
import java.util.*;

import net.minestom.server.Viewable;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.math.geometry.GeoPolygon;

public class Region implements Viewable {
	// Region fields
	private final @NotNull String name;
	private final @NotNull GeoPolygon polygon;
	private final @NotNull RegionType type;

	private final @NotNull Set<Player> viewers = new HashSet<>();
	
	public Region(@NotNull String name, @NotNull RegionType type, @NotNull GeoPolygon polygon) {
		this.polygon = polygon;
		this.name = name;
		this.type = type;
	}

	public @NotNull String getName() {
		return name;
	}

	public @NotNull RegionType getType() {
		return type;
	}
	
	/**
	 * Shows this region to the specified player
	 * 
	 * @param player the player to show the region to
	 */
	@Override
	public boolean addViewer(@NotNull Player player) {
		return viewers.add(player);
	}
	
	/**
	 * Hides this region from the specified player
	 * 
	 * @param player the player to stop showing this region to
	 */
	@Override
	public boolean removeViewer(@NotNull Player player) {
		return viewers.remove(player);
	}
	
	@Override
	public @NotNull Set<Player> getViewers() {
		return viewers;
	}

	public @NotNull GeoPolygon getPolygon() {
		return polygon;
	}
}
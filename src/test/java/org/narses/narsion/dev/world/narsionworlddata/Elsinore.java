package org.narses.narsion.dev.world.narsionworlddata;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.math.geometry.Area3dPolygon;
import org.narses.narsion.dev.player.DevPlayer;
import org.narses.narsion.region.RegionType;
import org.narses.narsion.region.StaticRegion;
import org.narses.narsion.player.NarsionPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * Elsinore is the starter town
 * 
 * @author Krystilize
 *
 */
public enum Elsinore implements StaticRegion {
	INSTANCE;

	private final Area3dPolygon area = Area3dPolygon.of(
			new Pos(3056, 255, -26), new Pos(3056, 0, -26),
			new Pos(2968, 255, 52), new Pos(2968, 0, 52),
			new Pos(2861, 255, 48), new Pos(2861, 0, 48),
			new Pos(2767, 255, 62), new Pos(2767, 0, 62),
			new Pos(2749, 255, 32), new Pos(2749, 0, 32),
			new Pos(2727, 255, -17), new Pos(2727, 0, -17),
			new Pos(2747, 255, -71), new Pos(2747, 0, -71),
			new Pos(2750, 255, -90), new Pos(2750, 0, -90),
			new Pos(2732, 255, -108), new Pos(2732, 0, -108),
			new Pos(2722, 255, -170), new Pos(2722, 0, -170),
			new Pos(2793, 255, -258), new Pos(2793, 0, -258),
			new Pos(2884, 255, -266), new Pos(2884, 0, -266),
			new Pos(3058, 255, -173), new Pos(3058, 0, -173)
	);

	private final Set<Player> viewers = new HashSet<>();

	@Override
	public void onPlayerEnter(@NotNull NarsionServer server, @NotNull Player player) {
		player.sendMessage("You have entered Elsinore, a small market town which marks the beginning of your journey.");
		DevPlayer devPlayer = server.wrap(player);
		Boolean visitedElsinore = devPlayer.getTag(NarsionPlayer.TAG_VISITED_ELSINORE);

		if (visitedElsinore != null && visitedElsinore) {
			return;
		}

		// Has not visited elsinore
		player.showTitle(
				Title.title(
						Component.text("WELCOME TO ELSINORE"),
						Component.text("May Narsion's energy resonate within you.")
				)
		);

		devPlayer.setTag(NarsionPlayer.TAG_VISITED_ELSINORE, true);
	}

	@Override
	public @NotNull String getName() {
		return "Elsinore";
	}

	@Override
	public boolean getClaimable() {
		return false;
	}

	@Override
	public @NotNull RegionType getType() {
		return RegionType.TOWN;
	}

	@Override
	public @NotNull Area3dPolygon getArea() {
		return area;
	}

	@Override
	public @NotNull Set<Player> getViewers() {
		return viewers;
	}
}

package org.narses.narsion.dev.world.narsionworlddata;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.dev.math.geometry.Area3dPolygon;
import org.narses.narsion.dev.region.RegionType;
import org.narses.narsion.dev.region.StaticRegion;

import java.util.HashSet;
import java.util.Set;

/**
 * Elsinore Markets are designed to be the highest value territory in the game as they are situated directly next to the
 * spawn area.
 * 
 * @author Krystilize
 *
 */
public class ElsinoreMarkets {
	public enum North implements StaticRegion {
		INSTANCE;

		private final Area3dPolygon area = Area3dPolygon.of(
				new Pos(2909, 256, -86), new Pos(2900, 0, -80),
				new Pos(2900, 256, -80), new Pos(2869, 0, -80),
				new Pos(2869, 256, -80), new Pos(2844, 0, -79),
				new Pos(2844, 256, -79), new Pos(2820, 0, -80),
				new Pos(2820, 256, -80), new Pos(2786, 0, -82),
				new Pos(2786, 256, -82), new Pos(2752, 0, -116),
				new Pos(2752, 256, -116), new Pos(2760, 0, -156),
				new Pos(2760, 256, -156), new Pos(2787, 0, -171),
				new Pos(2787, 256, -171), new Pos(2785, 0, -191),
				new Pos(2785, 256, -191), new Pos(2795, 0, -225),
				new Pos(2795, 256, -225), new Pos(2809, 0, -226),
				new Pos(2809, 256, -226), new Pos(2812, 0, -215),
				new Pos(2812, 256, -215), new Pos(2818, 0, -210),
				new Pos(2818, 256, -210), new Pos(2828, 0, -209),
				new Pos(2828, 256, -209), new Pos(2840, 0, -195),
				new Pos(2840, 256, -195), new Pos(2840, 0, -170),
				new Pos(2840, 256, -170), new Pos(2868, 0, -141),
				new Pos(2868, 256, -141), new Pos(2882, 0, -141),
				new Pos(2882, 256, -141), new Pos(2900, 0, -121),
				new Pos(2900, 256, -121), new Pos(2906, 0, -121),
				new Pos(2906, 256, -121), new Pos(2904, 0, -101),
				new Pos(2904, 256, -101), new Pos(2917, 0, -92),
				new Pos(2917, 256, -92), new Pos(2909, 0, -86)
		);

		private final Set<Player> viewers = new HashSet<>();

		@Override
		public @NotNull String getName() {
			return "Elsinore Northern Market";
		}

		@Override
		public boolean getClaimable() {
			return true;
		}

		@Override
		public @NotNull RegionType getType() {
			return RegionType.MARKET;
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

	public enum South implements StaticRegion {
		INSTANCE;

		private final Area3dPolygon area = Area3dPolygon.of(
				new Pos(2910, 0, -77), new Pos(2910, 256, -77),
				new Pos(2899, 0, -72), new Pos(2899, 256, -72),
				new Pos(2854, 0, -70), new Pos(2854, 256, -70),
				new Pos(2792, 0, -75), new Pos(2792, 256, -75),
				new Pos(2787, 0, -68), new Pos(2787, 256, -68),
				new Pos(2786, 0, -52), new Pos(2786, 256, -52),
				new Pos(2779, 0, -46), new Pos(2779, 256, -46),
				new Pos(2762, 0, -37), new Pos(2762, 256, -37),
				new Pos(2750, 0, -30), new Pos(2750, 256, -30),
				new Pos(2751, 0, -7), new Pos(2751, 256, -7),
				new Pos(2770, 0, 14), new Pos(2770, 256, 14),
				new Pos(2770, 0, 31), new Pos(2770, 256, 31),
				new Pos(2780, 0, 40), new Pos(2780, 256, 40),
				new Pos(2800, 0, 43), new Pos(2800, 256, 43),
				new Pos(2837, 0, 4), new Pos(2837, 256, 4),
				new Pos(2876, 0, 2), new Pos(2876, 256, 2),
				new Pos(2886, 0, -5), new Pos(2886, 256, -5),
				new Pos(2902, 0, -27), new Pos(2902, 256, -27),
				new Pos(2912, 0, -53), new Pos(2912, 256, -53),
				new Pos(2915, 0, -73), new Pos(2915, 256, -73)
		);

		private final Set<Player> viewers = new HashSet<>();

		@Override
		public @NotNull String getName() {
			return "Elsinore Southern Market";
		}

		@Override
		public boolean getClaimable() {
			return true;
		}

		@Override
		public @NotNull RegionType getType() {
			return RegionType.MARKET;
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
}

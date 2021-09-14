package org.narses.narsion.dev.world.narsionworlddata;

import net.minestom.server.coordinate.Pos;
import org.narses.narsion.dev.math.geometry.Area3dPolygon;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.region.RegionType;

import java.util.function.Supplier;

/**
 * Elsinore is the starter town
 * 
 * @author Krystilize
 *
 */
public enum Elsinore implements Supplier<Region> {
	INSTANCE;

	@Override
	public Region get() {
		return Region.of(
				"Elsinore",
				false,
				RegionType.TOWN,
				Area3dPolygon.of(
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
				)
		);
	}
}

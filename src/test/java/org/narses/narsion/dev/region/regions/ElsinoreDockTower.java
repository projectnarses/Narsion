package org.narses.narsion.dev.region.regions;

import net.minestom.server.coordinate.Pos;
import org.narses.narsion.dev.math.geometry.GeoPolygon;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.region.RegionType;

/**
 * Elsinore is the starter town
 * 
 * @author Krystilize
 *
 */
public class ElsinoreDockTower extends Region {
	
	private static final GeoPolygon polygon = GeoPolygon.of(
		new Pos(2930, 91, -100), new Pos(2930, 111, -100),
		new Pos(2931, 90, -113), new Pos(2931, 110, -113),
		new Pos(2920, 90, -113), new Pos(2920, 110, -113),
		new Pos(2912, 78, -119), new Pos(2912, 98, -119),
		new Pos(2907, 78, -117), new Pos(2907, 98, -117),
		new Pos(2906, 78, -108), new Pos(2906, 98, -108),
		new Pos(2916, 91, -102), new Pos(2916, 111, -102)
	);
	
	public ElsinoreDockTower() {
		super("Elsinore Dock Tower", RegionType.TOWER, polygon);
	}
	
}

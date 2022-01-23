package server.main.validators;

import server.main.enumeration.Terrain;
import server.main.exceptions.MapSizeException;
import server.main.model.Coordinate;
import server.main.model.Map;

/**
 * SizeValidator
 * Checks if the map has the correct number of better 32. 
 * Then checks if there is a minimum number of water and mountain fields.
 * 
 * Tasks: 
 * 1. Check the map size
 * 2. Check the minimum of water(4) and mountain(3) fields.
 * 
 * Using Map to process task
 * @author Veljko Radunovic 01528243
 */

public class SizeValidator implements IMapValidator {

	private final static int minWater = 4;
	private final static int minMountains = 3;
	private final static int minGrass = 15;

	@Override
	public void validateMap(Map map) {
		int grassCnt = 0;
		int waterCnt = 0;
		int mountainCnt = 0;
		if (map.getNodes().size() != 32)
			throw new MapSizeException("Map Size Exception", "map size is not valid");

		for (Coordinate c : map.getNodes().keySet()) {
			if (map.getNodes().get(c).getFieldType().equals(Terrain.GRASS))
				++grassCnt;
			if (map.getNodes().get(c).getFieldType().equals(Terrain.WATER))
				++waterCnt;
			if (map.getNodes().get(c).getFieldType().equals(Terrain.MOUNTAIN))
				++mountainCnt;
		}

		if (grassCnt < minGrass || waterCnt < minWater || mountainCnt < minMountains)
			throw new MapSizeException("Map Size Exception", "map amount of requested fields is not valid");

	}

}

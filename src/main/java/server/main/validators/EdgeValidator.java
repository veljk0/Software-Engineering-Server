package server.main.validators;

import server.main.enumeration.Terrain;
import server.main.exceptions.MapEdgeException;
import server.main.model.Coordinate;
import server.main.model.Map;

/**
 * EdgeValidator
 * Use to check that there are not too many water fields at the edges of the map. 
 * To make it possible in the game to move from one half of the map to the other.
 * 
 * Tasks: 
 * 1. Check the water fields on the edges of the map

 * 
 * Using Map to process task
 * @author Veljko Radunovic 01528243
 */
public class EdgeValidator implements IMapValidator {

	private final static int maxVertical = 1;
	private final static int maxHorizontal = 3;

	@Override
	public void validateMap(Map map) {
		int upperHorizontalWaterCounter = 0;
		int lowerHorizontalWaterCounter = 0;
		int leftVerticalWaterCounter = 0;
		int rightVerticalWaterCounter = 0;

		for (Coordinate c : map.getNodes().keySet()) {
			if (map.getNodes().get(c).getCoordinate().getY() == 0
					&& map.getNodes().get(c).getFieldType().equals(Terrain.WATER))
				++upperHorizontalWaterCounter;
			if (map.getNodes().get(c).getCoordinate().getY() == 3
					&& map.getNodes().get(c).getFieldType().equals(Terrain.WATER))
				++lowerHorizontalWaterCounter;
			if (map.getNodes().get(c).getCoordinate().getX() == 0
					&& map.getNodes().get(c).getFieldType().equals(Terrain.WATER))
				++leftVerticalWaterCounter;
			if (map.getNodes().get(c).getCoordinate().getX() == 7
					&& map.getNodes().get(c).getFieldType().equals(Terrain.WATER))
				++rightVerticalWaterCounter;
		}

		if (upperHorizontalWaterCounter > maxHorizontal || lowerHorizontalWaterCounter > maxHorizontal
				|| leftVerticalWaterCounter > maxVertical || rightVerticalWaterCounter > maxVertical)
			throw new MapEdgeException("Map edge exception", "edges are not valid");

	}

}

package server.main.validators;

import server.main.enumeration.FortState;
import server.main.enumeration.Terrain;
import server.main.exceptions.MapCastleException;
import server.main.model.Coordinate;
import server.main.model.Map;

/**
 * CastleValidator
 * Used to check if there is a castle placed on the grass field on the generated map
 * 
 * Tasks: 
 * 1. Check if the castle is on the map

 * 
 * Using Map to process task
 * @author Veljko Radunovic 01528243
 */
public class CastleValidator implements IMapValidator {

	@Override
	public void validateMap(Map map) {
		int castleCounter = 0;

		for (Coordinate c : map.getNodes().keySet()) {
			if (map.getNodes().get(c).getFieldType().equals(Terrain.GRASS)
					&& (map.getNodes().get(c).getFortState().equals(FortState.PlayerOneFortPresent) || map.getNodes().get(c).getFortState().equals(FortState.PlayerTwoFortPresent)))
				++castleCounter;
		}

		if (castleCounter != 1)
			throw new MapCastleException("Map Castle Exception", "Castle not valid");

	}

}

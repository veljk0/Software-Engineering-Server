package server.main.validators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import server.main.enumeration.Terrain;
import server.main.exceptions.MapIslandException;
import server.main.model.Coordinate;
import server.main.model.Map;
import server.main.model.MapNode;



/**
 * IslandValidator
 * Use it to check if there are grass fields on the map that cannot be reached.
 * To check if there are islands on the map. 
 * The validator finds the first grass field on the map and check out its neighbors. 
 * If it has grass or mountain neighbours, it writes them in the list and increases the counter for each field in that list. 
 * The first neighbour that is entered in the list is processed in the same way, without entering in the list the neighbour from whom it came. 
 * Finally, the counter is checked, and if it is 28, it means that the map is correct.
 * 
 * Tasks: 
 * 1. Check the islands on the map

 * 
 * Using Map to process task
 * @author Veljko Radunovic 01528243
 */
public class IslandValidator implements IMapValidator {

	private LinkedList<MapNode> askNodes;
	private Set<MapNode> checkedNodes;

	public IslandValidator() {
		askNodes = new LinkedList<>();
		checkedNodes = new HashSet<>();
	}

	@Override
	public void validateMap(Map map) {
		askNodes.clear();
		checkedNodes.clear();

		findFirst(map);
		while (!askNodes.isEmpty()) {
			MapNode tmp = askNodes.poll();
			checkedNodes.add(tmp);
			findNeighbours(tmp, map);

		}
		
		if(checkedNodes.size() != 28) throw new MapIslandException("Map Island Exception", "island is present on the map");

	}
	
	/**
	 * Look for the first grass or mountain field on the map from which the interrogation will begin.
	 * 
	 * @param map
	 * @author Veljko Radunovic 01528243
	 */
	private void findFirst(Map map) {
		for (Coordinate c : map.getNodes().keySet()) {
			if (!map.getNodes().get(c).getFieldType().equals(Terrain.WATER)) {
				this.askNodes.add(map.getNodes().get(c));
				break;
			}
		}

	}

	/**
	 * Look for neighbors from the current field. 
	 * It checks the fields above, below, to its left and to its right side. 
	 * Enters only fields that exits, that are not water and that are not in the list of already visited/checked fields .
	 * @param mapNode
	 * @param map
	 * @author Veljko Radunovic 01528243
	 */
	private void findNeighbours(MapNode mapNode, Map map) {
		Coordinate up = new Coordinate(mapNode.getCoordinate().getX(), mapNode.getCoordinate().getY() - 1);
		Coordinate left = new Coordinate(mapNode.getCoordinate().getX() - 1, mapNode.getCoordinate().getY());
		Coordinate down = new Coordinate(mapNode.getCoordinate().getX(), mapNode.getCoordinate().getY() + 1);
		Coordinate right = new Coordinate(mapNode.getCoordinate().getX() + 1, mapNode.getCoordinate().getY());

		for (Coordinate c : map.getNodes().keySet()) {
			if ((c.equals(up) || c.equals(left) || c.equals(down) || c.equals(right))
					&& !map.getNodes().get(c).getFieldType().equals(Terrain.WATER)
					&& !checkedNodes.contains(map.getNodes().get(c)))
				askNodes.add(map.getNodes().get(c));
		}
	}
}

package server.main.marshaller;

import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.EFortState;
import MessagesGameState.EPlayerPositionState;
import MessagesGameState.ETreasureState;
import MessagesGameState.FullMapNode;
import server.main.enumeration.FortState;
import server.main.enumeration.PlayerNumber;
import server.main.enumeration.PlayerPositionState;
import server.main.enumeration.Terrain;
import server.main.enumeration.TreasureState;
import server.main.exceptions.MarshallerTerrainException;
import server.main.model.Coordinate;
import server.main.model.Map;
import server.main.model.MapNode;


public class Marshaller {
	
	static Logger logger = LoggerFactory.getLogger(Marshaller.class);

	/**
	 * @author Veljko Radunovic 01528243
	 * Marshaller --- Making data readable in both directions for the CLIENT and for the SERVER 
	 * Tasks: 
	 * 1. Convert HalfMap to Server Map 
	 * 	1.1 Converting halfMapCoordinate to server map coordinates
	 * 	1.2 Converting halfMap terrain to server map terrain 
	 * 	1.3 Converting halfMap fortState to server map fortState
	 * 	1.4 Converting halfMap PlayerPosition to server map PlayerPosition
	 * 2. Convert Server Map to FullMap for Client 
	 * 	2.1 Convert Server Map FortState to FullMap EFortState
	 * 	2.2 Convert Server Map TreasureState to FullMap ETreasureState
	 * 	2.3 Convert Server Map PlayerPositionState to FullMap EPlayerPositionState
	 * 	2.4 Convert Server Map Terrain to FullMap ETerrain
	 */

	public Marshaller() {}

	
	/**
	 * @author Veljko Radunovic 01528243
	 * Task 1 Converting halfMap from player to map
	 * @param halfMap
	 * @param playerNumber
	 * @return HashMap<Coordinate, MapNode>
	 */
	public static HashMap<Coordinate, MapNode> convertMapToServer(HalfMap halfMap, PlayerNumber playerNumber) {

		Collection<HalfMapNode> halfMapNodes = halfMap.getMapNodes();
		HashMap<Coordinate, MapNode> myMapNodes = new HashMap<Coordinate, MapNode>();

		for (HalfMapNode halfMapNode : halfMapNodes) {
			MapNode myMapNode = new MapNode();
			myMapNode.setCoordinate(convertCoordinatesToClient(halfMapNode));
			myMapNode.setFieldType(convertTerrainToClient(halfMapNode));
			myMapNode.setFortState(convertFortStateToClient(halfMapNode, playerNumber));
			myMapNode.setPlayerPositionState(convertPlayerPositionStateToClient(myMapNode.getFortState()));
			myMapNodes.put(myMapNode.getCoordinate(), myMapNode);
		}

		List<Coordinate> randomList = new ArrayList<>(myMapNodes.keySet());
		Collections.shuffle(randomList);

		for (Coordinate c : randomList) {
			if (myMapNodes.get(c).getFieldType().equals(Terrain.GRASS)
					&& (!myMapNodes.get(c).getFortState().equals(FortState.PlayerOneFortPresent)
							&& !myMapNodes.get(c).getFortState().equals(FortState.PlayerTwoFortPresent))) {
				if (playerNumber.equals(PlayerNumber.PlayerOne)
						&& myMapNodes.get(c).getPlayerPositionState().equals(PlayerPositionState.Player1)) {
					myMapNodes.get(c).setTreasureState(TreasureState.PlayerOneTreasureIsPresent);
					break;
				}

				else if (playerNumber.equals(PlayerNumber.PlayerTwo)
						&& myMapNodes.get(c).getPlayerPositionState().equals(PlayerPositionState.Player2)) {
					myMapNodes.get(c).setTreasureState(TreasureState.PlayerTwoTreasureIsPresent);
					break;
				}
			}
		}

		return myMapNodes;

	}

	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 1.1 Converting halfMapCoordinate to server map coordinates
	 * @param HalfMapNode halfMapNode
	 * @return Coordinate
	 */
	public static Coordinate convertCoordinatesToClient(HalfMapNode halfMapNode) {
		return new Coordinate(halfMapNode.getX(), halfMapNode.getY());
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 1.2 Converting halfMap terrain to server map terrain
	 * 
	 * @param HalfMapNode halfMapNode
	 * @return Terrain
	 */
	public static Terrain convertTerrainToClient(HalfMapNode halfMapNode) {
		switch (halfMapNode.getTerrain()) {
		case Water:
			return Terrain.WATER;
		case Grass:
			return Terrain.GRASS;
		case Mountain:
			return Terrain.MOUNTAIN;
		default:
			throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
		}
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 1.3 Converting halfMap fortState to server map fortState
	 * 
	 * @param playerNumber
	 * 
	 * @param HalfMapNode halfMapNode
	 * @param PlayerNumber playerNumber
	 * @return FortState
	 */
	public static FortState convertFortStateToClient(HalfMapNode halfMapNode, PlayerNumber playerNumber) {
		if (halfMapNode.isFortPresent() && playerNumber.equals(PlayerNumber.PlayerOne))
			return FortState.PlayerOneFortPresent;
		else if (halfMapNode.isFortPresent() && playerNumber.equals(PlayerNumber.PlayerTwo))
			return FortState.PlayerTwoFortPresent;
		else
			return FortState.NoOrUnknownFortState;
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 1.4 Converting halfMap PlayerPosition to server map PlayerPosition
	 * 
	 * @param FortState fortState
	 * @return PlayerPositionState
	 */

	private static PlayerPositionState convertPlayerPositionStateToClient(FortState fortState) {
		switch (fortState) {
		case PlayerOneFortPresent:
			return PlayerPositionState.Player1;
		case PlayerTwoFortPresent:
			return PlayerPositionState.Player2;
		default:
			return PlayerPositionState.NoPlayerPresent;
		}
	}

	
	public static Collection<HalfMapNode> convertMapToClient(Map map) {
		HalfMapNode halfMapNode;
		Collection<HalfMapNode> halfMapNodes = new ArrayList<HalfMapNode>();
		ETerrain terrain = null;

		for (Coordinate c : map.getNodes().keySet()) {
			switch (map.getNodes().get(c).getFieldType()) {
			case GRASS:
				terrain = ETerrain.Grass;
				break;
			case MOUNTAIN:
				terrain = ETerrain.Mountain;
				break;
			case WATER:
				terrain = ETerrain.Water;
				break;
			default:
				throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
			}

			if (map.getNodes().get(c).getFortState().equals(FortState.MyFortPresent))
				halfMapNode = new HalfMapNode(c.getX(), c.getY(), true, terrain);
			else
				halfMapNode = new HalfMapNode(c.getX(), c.getY(), false, terrain);

			halfMapNodes.add(halfMapNode);
		}
		return halfMapNodes;
	}
	
	
	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 2 Convert Server Map to FullMap for Client 
	 * 
	 * @param Map map
	 * @param PlayerNumber playerNumber
	 * @return Collection<FullMapNode>
	 */
	public static Collection<FullMapNode> convertMapToFullMapNodes(PlayerNumber playerNumber, Map map) {
		FullMapNode fullMapNode;
		Collection<FullMapNode> fullMapNodes = new ArrayList<FullMapNode>();
		ETerrain terrain = null;
		EPlayerPositionState playerPositionState = null;
		ETreasureState treasureState = null;
		EFortState fortState = null;

		Random random = new Random();
		int conunter = 0;
		int randomNubmer = random.nextInt(32);

		for (Coordinate c : map.getNodes().keySet()) {

			terrain = convertTerrainToClient(map.getNodes().get(c).getFieldType());

			playerPositionState = convertPlayerPositionStateToClient(map.getNodes().get(c).getPlayerPositionState(),
					playerNumber);
			treasureState = convertTreasureStateToClient(map.getNodes().get(c).getTreasureState(), playerNumber);
			fortState = convertFortStateToClient(map.getNodes().get(c).getFortState(), playerNumber);
			int x = map.getNodes().get(c).getCoordinate().getX();
			int y = map.getNodes().get(c).getCoordinate().getY();

			if (conunter == randomNubmer && playerPositionState.equals(EPlayerPositionState.MyPlayerPosition)) {
				playerPositionState = EPlayerPositionState.BothPlayerPosition;
			}

			else if (conunter == randomNubmer)
				playerPositionState = EPlayerPositionState.EnemyPlayerPosition;

			fullMapNode = new FullMapNode(terrain, playerPositionState, treasureState, fortState, x, y);
			fullMapNodes.add(fullMapNode);
			++conunter;
		}

		return fullMapNodes;

	}
	
	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 2.1 Convert Server Map FortState to FullMap EFortState
	 * 
	 * @param FortState fortState
	 * @param PlayerNumber playerNumber
	 * @return EFortState
	 */
	private static EFortState convertFortStateToClient(FortState fortState, PlayerNumber playerNumber) {
		if (playerNumber.equals(PlayerNumber.PlayerOne) && fortState.equals(FortState.PlayerOneFortPresent))
			return EFortState.MyFortPresent;
		if (playerNumber.equals(PlayerNumber.PlayerTwo) && fortState.equals(FortState.PlayerTwoFortPresent))
			return EFortState.MyFortPresent;
		if (playerNumber.equals(PlayerNumber.PlayerTwo) && fortState.equals(FortState.PlayerOneFortPresent))
			return EFortState.NoOrUnknownFortState;
		if (playerNumber.equals(PlayerNumber.PlayerOne) && fortState.equals(FortState.PlayerTwoFortPresent))
			return EFortState.NoOrUnknownFortState;
		return EFortState.NoOrUnknownFortState;
	}
	
	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 2.2 Convert Server Map TreasureState to FullMap ETreasureState
	 * 
	 * @param FortState fortState
	 * @param PlayerNumber playerNumber
	 * @return EFortState
	 */
	private static ETreasureState convertTreasureStateToClient(TreasureState treasureState, PlayerNumber playerNumber) {

		if (playerNumber.equals(PlayerNumber.PlayerOne)
				&& treasureState.equals(TreasureState.PlayerOneTreasureIsPresent))
			return ETreasureState.MyTreasureIsPresent;

		if (playerNumber.equals(PlayerNumber.PlayerTwo)
				&& treasureState.equals(TreasureState.PlayerTwoTreasureIsPresent))
			return ETreasureState.MyTreasureIsPresent;

		return ETreasureState.NoOrUnknownTreasureState;
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 2.3 Convert Server Map PlayerPositionState to FullMap EPlayerPositionState
	 * 
	 * @param PlayerPositionState playerPositionState
	 * @param PlayerNumber playerNumber
	 * @return EPlayerPositionState
	 */
	private static EPlayerPositionState convertPlayerPositionStateToClient(PlayerPositionState playerPositionState,
			PlayerNumber playerNumber) {

		if (playerPositionState.equals(PlayerPositionState.BothPlayerPosition))
			return EPlayerPositionState.BothPlayerPosition;
		if (playerPositionState.equals(PlayerPositionState.NoPlayerPresent))
			return EPlayerPositionState.NoPlayerPresent;

		if (playerNumber.equals(PlayerNumber.PlayerOne) && playerPositionState.equals(PlayerPositionState.Player1))
			return EPlayerPositionState.MyPlayerPosition;

		if (playerNumber.equals(PlayerNumber.PlayerOne) && playerPositionState.equals(PlayerPositionState.Player2))
			return EPlayerPositionState.NoPlayerPresent;

		if (playerNumber.equals(PlayerNumber.PlayerTwo) && playerPositionState.equals(PlayerPositionState.Player2))
			return EPlayerPositionState.MyPlayerPosition;

		if (playerNumber.equals(PlayerNumber.PlayerTwo) && playerPositionState.equals(PlayerPositionState.Player1))
			return EPlayerPositionState.NoPlayerPresent;

		return null;
	}
	
	/**
	 * @author Veljko Radunovic 01528243
	 * TASK 2.4 Convert Server Map Terrain to FullMap ETerrain
	 * 
	 * @param Terrain fieldType
	 * @return ETerrain
	 */
	private static ETerrain convertTerrainToClient(Terrain fieldType) {
		switch (fieldType) {
		case GRASS:
			return ETerrain.Grass;
		case MOUNTAIN:
			return ETerrain.Mountain;
		case WATER:
			return ETerrain.Water;
		default:
			throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
		}
	}
}
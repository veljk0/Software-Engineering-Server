package server.main.marshaller;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.type.MapType;

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
import server.main.enumeration.PlayerGameState;
import server.main.enumeration.PlayerNumber;
import server.main.enumeration.PlayerPositionState;
import server.main.enumeration.Terrain;
import server.main.enumeration.TreasureState;
import server.main.enums.FullMapType;
import server.main.exceptions.MarshallerTerrainException;
import server.main.model.Coordinate;
import server.main.model.Map;
import server.main.model.MapNode;


public class Marshaller {


	/**
	 * Marshaller --- Making data readable in both directions for the CLIENT and for the SERVER
	 * Tasks:
	 * 1. Convert PlayerGameState to Client
	 * 2. Convert Map to Client
	 * 		2.1 Convert Coordinates to Client
	 * 		2.2 Convert Terrain to Client
	 * 		2.3 Convert FortState to Client
	 * 		2.4 Convert PlayerPosition to Client
	 * 		2.5 Convert TreasureState to Client
	 * 3. Convert Treasure State to Client
	 * 4. Convert map to Server
	 * 5. Convert move to Server
	 * @author Veljko Radunovic 01528243
	 */

	
	/**
	 * 
	 * @param ClientData clientData
	 * @param GameState serverGameState
	 */
	
	
	
	public Marshaller() {}
	
	/** 
	 * TASK 1
	 * @param ClientData clientData
	 * @param GameState serverGameState
	 * @return PlayerGameState
	 */
	
	
	/** TASK 2
	 * @param playerNumber 
	 * 
	 * @param ClientData clientData
	 * @param GameState serverGameState
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
			
			for(Coordinate c : randomList) {
				if(myMapNodes.get(c).getFieldType().equals(Terrain.GRASS) 
						&& (!myMapNodes.get(c).getFortState().equals(FortState.PlayerOneFortPresent) 
								&& !myMapNodes.get(c).getFortState().equals(FortState.PlayerTwoFortPresent))) {
					if(playerNumber.equals(PlayerNumber.PlayerOne) && myMapNodes.get(c).getPlayerPositionState().equals(PlayerPositionState.Player1)) { 
						myMapNodes.get(c).setTreasureState(TreasureState.PlayerOneTreasureIsPresent);	
						break;
					}
					
					else if(playerNumber.equals(PlayerNumber.PlayerTwo) && myMapNodes.get(c).getPlayerPositionState().equals(PlayerPositionState.Player2)) {
						myMapNodes.get(c).setTreasureState(TreasureState.PlayerTwoTreasureIsPresent);
						break;
					}
				}
			}
			
			return myMapNodes;
	
		
	}

	/** TASK 2.1
	 * 
	 * @param FullMapNode fullMapNode
	 * @return Coordinate
	 */
	public static Coordinate convertCoordinatesToClient(HalfMapNode halfMapNode) {
		return new Coordinate(halfMapNode.getX(), halfMapNode.getY());
	}

	/** TASK 2.2
	 * 
	 * @param FullMapNode fullMapNode
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

	/** TASK 2.3
	 * @param playerNumber 
	 * 
	 * @param FullMapNode fullMapNode
	 * @return FortState
	 */
	public static FortState convertFortStateToClient(HalfMapNode halfMapNode, PlayerNumber playerNumber) {
		if(halfMapNode.isFortPresent() && playerNumber.equals(PlayerNumber.PlayerOne)) 
			return FortState.PlayerOneFortPresent;
		else if(halfMapNode.isFortPresent() && playerNumber.equals(PlayerNumber.PlayerTwo))
			return FortState.PlayerTwoFortPresent;
		else return FortState.NoOrUnknownFortState;
	}

	/** TASK 2.4
	 * 
	 * @param FullMapNode fullMapNode
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

	/** TASK 2.5
	 * 
	 * @param FullMapNode fullMapNode
	 * @return TreasureState
	 */
	/*
	private static TreasureState convertTreasureStateToClient(HalfMapNode fullMapNode) {
		switch (fullMapNode.getTreasureState()) {
		case MyTreasureIsPresent:
			return TreasureState.MyTreasureIsPresent;
		case NoOrUnknownTreasureState:
			return TreasureState.NoOrUnknownTreasureState;
		default:
			throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
		}
	}
	*/
	/** TASK 3
	 * 
	 * @param GameState serverGameState
	 * @return boolean
	 */
	// TODO
	/*
	public boolean convertTreasureStateToClient(GameState serverGameState) {
		Set<PlayerState> playerStates = serverGameState.getPlayers();
		for (PlayerState p : playerStates)
			if (p.getUniquePlayerID().equals(clientData.getPlayerID())) {
				
				if(p.hasCollectedTreasure()) 
					return true;
			}
		
		return false;
	}

*/
	/**
	 * Marshaller --- Making data readable for SERVER - Methods
	 * 
	 * @author Veljko Radunovic 01528243
	 */

	
	/** TASK 4
	 * 
	 * @param Map map
	 * @return Collection<HalfMapNode>
	 */
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
			
			playerPositionState = convertPlayerPositionStateToClient(map.getNodes().get(c).getPlayerPositionState(), playerNumber);
			treasureState = convertTreasureStateToClient(map.getNodes().get(c).getTreasureState(), playerNumber);
			fortState = convertFortStateToClient(map.getNodes().get(c).getFortState(), playerNumber);
			int x = map.getNodes().get(c).getCoordinate().getX();
			int y = map.getNodes().get(c).getCoordinate().getY();
			
			if(conunter == randomNubmer && playerPositionState.equals(EPlayerPositionState.MyPlayerPosition)) {
				playerPositionState = EPlayerPositionState.BothPlayerPosition;
			}
			
			else if(conunter == randomNubmer)
				playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
			
			
			fullMapNode = new FullMapNode(terrain, playerPositionState, treasureState, fortState, x, y);
			fullMapNodes.add(fullMapNode);
			++conunter;
		}
		
		return fullMapNodes;
		
	
	}

	private static EFortState convertFortStateToClient(FortState fortState, PlayerNumber playerNumber) {
		if(playerNumber.equals(PlayerNumber.PlayerOne) && fortState.equals(FortState.PlayerOneFortPresent)) return EFortState.MyFortPresent;
		if(playerNumber.equals(PlayerNumber.PlayerTwo) && fortState.equals(FortState.PlayerTwoFortPresent)) return EFortState.MyFortPresent;
		if(playerNumber.equals(PlayerNumber.PlayerTwo) && fortState.equals(FortState.PlayerOneFortPresent)) return EFortState.NoOrUnknownFortState;
		if(playerNumber.equals(PlayerNumber.PlayerOne) && fortState.equals(FortState.PlayerTwoFortPresent)) return EFortState.NoOrUnknownFortState;
		return EFortState.NoOrUnknownFortState;
	}

	private static ETreasureState convertTreasureStateToClient(TreasureState treasureState, PlayerNumber playerNumber) {
		
		if(playerNumber.equals(PlayerNumber.PlayerOne) && treasureState.equals(TreasureState.PlayerOneTreasureIsPresent))
			return ETreasureState.MyTreasureIsPresent;
		
		if(playerNumber.equals(PlayerNumber.PlayerTwo) && treasureState.equals(TreasureState.PlayerTwoTreasureIsPresent))
			return ETreasureState.MyTreasureIsPresent;
		
		return ETreasureState.NoOrUnknownTreasureState;
	}

	private static EPlayerPositionState convertPlayerPositionStateToClient(PlayerPositionState playerPositionState,
			PlayerNumber playerNumber) {
		
		if(playerPositionState.equals(PlayerPositionState.BothPlayerPosition)) return EPlayerPositionState.BothPlayerPosition;
		if(playerPositionState.equals(PlayerPositionState.NoPlayerPresent)) return EPlayerPositionState.NoPlayerPresent;
		
		if(playerNumber.equals(PlayerNumber.PlayerOne) && playerPositionState.equals(PlayerPositionState.Player1))
			return EPlayerPositionState.MyPlayerPosition;
		
		if(playerNumber.equals(PlayerNumber.PlayerOne) && playerPositionState.equals(PlayerPositionState.Player2))
			return EPlayerPositionState.NoPlayerPresent;
		
		if(playerNumber.equals(PlayerNumber.PlayerTwo) && playerPositionState.equals(PlayerPositionState.Player2))
			return EPlayerPositionState.MyPlayerPosition;
		
		if(playerNumber.equals(PlayerNumber.PlayerTwo) && playerPositionState.equals(PlayerPositionState.Player1))
			return EPlayerPositionState.NoPlayerPresent;
		
		return null;
		}
		
		


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
package server.main.marshaller;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import MessagesBase.ETerrain;
import MessagesBase.HalfMap;
import MessagesBase.HalfMapNode;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
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
	
	
	
	public Marshaller() {
		//clientData = ClientData.getClientDataInstance();
	}
	
	/** 
	 * TASK 1
	 * @param ClientData clientData
	 * @param GameState serverGameState
	 * @return PlayerGameState
	 */
	/*
	public PlayerGameState convertPlayerGameStateToClient(GameState serverGameState) {		
		Set<PlayerState> playerStates = serverGameState.getPlayers();
		for (PlayerState p : playerStates)
			if (p.getUniquePlayerID().equals(clientData.getPlayerID())) {
				
				if(p.hasCollectedTreasure()) 
					ClientData.getClientDataInstance().setTreasureFound(true);
				
				switch (p.getState()) {
					case MustWait:
						return PlayerGameState.MustWait;
					case MustAct:
						return PlayerGameState.MustAct;
					case Lost:
						return PlayerGameState.Lost;
					case Won:
						return PlayerGameState.Won;
					default:
						throw new PlayerGameStateException("Marshaller PlayerGameState error");
				}
			}
		return null;
	}
	*/
	/** TASK 2
	 * @param playerNumber 
	 * 
	 * @param ClientData clientData
	 * @param GameState serverGameState
	 * @return HashMap<Coordinate, MapNode>
	 */
	public static HashMap<Coordinate, MapNode> convertMapToClient(HalfMap halfMap, PlayerNumber playerNumber) {

		
			Collection<HalfMapNode> fullMapNodes = halfMap.getMapNodes();
			HashMap<Coordinate, MapNode> myMapNodes = new HashMap<Coordinate, MapNode>();
	
			for (HalfMapNode fullMapNode : fullMapNodes) {
				MapNode myMapNode = new MapNode();
				myMapNode.setCoordinate(convertCoordinatesToClient(fullMapNode));
				myMapNode.setFieldType(convertTerrainToClient(fullMapNode));
				myMapNode.setFortState(convertFortStateToClient(fullMapNode, playerNumber));
				//myMapNode.setPlayerPositionState(convertPlayerPositionStateToClient(fullMapNode));
				
				//myMapNode.setTreasureState(convertTreasureStateToClient(fullMapNode));
				myMapNodes.put(myMapNode.getCoordinate(), myMapNode);
			}
			
			List<Coordinate> randomList = new ArrayList<>(myMapNodes.keySet());
			
			Collections.shuffle(randomList);
			
			for(Coordinate c: randomList) {
				if(myMapNodes.get(c).getFieldType().equals(Terrain.GRASS) 
						&& (!myMapNodes.get(c).getFortState().equals(FortState.PlayerOneFortPresent) 
								&& !myMapNodes.get(c).getFortState().equals(FortState.PlayerTwoFortPresent)))
					
					if(playerNumber.equals(PlayerNumber.PlayerOne)) myMapNodes.get(c).setTreasureState(TreasureState.PlayerOneTreasureIsPresent);		
					else myMapNodes.get(c).setTreasureState(TreasureState.PlayerTwoTreasureIsPresent);		
					
				
				break; 
			}
			
			return myMapNodes;
	
		
	}

	/** TASK 2.1
	 * 
	 * @param FullMapNode fullMapNode
	 * @return Coordinate
	 */
	public static Coordinate convertCoordinatesToClient(HalfMapNode fullMapNode) {
		return new Coordinate(fullMapNode.getX(), fullMapNode.getY());
	}

	/** TASK 2.2
	 * 
	 * @param FullMapNode fullMapNode
	 * @return Terrain
	 */
	public static Terrain convertTerrainToClient(HalfMapNode fullMapNode) {
		switch (fullMapNode.getTerrain()) {
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
	
	/*
	private static PlayerPositionState convertPlayerPositionStateToClient(HalfMapNode fullMapNode) {
		if(fullMapNode == null) throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");

		switch (fullMapNode.getPlayerPositionState()) {
		case BothPlayerPosition:
			return PlayerPositionState.BothPlayerPosition;
		case EnemyPlayerPosition:
			return PlayerPositionState.EnemyPlayerPosition;
		case MyPlayerPosition:
			
			return PlayerPositionState.MyPlayerPosition;
		case NoPlayerPresent:
			return PlayerPositionState.NoPlayerPresent;
		default:
			throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
		}
	}
*/
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
	public Collection<HalfMapNode> convertMapToServer(Map map) {
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

	/** TASK 5
	 * 
	 * @param Move move
	 * @return EMove
	 */
	
	/*
	public EMove convertMoveToServer(Move move) {
		switch (move) {
		case Down:
			return EMove.Down;
		case Up:
			return EMove.Up;
		case Right:
			return EMove.Right;
		case Left:
			return EMove.Left;
		default:
			throw new MarshallerTerrainException("MarshallerTerrainException", "Marshaller Terrain error");
		}
	}
	*/
}
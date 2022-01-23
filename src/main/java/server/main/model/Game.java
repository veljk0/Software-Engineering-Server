package server.main.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.main.enumeration.PlayerGameState;
import server.main.enumeration.PlayerNumber;
import server.main.enums.FullMapType;
import server.main.exceptions.NumberOfPlayersException;
import server.main.exceptions.PlayerNotRegisteredException;

public class Game {
	
	static Logger logger = LoggerFactory.getLogger(Game.class);
	
	private String gameID;
	List<Player> players;
	private Map fullMap;
	private int halfMapCounter;
	private long creationTime;
	private String gameStateID;

	public Game(String gameID) {
		this.gameID = gameID;
		this.players = new ArrayList<>();
		this.fullMap = new Map();
		this.halfMapCounter = 0;
		this.setCreationTime(System.currentTimeMillis());
		this.gameStateID = UUID.randomUUID().toString();
	}

	
	/**
	 * adding new player to the game
	 * @param player
	 */
	public void addPlayer(Player player) {
		if (players.size() >= 2) throw new NumberOfPlayersException("Number of players", "two players have been already registered for this game");
		
		if (players.isEmpty()) player.setPlayerNumber(PlayerNumber.PlayerOne);
		else player.setPlayerNumber(PlayerNumber.PlayerTwo);
		
		this.players.add(player);
		logger.info("ADDED PLAYER TO GAME " + gameID + " " +  player.getUniquePlayerID());
		this.gameStateID = UUID.randomUUID().toString();
	}

	
	/**
	 * Random picking first player to start acting
	 */
	public void findPlayerToStart() {
		if (players.size() == 2) {
			Random random = new Random();
			boolean luck = random.nextBoolean();
			
			if(luck) players.get(0).setPlayerGameState(PlayerGameState.MustAct);
			else players.get(1).setPlayerGameState(PlayerGameState.MustAct);
		}
	}
	
	
	public void combineHalfMaps() {
		this.gameStateID = UUID.randomUUID().toString();	

		
		Player player1 = players.get(0);
		Player player2 = players.get(1);
		
		if(player1.isHalfMap() && player2.isHalfMap()) {
			logger.info("MAP: combining halfmaps");
			List<Player> randomPlayers = new ArrayList<>();
			randomPlayers.add(player1);
			randomPlayers.add(player2);
			Collections.shuffle(randomPlayers);
			Collections.shuffle(randomPlayers);
			Collections.shuffle(randomPlayers);
			
			HashMap<Coordinate, MapNode> hashMap = new HashMap<Coordinate, MapNode>();
			hashMap.putAll(randomPlayers.get(0).getHalfMap().getNodes());
			updateCoordinates(randomPlayers.get(1).getHalfMap());
			hashMap.putAll(randomPlayers.get(1).getHalfMap().getNodes());
			fullMap.setNodes(hashMap);
			fullMap.printMap();
			
			for (Player p : players) {
				if(p.getPlayerGameState().equals(PlayerGameState.MustAct))
					p.setPlayerGameState(PlayerGameState.MustWait);
				else if(p.getPlayerGameState().equals(PlayerGameState.MustWait))
					p.setPlayerGameState(PlayerGameState.MustAct);
			}
			
		}
	}
	
	

	private void updateCoordinates(Map halfMap) {
		int increaseNumber = 0;

		if (fullMap.getMaptype().equals(FullMapType.HORIZONTAL)) {
			logger.info("GAME: updating horizontal nodes");
			increaseNumber = 8;
			for (Coordinate c : halfMap.getNodes().keySet()) {
				halfMap.getNodes().get(c).updateCoordinateX(increaseNumber);
			}

		}

		else if (fullMap.getMaptype().equals(FullMapType.VERTICAL)) {
			logger.info("GAME: updating vertical nodes");
			increaseNumber = 4;
			for (Coordinate c : halfMap.getNodes().keySet()) {
				halfMap.getNodes().get(c).updateCoordinateY(increaseNumber);
			}

		}
	}

	public PlayerNumber getPlayerNumber(String uniquePlayerID) {
		PlayerNumber playerNumber = null;

		for (Player player : players)
			if (player.getUniquePlayerID().equals(uniquePlayerID))
				playerNumber = player.getPlayerNumber();

		if (playerNumber == null)
			throw new PlayerNotRegisteredException("PlayerNotRegisteredException",
					"Player is not registered to the the Game!");

		return playerNumber;
	}

	public void finishGame(String uniquePlayerID) {
		for(Player p : getPlayers()) {
			if(p.getUniquePlayerID().equals(uniquePlayerID))
				p.setPlayerGameState(PlayerGameState.Lost);
			else
				p.setPlayerGameState(PlayerGameState.Won);
		}
		
	}

	/**
	 * 
	 * Getters & Setters
	 * 
	 */
	
	public String getGameID() {
		return gameID;
	}



	public void setGameID(String gameID) {
		this.gameID = gameID;
	}



	public List<Player> getPlayers() {
		return players;
	}



	public void setPlayers(List<Player> players) {
		this.players = players;
	}



	public Map getFullMap() {
		return fullMap;
	}



	public void setFullMap(Map fullMap) {
		this.fullMap = fullMap;
	}



	public int getHalfMapCounter() {
		return halfMapCounter;
	}



	public void setHalfMapCounter(int halfMapCounter) {
		this.halfMapCounter = halfMapCounter;
	}



	public long getCreationTime() {
		return creationTime;
	}



	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}



	public String getGameStateID() {
		return gameStateID;
	}



	public void setGameStateID(String gameStateID) {
		this.gameStateID = gameStateID;
	}


	@Override
	public String toString() {
		return "Game [gameID=" + gameID + ", players=" + players + ", fullMap=" + fullMap + ", halfMapCounter="
				+ halfMapCounter + ", creationTime=" + creationTime + ", gameStateID=" + gameStateID + "]";
	}
}

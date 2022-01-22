package server.main.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import server.main.enumeration.PlayerNumber;
import server.main.exceptions.NumberOfPlayersException;
import server.main.exceptions.PlayerNotRegisteredException;

public class Game {
	
	private String gameID;
	List<Player> players;

	public Game(String gameID) {
		this.gameID = gameID;
		this.players = new ArrayList<>();
	}

	public String getGameID() {
		return gameID;
	}
	
	public void addPlayer(Player player) {
		if(maxAmountOfPlayerIsReached()) throw new NumberOfPlayersException("Number of players", "two players have been already registered for this game");
		if(players.isEmpty()) {
			player.setPlayerNumber(PlayerNumber.PlayerOne);
			
		}
		else player.setPlayerNumber(PlayerNumber.PlayerTwo);
		players.add(player);
	}

	private boolean maxAmountOfPlayerIsReached() {
		if(players.size() >= 2) return true;
		return false;
	}

	public void registerHalfMap(Map playerHalfMap, String playerID) {
		
		
		
	}

	public PlayerNumber gePlayerNumber(String uniquePlayerID) {
		PlayerNumber playerNumber = null; 
		
		System.out.println("UBASCILOOOOOOOOOOOOOOOOOOOOOOOOOOOO    " + players.size() + "\n");
		
		for(Player player: players) {
			System.out.println(player.getUniquePlayerID() + "  " +  uniquePlayerID);
			if(player.getUniquePlayerID().equals(uniquePlayerID)) playerNumber = player.getPlayerNumber();	
			break; 
		}
		
		if(playerNumber == null) throw new PlayerNotRegisteredException("PlayerNotRegisteredException", "Player is not registered to the the Game!");
		return playerNumber;
		//return  players.stream().filter(p -> p.getUniquePlayerID().equals(uniquePlayerID)).findAny().orElse(null);
	}
	
}

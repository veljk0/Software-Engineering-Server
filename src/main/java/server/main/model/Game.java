package server.main.model;

import java.util.ArrayList;
import java.util.List;

import server.main.exceptions.NumberOfPlayersException;

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
		players.add(player);
	}

	private boolean maxAmountOfPlayerIsReached() {
		if(players.size() >= 2) return true;
		return false;
	}
	
}

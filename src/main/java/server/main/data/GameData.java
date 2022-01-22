package server.main.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import server.main.model.Game;

@Component
public class GameData {
	
	private Map<String,Game> games;
	
	public GameData() {
		games = new HashMap<String, Game>();
	
	}
	
	public Map<String, Game> getGames() {
		return games;
	}

	public void setGames(Map<String, Game> games) {
		this.games = games;
	}
}

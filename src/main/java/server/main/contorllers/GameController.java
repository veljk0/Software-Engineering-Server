package server.main.contorllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.main.data.GameData;
import server.main.exceptions.GameNotExistingException;
import server.main.model.Game;

@Component
public class GameController {
	
	@Autowired
	private GameData gameData;
	
	public void addGame(Game game) {
		gameData.getGames().put(game.getGameID(), game);
	}
	
	public Game getGame(String gameID) {
		for (String g : gameData.getGames().keySet()) {
			if(g.equals(gameID))
				return gameData.getGames().get(gameID);
		}
		return null;
	}

	public void checkIfGameExists(String gameID) {
		if(!gameData.getGames().keySet().contains(gameID)) throw new GameNotExistingException("Game does not exist error", "id has not been found");
	}
}

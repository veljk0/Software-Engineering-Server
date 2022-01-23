package server.main.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import server.main.model.Game;

/**
 * @author Veljko Radunovic 01528243 GameData Spring component that is used to
 *         store data. In this case we are storing all new created games All
 *         created games will be saved here.
 */

@Component
@EnableScheduling
public class GameData {

	static Logger logger = LoggerFactory.getLogger(GameData.class);

	private Map<String, Game> games;
	private List<String> gameIDs;

	public GameData() {
		games = new HashMap<String, Game>();
		gameIDs = new ArrayList<String>();
	}

	/**
	 * Checking creation time of the games Calling function
	 */
	@Scheduled(fixedRate = 5000)
	public void manageOldGames() {
		logger.info("GAMEDATA: checking games creation times");
		System.out.println("Checking creation time of the games");
		for (String g : games.keySet()) {
			if (System.currentTimeMillis() - games.get(g).getCreationTime() > 600000) {
				games.remove(g);
				logger.info("GAMEDATA: removing game that is to old");
			}

		}
	}

	/**
	 * Checking number of already existing games Adding new created game to the map
	 * 
	 * @param Game game
	 */
	public void addGame(Game game) {
		checkNubmberOfGames();
		gameIDs.add(game.getGameID());
		games.put(game.getGameID(), game);
		logger.info("GAMEDATA: added new game to the data: " + game.getGameID());
	}

	private void checkNubmberOfGames() {
		logger.info("GAMEDATA: checking number of games");
		if (games.size() > 999) {
			games.remove(gameIDs.get(0));
			gameIDs.remove(0);
		}

	}

	public synchronized Map<String, Game> getGames() {
		return games;
	}
}

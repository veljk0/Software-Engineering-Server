package server.main.contorllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.EPlayerGameState;
import MessagesGameState.FullMap;
import MessagesGameState.FullMapNode;
import MessagesGameState.GameState;
import MessagesGameState.PlayerState;
import server.main.data.GameData;
import server.main.enumeration.PlayerGameState;
import server.main.enumeration.PlayerNumber;
import server.main.marshaller.Marshaller;
import server.main.model.Game;
import server.main.model.Map;
import server.main.model.Player;

/**
 *@author Veljko Radunovic 01528243
 * GameController
 */

@Component
public class GameController {

	@Autowired
	private GameData gameData;

	public void addGame(Game game) {
		gameData.addGame(game);
	}

	public Game getGame(String gameID) {
		for (String g : gameData.getGames().keySet()) {
			if (g.equals(gameID))
				return gameData.getGames().get(gameID);
		}
		return null;
	}

	public boolean checkIfGameExists(String gameID) {
		if (gameData.getGames().keySet().contains(gameID))
			return true;
		return false;

	}

	/**
	 * @author Veljko Radunovic 01528243
	 * Finding the game where we should find first player that should act
	 * @param gameID
	 */
	public void findFirstPlayerToStart(String gameID) {
		gameData.getGames().get(gameID).findPlayerToStart();
	}

	/**
	 * * @author Veljko Radunovic 01528243
	 * Check number of registered players
	 * 
	 * @param uniqueGameID
	 * @return boolean
	 */

	public boolean checkNumberOfRegisteredPlayers(String uniqueGameID) {
		if (gameData.getGames().get(uniqueGameID).getPlayers().size() != 2)
			return false;
		return true;
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * check if player has already sent a halfMap
	 * 
	 * @param gameID
	 * @param playerID
	 * @return boolean
	 */
	public boolean checkIfPlayerAlereadySentMap(String gameID, String playerID) {
		for (Player p : gameData.getGames().get(gameID).getPlayers())
			if (p.getUniquePlayerID().equals(playerID))
				if (p.isHalfMap())
					return true;

		return false;
	}

	/**
	 * @author Veljko Radunovic 01528243
	 * check if player is registered in this game
	 * 
	 * @param game
	 * @param playerID
	 * @return boolean
	 */
	public boolean checkIfPlayerIsInTheGame(Game game, String playerID) {
		boolean flag = false;

		for (Player p : game.getPlayers())
			if (p.getUniquePlayerID().equals(playerID))
				flag = true;

		return flag;
	}
	
	/**
	 * @author Veljko Radunovic 01528243
	 * Registering HalfMap that server received from client.
	 * HalfMap is setted for user and added to the game.
	 * When both players have sent halfmaps > combineHalfMaps() will generate fullMap
	 * @param playerHalfMap
	 * @param playerID
	 * @param gameID
	 */
	public void registerHalfMap(Map playerHalfMap, String playerID, String gameID) {
		for (Player player : gameData.getGames().get(gameID).getPlayers()) {
			if (player.getUniquePlayerID().equals(playerID)) {
				player.setHalfMap(playerHalfMap);
				gameData.getGames().get(gameID).combineHalfMaps();
			}
		}

	}
	
	/**
	 * @author Veljko Radunovic 01528243
	 * Returning all data for client, after client has requested states.
	 * This method is called when sending request on endpoint 4 
	 * @param gameID
	 * @param playerID
	 * @return GameState
	 */
	public GameState getGameStateForPlayer(String gameID, String playerID) {
		Collection<PlayerState> playersStates = loadPlayerStates(gameID, playerID);

		Map gamefullMap = gameData.getGames().get(gameID).getFullMap();

		PlayerNumber playerNumber = gameData.getGames().get(gameID).getPlayerNumber(playerID);

		Collection<FullMapNode> fullMapNodes = Marshaller.convertMapToFullMapNodes(playerNumber, gamefullMap);

		Optional<FullMap> fullmap = Optional.ofNullable(new FullMap(fullMapNodes));
		return new GameState(fullmap, playersStates, gameData.getGames().get(gameID).getGameStateID());
	}

	/**
	 * @author Veljko Radunovic 01528243 Loading PlayerStates for players from perspective of one player using playerID
	 * @param gameID
	 * @param playerID
	 * @return Collection<PlayerState>
	 */
	private Collection<PlayerState> loadPlayerStates(String gameID, String playerID) {
		Collection<PlayerState> result = new ArrayList<>();

		for (Player p : gameData.getGames().get(gameID).getPlayers()) {
			if (p.getUniquePlayerID().equals(playerID)) {
				EPlayerGameState estate;
				switch (p.getPlayerGameState()) {
				case Lost:
					estate = EPlayerGameState.Lost;
					break;
				case Won:
					estate = EPlayerGameState.Won;
					break;
				case MustAct:
					estate = EPlayerGameState.MustAct;
					break;
				case MustWait:
					estate = EPlayerGameState.MustWait;
					break;
				default:
					estate = EPlayerGameState.Lost;
				}
				result.add(new PlayerState(p.getFirstName(), p.getLastName(), p.getStudentID(), estate,
						new UniquePlayerIdentifier(p.getUniquePlayerID()), false));
			} else {
				EPlayerGameState estate2;
				switch (p.getPlayerGameState()) {
				case Lost:
					estate2 = EPlayerGameState.Lost;
					break;
				case Won:
					estate2 = EPlayerGameState.Won;
					break;
				case MustAct:
					estate2 = EPlayerGameState.MustAct;
					break;
				case MustWait:
					estate2 = EPlayerGameState.MustWait;
					break;
				default:
					estate2 = EPlayerGameState.Lost;
				}
				result.add(new PlayerState(p.getFirstName(), p.getLastName(), p.getStudentID(), estate2,
						new UniquePlayerIdentifier("anonymous"), false));
			}
		}

		return result;
	}

	/**
	 * @author Veljko Radunovic 01528243 Changing playerGameState
	 * @param gameID
	 * @param playerID
	 */
	public void changePlayerGameState(String gameID, String playerID) {
		for (Player p : gameData.getGames().get(gameID).getPlayers()) {
			if (!p.getUniquePlayerID().equals(playerID)) {
				p.setPlayerGameState(PlayerGameState.MustAct);
			} else if (p.getUniquePlayerID().equals(playerID)) {
				p.setPlayerGameState(PlayerGameState.MustWait);
			}
		}
	}

	/**
	 * @author Veljko Radunovic 01528243 finishGame method This function will be
	 *         called if user send move before have right to so. Throught playerID
	 *         user will be notified that the game is lost. Other player from the
	 *         game will win the game.
	 * @param gameID
	 * @param playerID
	 */
	public void finishGame(String gameID, String playerID) {
		for (Player player : gameData.getGames().get(gameID).getPlayers()) {
			if (player.getUniquePlayerID().equals(playerID)) {
				player.setPlayerGameState(PlayerGameState.Lost);
			} else {
				player.setPlayerGameState(PlayerGameState.Won);
			}
		}

	}

}

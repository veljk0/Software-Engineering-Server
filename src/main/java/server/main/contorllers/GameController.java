package server.main.contorllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import server.main.enumeration.PlayerPositionState;
import server.main.exceptions.PlayerNotTurnException;
import server.main.marshaller.Marshaller;
import server.main.model.Coordinate;
import server.main.model.Game;
import server.main.model.Map;
import server.main.model.Player;

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
	 * Finding the game where we should find first player that should act
	 * @param gameID
	 */
	public void findFirstPlayerToStart(String gameID) { gameData.getGames().get(gameID).findPlayerToStart(); }

	
	/**
	 * Check number of registered players
	 * @param uniqueGameID
	 * @return
	 */
	
	public boolean checkNumberOfRegisteredPlayers(String uniqueGameID) {
		if (gameData.getGames().get(uniqueGameID).getPlayers().size() != 2) return false;
		return true;
	}


	/**
	 * check if player has already sent a halfMap
	 * @param gameID
	 * @param playerID
	 * @return
	 */
	public boolean checkIfPlayerAlereadySentMap(String gameID, String playerID) {
		for(Player p : gameData.getGames().get(gameID).getPlayers())
			if(p.getUniquePlayerID().equals(playerID))
				if(p.isHalfMap()) return true;
		
		return false;
	}

	
	/**
	 * check if player is registered in this game
	 * @param game
	 * @param playerID
	 * @return
	 */
	public boolean checkIfPlayerIsInTheGame(Game game, String playerID) {
		boolean flag = false;
		
		for (Player p : game.getPlayers())
			if (p.getUniquePlayerID().equals(playerID))
				flag = true;
		
		return flag;
	}
	
	
	
	public void registerHalfMap(Map playerHalfMap, String playerID, String gameID) {
		for(Player player : gameData.getGames().get(gameID).getPlayers()) {
			if(player.getUniquePlayerID().equals(playerID)) {
				player.setHalfMap(playerHalfMap);
				gameData.getGames().get(gameID).combineHalfMaps();
			}
		}
			
	}
	
	
	
	
	
	public GameState getGameStateForPlayer(String gameID, String playerID) {
		Collection<PlayerState> playersStates = loadPlayerStates(gameID, playerID);
		
		Map gamefullMap = gameData.getGames().get(gameID).getFullMap();
		
		PlayerNumber playerNumber = gameData.getGames().get(gameID).getPlayerNumber(playerID);
		
		Collection<FullMapNode> fullMapNodes = Marshaller.convertMapToFullMapNodes(playerNumber, gamefullMap);
		
		
		Optional<FullMap> fullmap = Optional.ofNullable(new FullMap(fullMapNodes));
		return new GameState(fullmap, playersStates, gameData.getGames().get(gameID).getGameStateID());
	}
	
	
	
	public GameState getInfoForPlayer(String gameID, String playerID) throws PlayerNotTurnException {
		Collection<PlayerState> playerStates = new ArrayList<PlayerState>();
		
		checkIfGameExists(gameID);
		checkNumberOfRegisteredPlayers(gameID);
		checkIfPlayerIsInTheGame(gameData.getGames().get(gameID), playerID);
		checkIfPlayerMustAct(gameID, playerID);
		
		Player myPlayer = new Player();
		
		for(Player p : gameData.getGames().get(gameID).getPlayers())
			if(p.getUniquePlayerID().equals(playerID))
				myPlayer = p;
		
		
		Map myfullMap = gameData.getGames().get(gameID).getFullMap();
		
		playerStates = loadPlayerStates(gameID, playerID);
		
		Collection<FullMapNode> fullMapNodes = Marshaller.convertMapToFullMapNodes(myPlayer.getPlayerNumber(), myfullMap);
		
		
		Optional<FullMap> fullmap = Optional.ofNullable(new FullMap(fullMapNodes));
		
		
		GameState result = new GameState(fullmap, playerStates, gameData.getGames().get(gameID).getGameStateID());
		return result;	
	}
	


	private Collection<PlayerState> loadPlayerStates(String gameID, String playerID) {
			Collection<PlayerState> result = new ArrayList<>();
		
		 	
			for(Player p : gameData.getGames().get(gameID).getPlayers()) {
		 		if(p.getUniquePlayerID().equals(playerID)) {
		 			EPlayerGameState estate;
		 			switch(p.getPlayerGameState()) {
			 			case Lost: estate = EPlayerGameState.Lost; break;
			 			case Won: estate = EPlayerGameState.Won; break;
			 			case MustAct: estate = EPlayerGameState.MustAct; break;
			 			case MustWait: estate = EPlayerGameState.MustWait; break;
			 			default: estate = EPlayerGameState.Lost;
		 			}
		 			result.add(new PlayerState(p.getFirstName(), p.getLastName(), p.getStudentID(),estate, new UniquePlayerIdentifier(p.getUniquePlayerID()), false));
		 		}
		 		else {
		 			EPlayerGameState estate2;
		 			switch(p.getPlayerGameState()) {
		 			case Lost: estate2 = EPlayerGameState.Lost; break;
		 			case Won: estate2 = EPlayerGameState.Won; break;
		 			case MustAct: estate2 = EPlayerGameState.MustAct; break;
		 			case MustWait: estate2 = EPlayerGameState.MustWait; break;
		 			default: estate2 = EPlayerGameState.Lost;
	 			}
		 			result.add(new PlayerState(p.getFirstName(), p.getLastName(), p.getStudentID(), estate2, new UniquePlayerIdentifier("anonymous"), false));
		 		}
			}	
		 			
		return result;
	}

	public void checkIfPlayerMustAct(String uniqueGameID, String uniquePlayerID) {
		if(gameData.getGames().get(uniqueGameID).getPlayers().size() != 2) return;
		
		setNextPlayeToAct(uniqueGameID);
		
	}
	
	public void changePlayerGameState(String gameID, String playerID) {
		for(Player p : gameData.getGames().get(gameID).getPlayers()) {
			if(!p.getUniquePlayerID().equals(playerID)) {
				p.setPlayerGameState(PlayerGameState.MustAct);
			}
		}
			
	}

	private void setNextPlayeToAct(String uniqueGameID) {
		if(gameData.getGames().get(uniqueGameID).getPlayers().size() != 2) return;
		
		Player player1 = gameData.getGames().get(uniqueGameID).getPlayers().get(0);
		Player player2 = gameData.getGames().get(uniqueGameID).getPlayers().get(1);
		
		Random random = new Random();
		boolean helper = random.nextBoolean();
		
		if(player1.getPlayerGameState().equals(player2.getPlayerGameState())) {
			if(helper) player1.setPlayerGameState(PlayerGameState.MustAct);
			else  player2.setPlayerGameState(PlayerGameState.MustAct);
		}
	}

	public void finishGame(String gameID, String playerID) {
		for(Player player : gameData.getGames().get(gameID).getPlayers()) {
			if(player.getUniquePlayerID().equals(playerID)) {
				player.setPlayerGameState(PlayerGameState.Lost);
			}
			else {
				player.setPlayerGameState(PlayerGameState.Won);
			}
		}
		
	}

	

	

	

	
}

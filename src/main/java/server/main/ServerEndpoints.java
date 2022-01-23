package server.main;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import MessagesBase.HalfMap;
import MessagesBase.PlayerRegistration;
import MessagesBase.ResponseEnvelope;
import MessagesBase.UniqueGameIdentifier;
import MessagesBase.UniquePlayerIdentifier;
import MessagesGameState.GameState;
import server.main.contorllers.GameController;
import server.main.contorllers.ValidatorController;
import server.main.enumeration.PlayerNumber;
import server.main.exceptions.GameNotExistingException;
import server.main.exceptions.GenericExampleException;
import server.main.exceptions.NotEnoughPlayersException;
import server.main.exceptions.PlayerAlreadySentMapException;
import server.main.exceptions.PlayerNotTurnException;
import server.main.generators.Generator;
import server.main.marshaller.Marshaller;
import server.main.model.Game;
import server.main.model.Map;
import server.main.model.Player;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
	
	@Autowired
	private GameController gameController;

	@Autowired
	private ValidatorController validatorController;
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		
		
		String gameID = Generator.generateID();
		
		if(gameController.checkIfGameExists(gameID) == true)
			throw new GameNotExistingException("Game already exist error", "id has been found in gamedata");
		
		
		
		gameController.addGame(new Game(gameID));

		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(gameID);
		return gameIdentifier;
	}

	
	
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
		if(gameController.checkIfGameExists(gameID.getUniqueGameID()) == false)
			throw new GameNotExistingException("Game does not exist error", "id has not been found");
		
		if(gameController.checkNumberOfRegisteredPlayers(gameID.getUniqueGameID()) == true)
			throw new NotEnoughPlayersException("To much players","There is enoughPlayers for this game");
		
		Player player = new Player(playerRegistration.getStudentFirstName(), playerRegistration.getStudentLastName(),
				playerRegistration.getStudentID(), newPlayerID.getUniquePlayerID());
		
		gameController.getGame(gameID.getUniqueGameID()).addPlayer(player);
		gameController.findFirstPlayerToStart(gameID.getUniqueGameID());

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		return playerIDMessage;
	}

	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> maxExchange(@PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody HalfMap halfMap) {
		
		String uniqueGameID = gameID.getUniqueGameID();
		String playerID = halfMap.getUniquePlayerID();
		
		
		
		if(gameController.checkIfGameExists(uniqueGameID) == false)
			throw new GameNotExistingException("Game does not exist error", "id has not been found");
		
		if(gameController.checkIfPlayerIsInTheGame(gameController.getGame(uniqueGameID), playerID) == false)
			throw new NotEnoughPlayersException("Player not found", "player with this ID is not registered for this game");
		
		if(gameController.checkNumberOfRegisteredPlayers(uniqueGameID) == false)
			throw new NotEnoughPlayersException("NotEnoughPlayersException","There is not enoughPlayers or the is not registered for this game");
		
		if(gameController.checkIfPlayerAlereadySentMap(uniqueGameID, playerID) == true)
			throw new PlayerAlreadySentMapException("PlayerAlreadySentMapException", "player has already sent half map");
		
		PlayerNumber playerNumber = gameController.getGame(uniqueGameID).getPlayerNumber(playerID);
		
		Map playerHalfMap = new Map(Marshaller.convertMapToServer(halfMap, playerNumber));
		
		try {
			validatorController.validateMap(playerHalfMap);
		}catch(GenericExampleException e) {
			gameController.finishGame(uniqueGameID, playerID);
			return new ResponseEnvelope<>(e);
		}
		
		gameController.registerHalfMap(playerHalfMap, playerID, uniqueGameID);
		
		gameController.changePlayerGameState(uniqueGameID, playerID);
		
		return new ResponseEnvelope<>();
	}

	
	
	/**
	 * 
	 * @param gameID
	 * @param playerID
	 * @return
	 * @throws PlayerNotTurnException 
	 */
	
	
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> getGameState(@PathVariable String gameID,
			@PathVariable String playerID) throws PlayerNotTurnException {
		
		if(gameController.checkIfGameExists(gameID) == false)
			throw new GameNotExistingException("Game does not exist error", "id has not been found");
		
		if(gameController.checkIfPlayerIsInTheGame(gameController.getGame(gameID), playerID) == false)
			throw new NotEnoughPlayersException("Player not found", "player with this ID is not registered for this game");
		
		GameState playergameState = gameController.getGameStateForPlayer(gameID, playerID);
		
		return new ResponseEnvelope<GameState>(playergameState);
	}

	
	
	
	
	
	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}

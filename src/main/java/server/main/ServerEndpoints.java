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
import server.main.contorllers.GameController;
import server.main.exceptions.GenericExampleException;
import server.main.generators.Generator;
import server.main.model.Game;
import server.main.model.Player;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
	
	@Autowired
	private GameController gameController;
	
	// ADDITONAL TIPS ON THIS MATTER ARE GIVEN THROUGHOUT THE TUTORIAL SESSION!
	// Note, the same network messages which you have used for the client (along
	// with its documentation) apply to the server too.

	/*
	 * Please do NOT add all the necessary code in the methods provided below. When
	 * following the single responsibility principle those methods should only
	 * contain the bare minimum related to network handling. Such as the converts
	 * which convert the objects from/to internal data objects to/from messages.
	 * Include the other logic (e.g., new game creation and game id handling) by
	 * means of composition (i.e., it should be provided by other classes).
	 */

	// below you can find two example endpoints (i.e., one GET and one POST based
	// endpoint which are all endpoint types which you need),
	// Hence, all the other endpoints can be defined similarly.

	// example for a GET endpoint based on /games
	// similar to the client, the HTTP method and the expected data types are
	// specified at the server side too
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {

		
		boolean showExceptionHandling = false;
		if (showExceptionHandling) {
			// if any error occurs, simply throw an exception with inherits from
			// GenericExampleException
			// the given code than takes care of responding with an error message to the
			// client based on the @ExceptionHandler below
			// make yourself familiar with this concept by setting
			// showExceptionHandling=true
			// and creating a new game through the browser
			// your implementation should use more useful error messages and specialized
			// exception classes
			throw new GenericExampleException("Name: Something", "Message: went totally wrong");
		}

		// TIP: you will need to adapt this part to generate a game id with the valid
		// length. A simple solution for this
		// would be creating an alphabet and choosing random characters from it till the
		// new game id becomes long enough
		String gameID = Generator.generateID();
		gameController.addGame(new Game(gameID));
		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(gameID);
		return gameIdentifier;

		// note you will need to include additional logic, e.g., additional classes
		// which create, store, validate, etc. exchanged data
	}

	// example for a POST endpoint based on games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
		
		
		
		gameController.checkIfGameExists(gameID.getUniqueGameID());
		
		
		
		Player player = new Player(playerRegistration.getStudentFirstName(), playerRegistration.getStudentLastName(), playerRegistration.getStudentID(), newPlayerID.toString());
		
		System.out.println(newPlayerID.toString());

		
		gameController.getGame(gameID.getUniqueGameID()).addPlayer(player);
		
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		
		System.out.println(player.getUniquePlayerID());
		
		return playerIDMessage;

		// note you will need to include additional logic, e.g., additional classes
		// which create, store, validate, etc. exchanged data
	}
	
	
	
	// example for a POST endpoint based on games/{gameID}/players
		@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
		public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> mapExchange (
				@Validated @PathVariable UniqueGameIdentifier gameID,
				@Validated @RequestBody HalfMap halfMap) {
			UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
			
			return null;
			
			
			
			
			
		}
	
	
	
	

	/*
	 * Note, this is only the most basic way of handling exceptions in spring (but
	 * sufficient for our task) it would for example struggle if you use multiple
	 * controllers. Add the exception types to the @ExceptionHandler which your
	 * exception handling should support, the superclass catches subclasses aspect
	 * of try/catch applies also here. Hence, we recommend to simply extend your own
	 * Exceptions from the GenericExampleException. For larger projects one would
	 * most likely want to use the HandlerExceptionResolver, see here
	 * https://www.baeldung.com/exception-handling-for-rest-with-spring
	 * 
	 * Ask yourself: Why is handling the exceptions in a different method than the
	 * endpoint methods a good solution?
	 */
	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());

		// reply with 200 OK as defined in the network documentation
		// Side note: We only do this here for simplicity reasons. For future projects,
		// you should check out HTTP status codes and
		// what they can be used for. Note, the WebClient used on the client can react
		// to them using the .onStatus(...) method.
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}

package server.main.contorllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import server.main.exceptions.MapValidationException;
import server.main.model.Map;
import server.main.validators.CastleValidator;
import server.main.validators.EdgeValidator;
import server.main.validators.IMapValidator;
import server.main.validators.IslandValidator;
import server.main.validators.SizeValidator;


/**
 * ValidatorController
 * 
 * This is a controller that uses instances that extend IMapValidator. 
 * Each in its own way validates certain cases. More details in the validators. 
 * The controller serves to run all validators and to determine if the map is correct. 
 * 
 * Tasks: 
 * 1. Validate size of the map
 * 2. Validate my castle position
 * 3. Validate the borders/edges
 * 4. Validate the existence of the island
 * 
 * Using EdgeValidator, CastleValidator, IslandValidator & SizeValidator instances to process tasks
 * @author Veljko Radunovic 01528243
 */
@Component
public class ValidatorController implements IMapValidator {
	
	/**
	 * defining class logger
	 */
	static Logger logger = LoggerFactory.getLogger(ValidatorController.class);


	private List<IMapValidator> validators;

	public ValidatorController() {
		validators = new ArrayList<IMapValidator>();
		validators.add(new EdgeValidator());
		validators.add(new CastleValidator());
		validators.add(new SizeValidator());
		validators.add(new IslandValidator());
	}
	
	
	
	/**
	 * 
	 * 
	 * This is a controller that uses instances that extend IMapValidator. 
	 * Each in its own way validates certain cases. More details in the validators. 
	 * The controller serves to run all validators and to determine if the map is correct or not. 
	 * 
	 * Tasks: 
	 * 1. Validate size of the map
	 * 2. Validate my castle position
	 * 3. Validate the borders/edges
	 * 4. Validate the existence of the island
	 * 
	 * Using EdgeValidator, CastleValidator, IslandValidator & SizeValidator instances to process tasks
	 * @param Map map
	 * @author Veljko Radunovic 01528243
	 * 
	 */
	@Override
	public void validateMap(Map map) {
		logger.info("validating map");
				
		for(IMapValidator i : validators)
			i.validateMap(map);
	}
}

package server.main.validators;

import server.main.model.Map;

/**
 * IMapValidator
 * This is an interface that has a validate method. 
 * Different validators implement this interface to validate the Map in different ways. 
 * To find out whether it is correct or not.
 *
 * @author Veljko Radunovic 01528243
 */
public interface IMapValidator {

	public void validateMap(Map map);

}

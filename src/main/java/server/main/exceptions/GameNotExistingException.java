package server.main.exceptions;

public class GameNotExistingException extends GenericExampleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameNotExistingException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
		
	}

}

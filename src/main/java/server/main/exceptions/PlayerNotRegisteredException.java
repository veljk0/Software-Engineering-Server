package server.main.exceptions;

public class PlayerNotRegisteredException extends GenericExampleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerNotRegisteredException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}

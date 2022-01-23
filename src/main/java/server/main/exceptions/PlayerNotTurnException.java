package server.main.exceptions;

public class PlayerNotTurnException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerNotTurnException(String errorName, String errorMessage) {
		super(errorMessage);
	}
}

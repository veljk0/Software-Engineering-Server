package server.main.model;

import server.main.enumeration.PlayerGameState;
import server.main.enumeration.PlayerNumber;

public class Player {
	private String uniquePlayerID;
	private String firstName;
	private String lastName;
	private String studentID;
	private PlayerGameState playerGameState;
    private boolean collectedTreasure;
	private PlayerNumber playerNumber;
	private Map halfMap;
	
    public Player(String firstName, String lastName, String studentID, String uniquePlayerID) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentID = studentID;
		this.uniquePlayerID = uniquePlayerID;
		this.halfMap = new Map();
		this.playerGameState = PlayerGameState.MustWait;
		this.collectedTreasure = false;
	}

	

	public String getUniquePlayerID() {
		return uniquePlayerID;
	}

	public void setUniquePlayerID(String uniquePlayerID) {
		this.uniquePlayerID = uniquePlayerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public PlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	public void setPlayerGameState(PlayerGameState playerGameState) {
		this.playerGameState = playerGameState;
	}

	public boolean isCollectedTreasure() {
		return collectedTreasure;
	}

	public void setCollectedTreasure(boolean collectedTreasure) {
		this.collectedTreasure = collectedTreasure;
	}
	
	public void setPlayerNumber(PlayerNumber playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	public PlayerNumber getPlayerNumber() {
		return playerNumber;
	}

	public Map getHalfMap() {
		return halfMap;
	}

	public void setHalfMap(Map halfMap) {
		this.halfMap = halfMap;
	}
	
	public boolean isHalfMap() {
		return this.halfMap.getNodes().size() == 32;
	}

	@Override
	public String toString() {
		return "Player [uniquePlayerID=" + uniquePlayerID + "playerGameState=" + playerGameState + "]";
	}
	
	
}

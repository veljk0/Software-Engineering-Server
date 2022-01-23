package server.main.model;

import server.main.enumeration.FortState;
import server.main.enumeration.PlayerPositionState;
import server.main.enumeration.Terrain;
import server.main.enumeration.TreasureState;

/**
 * MapNode
 * Objects of this class represent map fields in this game. 
 * The field has its coordinates, its type of terrain, the presence of the castle, the presence of gold and the position of the players.
 * It overrides the equals method for easier field comaprison.
 * @author Veljko Radunovic 01528243
 */

public class MapNode {
	private Coordinate coordinate;
	private Terrain terrain;
	private FortState fortState;
	private TreasureState treasureState;
	private PlayerPositionState playerPositionState;

	public MapNode() {
		this.treasureState = TreasureState.NoOrUnknownTreasureState;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
	}

	public MapNode(Coordinate coordinate, Terrain fieldType, FortState fortState) {
		this.coordinate = coordinate;
		this.terrain = fieldType;
		this.fortState = fortState;
		this.playerPositionState = PlayerPositionState.NoPlayerPresent;
		this.treasureState = TreasureState.NoOrUnknownTreasureState;
	}
	
	public void updateCoordinateX(int n) {
		this.coordinate.setX(this.coordinate.getX() + n);
	}
	
	public void updateCoordinateY(int n) {
		this.coordinate.setY(this.coordinate.getY() + n);
	}

	/**
	 * 
	 * Getters & Setters
	 * 
	*/
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Terrain getFieldType() {
		return terrain;
	}

	public void setFieldType(Terrain fieldType) {
		this.terrain = fieldType;
	}

	public FortState getFortState() {
		return fortState;
	}

	public void setFortState(FortState fortState) {
		this.fortState = fortState;
	}

	public TreasureState getTreasureState() {
		return treasureState;
	}

	public void setTreasureState(TreasureState treasureState) {
		this.treasureState = treasureState;
	}

	public PlayerPositionState getPlayerPositionState() {
		return playerPositionState;
	}

	public void setPlayerPositionState(PlayerPositionState playerPositionState) {
		this.playerPositionState = playerPositionState;
	}

	@Override
	public String toString() {
		return "X = " + this.coordinate.getY() + ", " + "Y = " + this.coordinate.getX();
	}

	@Override
	public boolean equals(Object o) {

		if (o == this) {
			return true;
		}

		if (!(o instanceof MapNode)) {
			return false;
		}

		MapNode c = (MapNode) o;
		return this.coordinate.equals(c.getCoordinate());
	}

}

package server.main.model;

/**
 * Coordinate
 * The coordinates contain the positions of x and y. 
 * It is assigned to the MapNode, which is later also used as a key value for the HashMap, which contains all the nodes, ie the Map.
 * 
 * @author Veljko Radunovic 01528243
 */

public class Coordinate {
	private int x;
	private int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * Getters & Setters
	 * 
	*/

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

	
	/**
	 * 
	 * Coordinate overrides an equals method for accurate comparison of objects using the hashing principle.
	 * 
	 */
	@Override
	public boolean equals(Object o) {

		if (o == this) 
			return true;
		
		if (!(o instanceof Coordinate)) 
			return false;
		
		Coordinate c = (Coordinate) o;

		return Integer.valueOf(x) == Integer.valueOf(c.getX()) && Integer.valueOf(y) == Integer.valueOf(c.getY());
	}

}

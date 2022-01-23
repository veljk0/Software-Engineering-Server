package server.main.enumeration;

/**
 * @author Veljko Radunovic 01528243
 * FullMapType enumeration
 */

import java.util.Random;

public enum FullMapType {
	VERTICAL, HORIZONTAL;
	
	
	/**
	 * Random generator for the map type. 
	 * Map can be vertical or horizontal 
	 * @author Veljko Radunovic 01528243
	 * @return FullMapType
	 */
	public static FullMapType randomMapType() {
		int random = new Random().nextInt(FullMapType.values().length);
		return FullMapType.values()[random];
	}
}


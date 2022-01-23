package server.main.enums;

import java.util.Random;

public enum FullMapType {
	VERTICAL, HORIZONTAL;
	
	public static FullMapType randomMapType() {
		int random = new Random().nextInt(FullMapType.values().length);
		return FullMapType.values()[random];
	}
}


package server.main.generators;

import java.util.Random;


public class Generator {

	public static String generateID() {
		
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		String gameID = "";
		Random random = new Random();
		for(int i = 0; i < 5; ++i) {
			int randomRangeNumber = random.nextInt(s.length());
			gameID += s.charAt(randomRangeNumber);
		}
		
		return gameID;
	}

}

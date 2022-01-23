package server.main.generators;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Generator {
	static Logger logger = LoggerFactory.getLogger(Generator.class);
	
	public static String generateID() {
		
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		String gameID = "";
		Random random = new Random();
		for(int i = 0; i < 5; ++i) {
			int randomRangeNumber = random.nextInt(s.length());
			gameID += s.charAt(randomRangeNumber);
		}
		
		logger.info("GENERATOR: new id for the game has been generated");
		return gameID;
	}

}

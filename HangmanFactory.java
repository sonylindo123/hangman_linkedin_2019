package mari.mvp3;

import java.io.IOException;


/**
 * Returns objects of HangmanGame class and returns the implementation of HangmanWordMode or HangmanPhraseMode without exposing the logic to the calling method. 
 * @author marianahu
 *
 */
public class HangmanFactory {

	/**
	 * Default constructor
	 */
	public HangmanFactory() {
	}

	/***
	 * Creates objects of HangmanGame class and returns the implementation of HangmanWordMode or HangmanPhraseMode
	 * depending on the play mode chosen Returns null if the value of gameMode is invaild.
	 * @param is the String of the game mode selected
	 * @throws LeaderboardException (thrown by the HangmanWordMode or HangmanPhraseMode constructors) up the call stack
	 */
	
	public HangmanGame createGame(String gameMode) throws LeaderboardException {
		switch (gameMode.toLowerCase()) {
		case "words":
			return new HangmanWordMode();
		case "phrases":
			return new HangmanPhraseMode();
		default:
			return null;
		}

	}
}
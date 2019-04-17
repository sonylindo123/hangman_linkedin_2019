package mari.mvp3;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This class extends abstract class HangmanGame and overrides certain methods to adapt it to the "Guess word" play mode
 * In addition to the HangmanGame attributes, this class also contains the constant attributes for the leaderboard file name and the URL containing the dictionary
 * @author marianahu
 *
 */
public class HangmanWordMode extends HangmanGame {
/** 
 * Defines constants for the filename where leaderboard results are persisted and retrieved from, and the url containing the dictionary of words
 */
	public final static String filename_word_lb = "word_LB.ser";
	public final static String dictionaryUrl = "http://app.linkedin-reach.io/words?";

	/**
	 * The HangmanWordMode constructor calls a method from Leaderboard to read the leaderboard results from file for games played on "guess words" mode
	 */
	public HangmanWordMode() throws LeaderboardException  {
		super();
		try {
		Leaderboard.readLeaderboardResultsFromFile(filename_word_lb);
		} catch(EOFException e) { // If the file is currently empty, skip reading from it and return
			return;
		}catch(IOException | ClassNotFoundException e) { 
			throw new LeaderboardException("There was a problem loading the leader board. The application will now exit.");
		} 	
		}

	/**
	 * Overriding abstract method to get a list of words. This implementation reads words from the LinkedIn provided API.
	 */
	@Override
	protected ArrayList<String> getListOfStrings() throws WordListException  {
		String url = dictionaryUrl + "difficulty=" + this.difficulty;
		ArrayList<String> wordsFromDictionary = new ArrayList<>();
		HttpURLConnection con = null;
		try {
		URL obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			wordsFromDictionary.add(inputLine.toLowerCase());
		}
		in.close();
		} catch(IOException e) {
			throw new WordListException("There was a problem connecting and/or reading list of words from the API");
		}
		return wordsFromDictionary;
	}
	
/**
 * Overriding abstract method to calculate and save scores at the end of a game.
 * Score calculation is based on difficulty level.
 * Player name and score are saved to the leader board
 * @return score
 */
	@Override
	protected int calculateAndSaveScore() {
		int score = 0;
		int dif = this.difficulty;
		if (!this.didPlayerWin) {
			score = 0;
		} else {
			switch (dif) {
			case 1:
				score = 1;
				break;
			case 2:
				score = 2;
				break;
			case 3:
				score = 3;
				break;
			case 4:
				score = 4;
				break;
			case 5:
				score = 5;
				break;
			case 6:
				score = 6;
				break;
			case 7:
				score = 7;
				break;
			case 8:
				score = 8;
				break;
			case 9:
				score = 9;
				break;
			case 10:
				score = 10;
				break;
			}
		}
		Leaderboard.addResultToLeaderboard(this.name, score);
		return score;
	}

	/**
	 * Overriding abstract method to save leader board results to file containing only results for games played on "Guess word" mode
	 */
	@Override
	protected void saveScoresToFile() {
		Leaderboard.writeLeaderboardResultsToFile(filename_word_lb);
	}

	/** 
	 * Overriding method to replace underscores with the successfully guessed word.
	 */
	@Override
	protected void revealGuessedWord(String guess) {
		  // If player guessed correctly the entire word instead of one letter, update the hidden secret word to the guess
			 this.hiddenSecretString = new StringBuilder(guess);
	}

	/**
	 * Overriding method to check whether a guessed word matches the secret word. 
	 * @return boolean true if the word was guessed correctly; returns false otherwise
	 */
	@Override
	protected boolean isWordGuessCorrect(String guess) {
		 // If player guessed a word (not a single letter), check whether the guess matches the secret word
		 return this.secretString.equalsIgnoreCase(guess);
	}
}
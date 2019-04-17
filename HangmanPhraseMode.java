package mari.mvp3;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * This class extends abstract class HangmanGame and overrides certain methods to adapt it to the "Guess phrase" play mode
 * In addition to the HangmanGame attributes, this class also contains the constant attributes for the leaderboard file name and the text files containing 
 * the list of easy and difficult phrases
 * @author marianahu
 *
 */

public class HangmanPhraseMode extends HangmanGame {

	/** 
	 * Defines constants for the filename where leaderboard results are persisted and retrieved from, and the text files containing 
	 * the list of easy and difficult phrases
	 * */
	
	public final static String filename_phrase_lb = "Phrase_LB.ser";
	public final static String filename_difficult_phrases = "hangman_phrases_difficult.txt";
	public final static String filename_easy_phrases = "hangman_phrases_easy.txt";

	/**
	 * The HangmanPhraseMode constructor calls a method from Leaderboard to read the leaderboard results from file for games played on "guess phrases" mode
	 */
	public HangmanPhraseMode() throws LeaderboardException  {
		super();
		try {
		Leaderboard.readLeaderboardResultsFromFile(filename_phrase_lb);
		} catch(EOFException e) { // If the file is currently empty, skip reading from it and return
			return;
		}catch(IOException | ClassNotFoundException e) { 
			throw new LeaderboardException("There was a problem loading the leader board. The application will now exit.");
		} 	
		}
	
	/**
	 * Overriding abstract method to get a list of phrases from text file.
	 */
	@Override
	protected ArrayList<String> getListOfStrings() throws FileNotFoundException  {
		ArrayList<String> phrases = new ArrayList<>();
		File file = new File("");
		if (this.difficulty > 6) {
			file = new File(filename_difficult_phrases);
		} else {
			file = new File(filename_easy_phrases);
		}
		
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			phrases.add((scanner.nextLine().toLowerCase()));
		}
		return phrases;
	}
	

	/**
	 * Overriding abstract method to calculate and save scores at the end of a game.
	 * Score calculation is based on length of secret phrase guessed.
	 * Player name and score are saved to the leaderboard
	 * @return score
	 */
	@Override
	protected int calculateAndSaveScore() {
		int score = 0;
		String[] words = this.secretString.split("\\s+"); // one or more whitespaces
		int phraseLen = words.length;

		if (!this.didPlayerWin) {
			score = 0;
		} else {
			score = phraseLen * 5;
		}
		Leaderboard.addResultToLeaderboard(this.name, score);
		return score;
	}

	/**
	 * Overriding abstract method to save leader board results to file containing only results for games played on "Guess phrase" mode
	 */
	@Override
	protected void saveScoresToFile() {
		this.lb.writeLeaderboardResultsToFile(filename_phrase_lb);
	}

	/** 
	 * Overriding method to replace underscores with the successfully guessed word.
	 * Splits up the secret string into individual words and checks whether any of its words matches the word guessed
	 * If there is a match, update the word in the hidden secret phrase
	 */
	@Override
	protected void revealGuessedWord(String guess) {
			String[] wordToGuessSplit = this.hiddenSecretString.toString().split("\\s");
			String[] secretStringSplit = this.secretString.split("\\s+");
			int numberOfWords = secretStringSplit.length;
			for (int i = 0; i < numberOfWords; i++) {
				if (secretStringSplit[i].equalsIgnoreCase(guess)) {
					wordToGuessSplit[i] = guess;
				}
			}
			StringBuilder newWordToGuess = new StringBuilder();
			for (int j = 0; j < numberOfWords; j++) {
				newWordToGuess = newWordToGuess.append(wordToGuessSplit[j] + " ");
			}
			hiddenSecretString = newWordToGuess;
	}

	/**
	 * Overriding method to check whether a guessed word is present in the secret phrase. 
	 * @return boolean true if the word was guessed correctly; returns false otherwise
	 */
	@Override
	protected boolean isWordGuessCorrect(String guess) {
		String[] words = this.secretString.split("\\s+");
		return Arrays.asList(words).contains(guess);
	}

}

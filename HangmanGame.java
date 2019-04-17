package mari.mvp3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

public abstract class HangmanGame {

	/**
	 * An abstract class that models a general hangman game by defining attributes to manage game state (e.g. attempts left, incorrect guesses) 
	 * and methods to process data according to game rules (e.g. check if a guess is correct, check if player ran out of attempts). 
	 * It contains both abstract and concrete methods.
	 */
	protected String name; // Player name
	protected ArrayList<String> listOfStrings; // List containing secret strings the program chooses from
	int difficulty; // The difficulty level selected by player
	protected String secretString; // The secret word selected by the program from the listOfStrings
	protected int score; // The score calculated upon winning or losing a game
	public StringBuilder hiddenSecretString; // The hidden secret word revealing correct guesses and hiding unknown letters with underscores. Uses StringBuilder vs String to allow easier and more efficient modification of String objects
	public int attemptsLeft; // Keeps track of attempts left
	public ArrayList<String> incorrectGuesses; // Keeps track of incorrect guesses made by player. ArrayList allows automatic size expansion as new elements are added
	public ArrayList<String> correctGuesses; // Keeps track of correct guesses made by player
	protected boolean didPlayerWin; // Keeps track of whether player has won
	protected static Leaderboard lb = new Leaderboard(); // A static attribute that stores player names and scores from past games
	protected final static int MAX_ATTEMPTS = 6; // Maximum allowed incorrect attempts set as a constant
	
	
	protected HangmanGame()  {
	}
	
	/**
	 * Sets player name and game configuration parameters for difficulty level. It also retrieves the list of strings available to play the game
	 * @param name is the player's name
	 * @param difficulty is the difficulty level
	 * @throws Exception
	 */
	protected void configure(String name, int difficulty) throws WordListException, FileNotFoundException   {
		this.name = name;
		this.difficulty = difficulty;
		this.listOfStrings = getListOfStrings();
	}
	
	/** 
	 * Abstract method that obtains the list of strings available to play the game
	 * @return an ArrayList of Strings containing available words or phrases to play the game
	 * @throws Exception
	 */
	abstract protected ArrayList<String> getListOfStrings() throws WordListException, FileNotFoundException ;
	
	/** 
	 * Initializes game state parameters to start a new game from scratch. 
	 * Selects a random secret string, creates the hidden secret string by replacing letters with underscores, sets attempts left to the maximum number allowed,
	 * clears the list of incorrect and correct guesses, resets the didPlayerWin boolean to false, and resets the score to 0
	 */
	public void resetGameStatus() {
		this.secretString = chooseSecretString().toLowerCase(); 
		/**
		 * Creates the hidden secret string by replacing all characters specified in the regex with underscores: 
		 * letters from A to Z (lower and upper case), and numbers from 0 to 9
		 */
		this.hiddenSecretString= new StringBuilder(this.secretString.replaceAll("[a-zA-Z0-9]", "_")); 
		this.attemptsLeft = MAX_ATTEMPTS;
		this.incorrectGuesses = new ArrayList<>();
		this.correctGuesses = new ArrayList<>();
		this.didPlayerWin = false;
		this.score = 0;
	}
	
	/**
	 * Randomly chooses a secret string from the listOfStrings
	 * @return a randomly selected secret string
	 */
	 protected String chooseSecretString() { 
		Random random = new Random();
		int randonNumber = random.nextInt(this.listOfStrings.size());
		return this.listOfStrings.get(randonNumber);
	}
	
	/** 
	 * Evaluates whether a guess has been repeated by checking in the correctGuesses and incorrectGuesses lists
	 * @param guess is the player's guess of a letter or word
	 * @return boolean true if guess is repeated; false if not repeated
	 */
	protected boolean isGuessRepeated(String guess) {
		if(this.correctGuesses.contains(guess) || this.incorrectGuesses.contains(guess)) {
			return true;
		}
		else return false;
	}
	
	/**
	 * Checks whether the guess is correct. If guessing only one letter, the same method is shared for all subclasses;
	 * If guessing words instead of letters, the method will vary for word mode and phrase mode
	 */
	 protected boolean isGuessCorrect(String guess) {
		 if(guess.length() == 1) { 
			 return this.secretString.contains(guess);	 
		 }
		 else return isWordGuessCorrect(guess);
		 }

	/**
	 * Abstract method to evaluate whether a guess is correct
	 * @return boolean true if guess is correct; false is guess is incorrect
	 */
	protected abstract boolean isWordGuessCorrect(String guess);

	/**
	 * Reveals all occurrences of a successful guess by replacing underscores with the guessed letter or word. 
	 * The implementation of the method to replace underscores with a guessed letter is shown below.
	 * The method to replace underscores with a guessed word is abstract and therefore not shown below
	 */
	protected void revealGuessedString(String guess) {
	if(guess.length() == 1){
		for (int i = 0; i < this.secretString.length(); i++) { 
			if (this.secretString.substring(i, i + 1).equalsIgnoreCase(guess)) {
				this.hiddenSecretString.replace(i, i + 1, guess);
			}
		}}
	else revealGuessedWord(guess);
	}

/**
 * Abstract method for replacing underscores with the guessed word
 */
	protected abstract void revealGuessedWord(String guess);

	protected boolean checkForWin() {
		if (this.hiddenSecretString.indexOf("_") == -1) {
			this.didPlayerWin = true;
		} else {
			this.didPlayerWin =  false;
	}
	return this.didPlayerWin;
	}
	
	/**
	 * Abstract method for calculating and saving the score at the end of a game
	 * @return the score
	 */
	abstract protected int calculateAndSaveScore();
	
	
/**
 * Abstract method for saving the leader board scores to a file
 */
	abstract protected void saveScoresToFile();

	/**
	 * Sorts the leader board results in descending order and returns the leader board
	 * @return the leader board
	 */
	protected Leaderboard getLeaderboard() {
		 Leaderboard.sortByScore();;
		 return lb;
	}
	
	/**
	 * Records an incorrect guess by saving it into an ArrayList of Strings
	 */
	protected void addIncorrectGuess(String guess) {
this.incorrectGuesses.add(guess);		
	}

	/**
	 * Records a correct guess by saving it into an ArrayList of Strings
	 */
	protected void addCorrectGuess(String guess) {
this.correctGuesses.add(guess);		
	}
	
	
	protected StringBuilder getWordToGuess() {
		return this.hiddenSecretString;
	}
	
	protected ArrayList<String> getIncorrectGuesses() {
		return this.incorrectGuesses;
	}
	
	protected int getAttemptsLeft() {
		return this.attemptsLeft;
	}
	
	protected void decreaseAttemptByOne() {
		this.attemptsLeft--;
	}
	

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected int getScore() {
		return score;
	}

	protected void setScore(int score) {
		this.score = score;
	}

	protected boolean getDidPlayerWin() {
		return didPlayerWin;
	}

	protected void setDidPlayerWin(boolean didPlayerWin) {
		this.didPlayerWin = didPlayerWin;
	}

	protected static Leaderboard getLb() {
		return lb;
	}

	protected static void setLb(Leaderboard lb) {
		HangmanGame.lb = lb;
	}

	protected static int getMaxAttempts() {
		return MAX_ATTEMPTS;
	}

	protected void setListOfWords(ArrayList<String> listOfStrings) {
		this.listOfStrings = listOfStrings;
	}

	protected String getSecretWord()
	{
		return this.secretString;
	}

}


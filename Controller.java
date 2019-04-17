package mari.mvp3; 

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * It is an intermediary between the View and the HangmanGame Model.
 * It implements the business logic for the view (action listeners). 
 * It receives data from View and HangmanGame and manipulates both to produce
 * an appropriate response.
 * 
 * @author marianahu
 *
 */
public class Controller {

	private View view; 
	private HangmanGame game; 

	/**
	 * Defines constant Strings representing the key of the player preferences hash
	 * map
	 */
	public static final String HM_KEY_MODE = "mode";
	public static final String HM_KEY_DIFFICULTY = "difficulty";
	public static final String HM_KEY_NAME = "name";

	/**
	 * Constructor that takes a View object representing the GUI as argument and
	 * sets it to the view attribute. 
	 * Note that the game attribute is not created in this constructor. This is because
	 * the creation of a HangmanGame requires first obtaining user 
	 * preferences (play mode, difficulty level) via the View
	 * 
	 * @param view is the View object
	 */
	public Controller(View view) {
		this.view = view;  
	}

	/**
	 * Main method that creates a Controller object with view and game attributes,
	 * implements the View business logic (action listeners), and prepares game and 
	 * view for a new game
	 * @param args
	 * @throws LeaderboardException 
	 * @throws WordListException
	 * @throws FileNotFoundException
	 */
	public static void main(String... args) throws LeaderboardException, WordListException, FileNotFoundException {
		View view = null;
		try {
			view = new View();
		} catch (Exception e) {
			System.out.println("There was a problem creating the GUI");
			System.exit(0);
		}
		Controller controller = new Controller(view);
		Map<String, String> preferences = controller.getPlayerPreferences();
		HangmanGame game = null;
		try {
			game = controller.createHangmanGame(preferences);
		} catch (LeaderboardException e) {
			view.showErrorMsg(e.getMessage());
			System.exit(0);
		} catch (WordListException e) {
			view.showErrorMsg(e.getMessage());
			System.exit(0);
		} catch (FileNotFoundException e) {
			view.showErrorMsg("File containing list of phrases was not found");
			System.exit(0);
		}
		controller.setHangmanGame(game);
		controller.setUpViewEvents();
		controller.resetGameParameters();
	}

	/**
	 * Obtains the player name and game preferences upon detecting that player has
	 * submitted these entries. Game preferences are: game mode ("guess words" or
	 * "guess phrases"), and difficulty level (1-10). All fields are mandatory, so
	 * as long as one of them is left empty, the view will continue prompting the
	 * player to enter all fields
	 * @return preferences, a hash map containing player selected name, play mode, and difficulty level
	 */
	public Map<String, String> getPlayerPreferences() {
		String mode = null;
		String difficulty = null;
		String name = null;
		Map<String, String> preferences = new HashMap<String, String>();

		boolean didPlayerSubmitPreferences = view.didPlayerSubmitPreferences();

		if (didPlayerSubmitPreferences) {
			mode = view.getGameMode();
			difficulty = view.getDifficulty();
			name = view.getName();
			while (mode.isEmpty() || name.isEmpty() || difficulty.isEmpty()) {
				view.showErrorMsg("All fields are required");
				didPlayerSubmitPreferences = view.didPlayerSubmitPreferences();
				mode = view.getGameMode();
				difficulty = view.getDifficulty();
				name = view.getName();
			}
			preferences.put(HM_KEY_MODE, mode);
			preferences.put(HM_KEY_DIFFICULTY, difficulty);
			preferences.put(HM_KEY_NAME, name);

		} else if (!didPlayerSubmitPreferences) {
			System.exit(0);
		}
		// Main game window is shown after player has finished entering preferences
		view.showGameWindow();
		return preferences;
	}

	/**
	 * First creates an instance of HangmanFactory which returns a HangmanGame object with
	 * the implementation of HangmanWordMode or HangmanPhraseMode depending on the game
	 * mode chosen. Then, configures the game with player name and difficulty level.
	 * @rturn game, the instance of HangmanGame object created
	 */
	private HangmanGame createHangmanGame(Map<String, String> preferences)
			throws LeaderboardException, WordListException, FileNotFoundException {
		HangmanFactory factory = new HangmanFactory();
		HangmanGame game = factory.createGame(preferences.get(HM_KEY_MODE));
		game.configure(preferences.get(HM_KEY_NAME), Integer.parseInt(preferences.get(HM_KEY_DIFFICULTY)));
		return game;
	}

	// Setter for Controller's class game attribute
	public void setHangmanGame(HangmanGame game) {
		this.game = game;
	}

	/**
	 * Resets all game state parameters (secret word, attempts left, incorrect guesses,
	 * etc) to start a new game with the initially selected game mode and difficulty
	 * level. This method is called 1) the first time the game is launched and 2)
	 * every time the player chooses to play Again
	 */
	public void resetGameParameters() {
		Leaderboard lb = game.getLeaderboard();
		view.showLeaderboard(lb);
		game.resetGameStatus();
		StringBuilder wordToGuess = game.getWordToGuess();
		int attemptsLeft = game.getAttemptsLeft();
		String secretString = game.getSecretWord();
		view.resetGameParameters(secretString, wordToGuess, attemptsLeft);
	}

	/**
	 * Implements the action listeners of the view controls to define response to player's
	 * interaction with View. 
	 */
	private void setUpViewEvents() {
		/**
		* Upon detecting a click on the Guess button, controller manipulates view and game to
		* to produce the appropriate the appropriate response
		*/
		view.getGuessButton().addActionListener(e -> {
			int attemptsLeft = game.getAttemptsLeft();
			// If a guess is submitted when the game is already won or lost, view displays error message and method returns; 
			if (attemptsLeft == 0) {
				view.showErrorMsg("You ran out of attempts on this game. Start new game to continue playing!");
				return;
			}
			if (game.checkForWin()) {
				view.showErrorMsg("You already won this game. Start new game to continue playing!");
				return;
			}
			
			// Obtains the guess String from the view
			String guess = view.getGuess();
			view.clearGuessTextField();
			view.focusOnGuessTextField();

			// If the guess is an empty guess or a special character, view displays error message and method returns; 
			if (guess.equals("")) {
				view.showErrorMsg("You did not enter any guess. Try again!");
				return;
			}
			boolean isInvalidGuess = guess.matches("[^A-Za-z0-9]");
			if (isInvalidGuess) {
				view.showErrorMsg("You entered an invalid character. Only letters and numbers are allowed. Try again!");
				return;
			}

			// If guess is repeated guess, method returns; 
			boolean isGuessRepeated = game.isGuessRepeated(guess);
			if (isGuessRepeated) {
				view.setFeedback("You repeated your guess");
				return;
			}

			boolean isGuessCorrect = game.isGuessCorrect(guess);
			// If guess is correct, game updates secret word and view reflects this update;
			if (isGuessCorrect) {
				game.addCorrectGuess(guess);
				game.revealGuessedString(guess);
				StringBuilder wordToGuess = game.getWordToGuess();
				view.runCorrectGuessRoutine(wordToGuess);
			} else { // If guess is incorrect, game reduces attempts left and records incorrect guesses, and view reflects these updates 
				game.decreaseAttemptByOne();
				attemptsLeft = game.getAttemptsLeft();
				game.addIncorrectGuess(guess);
				ArrayList<String> incorrectGuesses = game.getIncorrectGuesses();
				view.runIncorrectGuessRoutine(attemptsLeft, guess);
			}
			
			// If game is lost or won, a score is calculated and saved, the updated leaderboard is displayed, 
			boolean didPlayerWin = game.checkForWin();
			if (attemptsLeft == 0 || didPlayerWin) {
				int score = game.calculateAndSaveScore();
				view.setScore(score);

				Leaderboard lb = game.getLeaderboard();
				view.showLeaderboard(lb);
				
			// Player is asked whether or not to play again
				boolean playAgain = true;
				if (attemptsLeft == 0) {
					String secretWord = game.getSecretWord();
					view.setSecretTextArea(secretWord);
					playAgain = view.getPlayAgain("lost", Integer.toString(score));
				} else if (didPlayerWin) {
					playAgain = view.getPlayAgain("won", Integer.toString(score));
				}
				// If player selects to play again, game parameters are reset and view gets updated to start new game. 
				// Otherwise, the method returns
				if (playAgain) {
					resetGameParameters();
				} else if (!playAgain)
					return;
			}
		});

		/**
		* Upon detecting a click on the Quit button, displays pop-up window requesting confirmation to quit.
		* If player confirms to quit, scores are saved to file for persistence, and application exits; Otherwise, it returns
		*/
		view.getQuitGameButton().addActionListener(e -> {
			if (view.confirmQuitGame()) {
				game.saveScoresToFile();
				System.exit(0);
			} else
				return;
		});

		/**
* Upon detecting a click on the New Game button, scores are saved to file and the player is prompted to enter name
* and game preferences to start a new game. A HangmanGame object is created and configured with the preferences.
* The game state parameters and view are reset to begin a new game.
*/	
		view.getNewGameButton().addActionListener(e -> {
				game.saveScoresToFile();
				Map<String, String> preferences = getPlayerPreferences();// Hash map stores player's name, play mode, and difficulty level
				HangmanGame game = null;
				try {
					/**Creates a HangmanFactory object which returns a HangmanGame object with the 
					 * implementation of HangmanWordMode or HangmanPhraseMode
					 */
					game = createHangmanGame(preferences);
				} catch (LeaderboardException e1) {
					view.showErrorMsg(e1.getMessage());
					System.exit(0);
				} catch (WordListException e1) {
					view.showErrorMsg(e1.getMessage());
					System.exit(0);
				} catch (FileNotFoundException e1) {
					view.showErrorMsg("File containing list of phrases was not found");
					System.exit(0);
				}
				setHangmanGame(game);
				resetGameParameters(); // game state parameters and view are reset to start new game
		});
		
		/**
		* Makes the secret string visible upon detecting a click on the Admin Mode button
		*/
		view.getAdminButton().addActionListener(e -> {
			view.showAdminView();
		});

		/**
		* Makes the secret string invisible upon detecting a click on the Player Mode button
		*/
		view.getPlayerButton().addActionListener(e -> {
			view.hideAdminView();
		});

		/**
		* Requests confirmation to exit the application upon detecting a click X button of the window
		* Exits application upon player confirmation.
		*/
		view.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (view.confirmQuitGame()) {
					game.saveScoresToFile();
					System.exit(0);
				} 
			}

		});

	}

	/**
	 * Getters and setters for Controller class' view and game attributes
	 * @return
	 */
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public HangmanGame getGame() {
		return game;
	}

	public void setGame(HangmanGame game) {
		this.game = game;
	}
}

package mari.mvp3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import javafx.scene.layout.Border;

/**
 * The View class provides the controls that display game data and feedback to
 * the player (secret word, attempts left, incorrect guesses, etc) and allows
 * player to interact with the game (selecting preferences, entering guesses,
 * starting new game, etc). This class is only concerned about displaying
 * controls and data that is passed to it. It does not perform any logic
 * functionalities. logic functionalities.
 * 
 * @author marianahu
 *
 */
public class View {
	private final JTextField guessTextField;
	private final JButton guessButton;
	private final JTextArea incorrectGuessesTextArea;
	private final JFrame frame;
	private final JLabel feedback;
	private JComboBox gameModes;
	private JComboBox gameDifficulty;
	private int preferenceSelected;
	private final JTextArea leaderboardArea;
	private JTextField nameField;
	private final int TITLE_FONT_SIZE = 12;
	private final String FONT = "Sans Serif";
	private final JButton newGameButton;
	private final JButton quitGameButton;
	private final JTextArea secretTextArea;
	private final JLabel scoreLabel;
	private final JLabel attemptsLeftTitle;
	private final JLabel attemptsLeftLabel;
	private final JProgressBar attemptsLeftProgress;
	private final JButton adminButton;
	private final JScrollPane adminScroll;
	private final JLabel adminViewTitle;
	private final JButton playerButton;
	private final JTextArea adminViewArea;

	/**
	 * Creates a frame and its components: Secret String, Menu, Game Status,
	 * Leaderboard, and Guess panels
	 * 
	 * @throws Exception if it fails to load the look and feel
	 */
	public View() throws Exception {
		/**
		 * Creates a frame that contains Panels for: Secret String, Menu, Game Status,
		 * Leaderboard, and Guess
		 */
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		this.frame = new JFrame();
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setTitle("Hangman Game");
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());

		javax.swing.border.Border blackline = BorderFactory.createLineBorder(Color.BLACK);

		/**
		 * The Secret String Panel displays brief instructions and the hidden secret
		 * string (represented with underscores and correct guesses)
		 */
		JPanel secretStringPanel = new JPanel();
		secretStringPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
		secretStringPanel.setBorder(blackline);
		Dimension dim = new Dimension(700, 130);
		secretStringPanel.setPreferredSize(dim);
		JLabel secretTitle = new JLabel("Guess the secret word or phrase. Guess one letter or one word at a time!");
		secretTitle.setFont(new Font(FONT, java.awt.Font.BOLD, 18));
		secretStringPanel.add(secretTitle);

		secretTextArea = new JTextArea();
		secretTextArea.setRows(3);
		secretTextArea.setColumns(45);
		secretTextArea.setFont(new java.awt.Font(FONT, java.awt.Font.PLAIN, 18));
		secretTextArea.setLineWrap(true);
		secretTextArea.setWrapStyleWord(true);
		secretTextArea.setOpaque(false);
		secretTextArea.setEditable(false);
		secretTextArea.setText("");
		JScrollPane secretScroll = new JScrollPane(secretTextArea);
		secretStringPanel.add(secretScroll);

		frame.add(secretStringPanel, BorderLayout.NORTH);

		/**
		 * The Menu Panel contains buttons: "New game" to start a new game with new
		 * player name and preferences; "Quit game" to exit the application; "Admin
		 * mode" to view the secret string; and "Player mode" to return to hide the
		 * secret string
		 */
		JPanel menuPanel = new JPanel();
		
		Dimension dim3 = new Dimension(150, 300);
		menuPanel.setPreferredSize(dim3);

		GridBagConstraints cm = new GridBagConstraints();
		cm.anchor = GridBagConstraints.CENTER;
		cm.insets = new Insets(10, 10, 10, 10);
		cm.ipadx = 5;
		cm.ipady = 5;
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBorder(blackline);

		cm.gridy = 0;
		cm.gridx = 0;
		newGameButton = new JButton("New Game");
		menuPanel.add(newGameButton, cm);

		cm.gridy = 1;
		cm.gridx = 0;
		quitGameButton = new JButton("Quit Game");
		menuPanel.add(quitGameButton, cm);

		cm.gridy = 2;
		cm.gridx = 0;
		adminButton = new JButton("Admin Mode");
		menuPanel.add(adminButton, cm);

		cm.gridy = 3;
		cm.gridx = 0;
		playerButton = new JButton("Player Mode");
		menuPanel.add(playerButton, cm);

		frame.add(menuPanel, BorderLayout.WEST);

		/**
		 * The Status Panel contains the game status parameters that get updated as the
		 * game progresses "Attempts left", "Score", and "Incorrect guesses". It also
		 * contains the visible secret string that is available on "Admin view"
		 */
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(blackline);
		//Color cblue = new Color(210, 226, 238);
		
		//statusPanel.setBackground(cblue);
		statusPanel.setSize(400, 100);
		statusPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10, 10, 10, 10);
		c.ipadx = 0;
		c.ipady = 0;

		c.gridx = 0;
		c.gridy = 0;
		attemptsLeftTitle = new JLabel("Attempts left");
		attemptsLeftTitle.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		statusPanel.add(attemptsLeftTitle, c);

		c.gridx = 1;
		c.gridy = 0;
		attemptsLeftProgress = new JProgressBar(0, 6);
		attemptsLeftProgress.setValue(6);
		attemptsLeftProgress.setString("attempts left");
		statusPanel.add(attemptsLeftProgress, c);
		
		c.gridx = 2;
		c.gridy = 0;
		attemptsLeftLabel = new JLabel("6");
		attemptsLeftLabel.setForeground(Color.orange.darker());
		attemptsLeftLabel.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, 18));
		statusPanel.add(attemptsLeftLabel, c);

		c.gridx = 0;
		c.gridy = 2;
		JLabel scoreTitle = new JLabel("Score");
		scoreTitle.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		statusPanel.add(scoreTitle, c);

		c.gridx = 1;
		c.gridy = 2;
		scoreLabel = new JLabel();
		scoreLabel.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		statusPanel.add(scoreLabel, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 2;
		JLabel incorrectGuessesTitle = new JLabel("Incorrect guesses");
		incorrectGuessesTitle.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		statusPanel.add(incorrectGuessesTitle, c);

		c.gridx = 1;
		c.gridy = 4;
		incorrectGuessesTextArea = new JTextArea();
		incorrectGuessesTextArea.setRows(2);
		incorrectGuessesTextArea.setColumns(10);
		incorrectGuessesTextArea.setFont(new java.awt.Font(FONT, java.awt.Font.PLAIN, 15));
		incorrectGuessesTextArea.setLineWrap(true);
		incorrectGuessesTextArea.setWrapStyleWord(true);
		incorrectGuessesTextArea.setEditable(false);
		Color grey = new Color(237, 238, 239);
		incorrectGuessesTextArea.setBackground(grey);
		JScrollPane incorrectGuessesScroll = new JScrollPane(incorrectGuessesTextArea);
		statusPanel.add(incorrectGuessesScroll, c);
		statusPanel.add(incorrectGuessesTextArea, c);

		c.gridx = 0;
		c.gridy = 6;
		adminViewTitle = new JLabel("Secret string");
		adminViewTitle.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		adminViewTitle.setVisible(false);
		statusPanel.add(adminViewTitle, c);

		c.gridx = 1;
		c.gridy = 6;
		adminViewArea = new JTextArea();
		adminViewArea.setRows(3);
		adminViewArea.setColumns(15);
		adminViewArea.setFont(new java.awt.Font(FONT, java.awt.Font.PLAIN, 10));
		adminViewArea.setLineWrap(true);
		adminViewArea.setWrapStyleWord(true);
		adminViewArea.setEditable(false);
		adminViewArea.setVisible(false);
		adminViewArea.setBackground(grey);
		adminScroll = new JScrollPane(adminViewArea);
		adminScroll.setVisible(false);
		statusPanel.add(adminScroll, c);
		frame.add(statusPanel, BorderLayout.CENTER);

		/**
		 * The LB (Leader board) Panel displays the results of the leader board in
		 * descending order
		 */
		JPanel lbPanel = new JPanel();
		Dimension lbdim = new Dimension(220, 290);
		lbPanel.setPreferredSize(lbdim);
		leaderboardArea = new JTextArea(14, 15);
		leaderboardArea.setText("Leaderboard \n\n" + "Name    " + "     Score\n\n");
		JScrollPane lbScroll = new JScrollPane(leaderboardArea);
		lbPanel.add(lbScroll);

		frame.add(lbPanel, BorderLayout.EAST);

		/**
		 * The Guess Panel allows the user to enter guesses in a text field and by
		 * clicking on the "Submit guess" button
		 */
		JPanel guessPanel = new JPanel();
		Dimension dim2 = new Dimension(700, 100);
		GridBagConstraints cg = new GridBagConstraints();
		guessPanel.setPreferredSize(dim2);
		cg.anchor = GridBagConstraints.CENTER;
		cg.insets = new Insets(5, 5, 5, 5);
		cg.ipadx = 20;
		cg.ipady = 10;
		guessPanel.setLayout(new GridBagLayout());
		guessPanel.setBorder(blackline);

		cg.gridy = 0;
		cg.gridx = 0;
		guessTextField = new JTextField(10);
		guessTextField.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, 20));
		guessPanel.add(guessTextField, cg);

		cg.gridy = 0;
		cg.gridx = 2;
		guessButton = new JButton("Submit guess!");
		Dimension dimButton = new Dimension(120, 30);
		
		javax.swing.border.Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		guessButton.setBorder(raisedetched);
		guessButton.setPreferredSize(dimButton);
		guessButton.setBorder(BorderFactory.createEmptyBorder());
		guessButton.setOpaque(true);
		guessButton.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, 15));
		guessPanel.add(guessButton, cg);

		cg.gridy = 1;
		cg.gridx = 0;
		feedback = new JLabel();
		Dimension dimf = new Dimension(200, 10);
		feedback.setPreferredSize(dimf);
		feedback.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, TITLE_FONT_SIZE));
		feedback.setText("You guessed right");
		guessPanel.add(feedback, cg);

		frame.add(guessPanel, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);

		frame.getRootPane().setDefaultButton(guessButton);
		frame.setVisible(false);
	}

	/**
	 * Shows a pop-up window prompting user to enter: name, game mode (word or
	 * phrases), and difficulty level (1-10). Reads the button clicked by the player
	 * on the pop-up window
	 * 
	 * @return boolean true if the player clicked the OK button; returns false if
	 *         the player clicked the Cancel button
	 */
	public boolean didPlayerSubmitPreferences() {
		String[] gameModeOptions = { "Words", "Phrases" };
		gameModes = new JComboBox(gameModeOptions);
		String[] difficultyOptions = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
		gameDifficulty = new JComboBox(difficultyOptions);
		nameField = new JTextField(10);
		JPanel preferencesPanel = new JPanel();
		preferencesPanel.add(new JLabel("Enter your name"));
		preferencesPanel.add(nameField);
		preferencesPanel.add(new JLabel("Guess words or phrases"));
		preferencesPanel.add(gameModes);
		preferencesPanel.add(Box.createHorizontalStrut(15)); // a spacer
		preferencesPanel.add(new JLabel("Difficulty level"));
		preferencesPanel.add(gameDifficulty);

		int preferenceSelected = JOptionPane.showConfirmDialog(null, preferencesPanel,
				"Enter your name and game preferences", JOptionPane.OK_CANCEL_OPTION);

		if (preferenceSelected == JOptionPane.OK_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Gets the game mode selected by the user in the combo box
	 * 
	 * @return String containing game mode selected by player
	 */
	public String getGameMode() {
		String mode = (String) gameModes.getSelectedItem();
		return mode;
	}

	/**
	 * Gets the difficulty level selected by the user in the combo box
	 * 
	 * @return String containing difficulty level selected by player
	 */
	public String getDifficulty() {
		String dif = (String) gameDifficulty.getSelectedItem();
		return dif;
	}

	/**
	 * Shows pop-up error message to player
	 * 
	 * @param msg is the message to display in the pop-up window
	 */
	public void showErrorMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	/**
	 * Resets the text in various controls to starting values for a new game. It
	 * also clears out and sets the focus on the guess text field.
	 * 
	 * @param secretString       is the secret String of the game
	 * @param hiddenSecretString is the secret String represented with underscores
	 * @param attemptsLeft       is the maximum number of incorrect attempts allowed
	 */
	public void resetGameParameters(String secretString, StringBuilder hiddenSecretString, int attemptsLeft) {
		setSecretTextArea(hiddenSecretString.toString());
		this.incorrectGuessesTextArea.setText("");
		this.attemptsLeftProgress.setValue(attemptsLeft);
		this.attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
		this.feedback.setText("");
		this.scoreLabel.setText("0");
		this.adminViewArea.setText(secretString);
		clearGuessTextField();
		focusOnGuessTextField();
	}

	/**
	 * Adds an extra space in between characters of the hidden secret word,
	 * eliminates leading and trailing spaces, and sets the resulting String in the
	 * secret text area
	 * 
	 * @param text is the hidden secret string revealing correct guesses and hiding
	 *             unknown letters with underscores
	 */
	public void setSecretTextArea(final String text) {
		String textSpacedOut = text.replace("", " ").trim();
		this.secretTextArea.setText(textSpacedOut);
	}

	/**
	 * Returns the player's guess (letter or word) after converting it to lower case
	 * and eliminating leading and trailing spaces. Only the first letter or word is returned.
	 * 
	 * @return String containing the guess entered by the player in the
	 *         GuessTextField (letter or word)
	 */
	public String getGuess() {
		int endIndex = guessTextField.getText().indexOf(" ");
		if (endIndex != -1) {
			return guessTextField.getText().substring(0, endIndex).toLowerCase().trim();
		} else
			return guessTextField.getText().toLowerCase().trim();
	}

	/**
	 * Sets the attemptsLeftTitle field and the progress bar to the updated number
	 * of attempts left
	 * 
	 * @param attemptsLeft is the updated number of attempts left
	 */
	public void setAttemptsLeft(int attemptsLeft) {
		this.attemptsLeftLabel.setText(Integer.toString(attemptsLeft));
		this.attemptsLeftProgress.setValue(attemptsLeft);
	}

	/**
	 * Sets the text in the incorrect guesses text area to show all incorrect
	 * guesses
	 * 
	 * @param incorrectGuesses is an ArrayList of Strings containing incorrect
	 *                         guesses
	 */
	public void addIncorrectGuess(String guess) {
		this.incorrectGuessesTextArea.append(guess + " ");
	}

	/**
	 * Updates controls to display updated game status upon an incorrect guess. Sets
	 * updated number of attempts left, sets updated number of incorrect guesses,
	 * and provides feedback to player about incorrect guess
	 * 
	 * @param attemptsLeft     is the updated number of attempts left
	 * @param incorrectGuesses is an ArrayList of all incorrect guesses
	 */
	public void runIncorrectGuessRoutine(int attemptsLeft, String guess) {
		setAttemptsLeft(attemptsLeft);
		addIncorrectGuess(guess);
		feedback.setForeground(Color.RED);
		this.feedback.setText("Your guessed incorrectly!");
	}

	/**
	 * Updates controls to display updated game status upon a correct guess. Updates
	 * the hidden secret string and provides feedback to player about correct guess
	 * 
	 * @param wordToGuess
	 */
	public void runCorrectGuessRoutine(StringBuilder hiddenSecretString) {
		setSecretTextArea(hiddenSecretString.toString());
		Color g = Color.green;
		feedback.setForeground(g.darker());
		this.feedback.setText("Your guessed correctly!");
	}

	/**
	 * Gets the leader board data and populates it into the JTable
	 * 
	 * @param lb is the Leader Board object containing leader board data
	 */
	public void showLeaderboard(Leaderboard lb) {
		ArrayList<LeaderboardResult> results = lb.getLeaderboardResults();
		StringBuilder allResults = new StringBuilder();
		String name;
		int score;

		for (LeaderboardResult r : results) {
			name = r.getName();
			score = r.getScore();
			allResults = allResults.append(name + "	" + score + "\n");
		}
		leaderboardArea.setText("Leaderboard \n\n" + "Name    " + "   Score\n\n" + allResults.toString());

	}

	/**
	 * Shows a pop-up message informing player that he/she won or lost and the
	 * score. Asks user "Play again?", gets player response, and returns boolean for
	 * player response
	 * 
	 * @param winOrLose is the String containing either "won" or "lost" to display
	 *                  in the pop-up message
	 * @param score     is the score earned in the game
	 * @return boolean true if player chooses to play again, and false if player
	 *         chooses not to play again
	 */
	public boolean getPlayAgain(String winOrLose, String score) {
		int n = JOptionPane.showConfirmDialog(frame,
				"You " + winOrLose + "! Your score is " + score + " .Do you want to play again?", "Play again?",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.OK_OPTION)
			return true;
		else
			return false;
	}

	public boolean confirmQuitGame() {
		int n = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit the game?", "Quit game?", 
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE);
		if(n==JOptionPane.OK_OPTION)
			return true;
		else
			return false;       		
	}
	
	/**
	 * Makes "Admin mode" elements visible. These elements include: the title, text
	 * area, and scroll bar pertaining to the secret word that becomes visible on
	 * "Admin mode"
	 */
	public void showAdminView() {
		this.adminScroll.setVisible(true);
		this.adminViewArea.setVisible(true);
		this.adminViewTitle.setVisible(true);
	}

	/**
	 * Makes "Admin mode" elements invisible. These elements include: the title,
	 * text area, and scroll bar pertaining to the secret word that become invisible
	 * on "Player mode"
	 */
	public void hideAdminView() {
		this.adminScroll.setVisible(false);
		this.adminViewArea.setVisible(false);
		this.adminViewTitle.setVisible(false);
	}

	public String getLeaderboardText() {
		return leaderboardArea.getText();
	}

	public void clearGuessTextField() {
		guessTextField.setText("");
	}

	public JButton getGuessButton() {
		return guessButton;
	}

	public void setFeedback(String text) {
		this.feedback.setText(text);
		feedback.setForeground(Color.BLACK);
	}

	public void clearAttemptsLeftTitle() {
		this.attemptsLeftTitle.setText("");
	}

	public void setGuessTextField(String dif) {
		this.guessTextField.setText(dif);
	}

	public JButton getAdminButton() {
		return this.adminButton;
	}

	public void setLeaderboardArea(String string) {
		leaderboardArea.setText(string);
	}

	public void focusOnGuessTextField() {
		this.guessTextField.requestFocus();
	}

	public String getName() {
		return nameField.getText();
	}

	public void showGameWindow() {
		this.frame.setVisible(true);
	}

	public JButton getQuitGameButton() {
		return this.quitGameButton;
	}

	public JButton getNewGameButton() {
		return this.newGameButton;
	}

	public void setScore(int score) {
		this.scoreLabel.setText(Integer.toString(score));
	}

	public void setAdminTextArea(String secretString) {
		this.adminViewArea.setText(secretString);
	}

	public JButton getPlayerButton() {
		return this.playerButton;
	}

	public JFrame getFrame() {
		return this.frame;
	}



}

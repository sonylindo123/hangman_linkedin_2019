# hangman_linkedin_2019
Hangman game created for LinkedIn 2019 Reach Program application

## Table Of Contents
* Introduction
* Technology Stack
* Set Up
* Game Rules
* Code Structure
* Additional features implemented

## Introduction
The Hangman game was developed as part of the application to LinkedIn's 2019 Reach  Apprenticeship Program.

## Technologies
* Java 8
* Java Swing Library

## Set Up
There are two ways to run this program:

### Method 1: Executing the Hangman_Linkedin.jar file
Download the Hangman.zip file and extract all files into the same folder. The folder should contain:
* Hangman_Linkedin.jar
* hangman_phrases_easy.txt
* hangman_phrases_difficult.txt
* word_LB.ser (optional file: contains pre-loaded leaderboard data for "guess word" play mode)
* Phrase_LB.ser (optional file: contains pre-loaded leaderboard data for "guess phrase" play mode)

Double click on the Hangman_Linkedin.jar file to execute it
If double-clicking on the file does not work, execute the Hangman_Linkedin.jar file from the command line interface following these steps:
1. Navigate to the directory where the Hangman.jar file was saved
2. Enter this command: java -jar Hangman_Linkedin.jar

### Method 2: Run the program from the IDE
Compile and execute the Controller.class that contains the main method.


## Game Rules
### Game Preferences
This version of Hangman currently supports two play modes: "Guess word" or "Guess phrase". Each mode can be played at difficulty levels ranging from 1 to 10. The player must enter his/her name and select the play mode and difficulty level to start the game.

### Choosing A Secret String
The program will randomly choose a secret word ("Guess word" play mode) or phrase ("Phrase mode" play mode) based on the difficulty level. Words are retrieved from the LinkedIn API and phrases are retrieved from text files provided in the src folder of the Java project. 

### Guessing The Secret String
Throughout the game the secret string is shown to the player as a set of underscores. A player may guess a letter or a word at a time. Only alphanumeric entries are allowed. The are a maximum of 6 incorrect attempts to guess the secret string. 
If a player guesses correctly, the secret string will reveal all occurrences of the correct guess. The unknown letters are shown as underscores. If the player guesses incorrectly, the player loses one attempt and the incorrect guesses are shown.

### Winning Or Losing The Game
If the player guesses the secret string before losing the 6 attempts, the game is won and the score is calculated and saved.
If the player runs out of attempts and did not guess the secret string, the game is lost. A score of zero will be saved.

### Starting A New Game
At any time, the player can click the "New Game" button to start a new game with a new name, play mode, and difficulty level. The old game's score is automatically saved and the old game is then automatically discarded.

### Quitting The Game
At any time, the player can click the "Quit Game" button to quit the game.

### Admin Mode
At any time, the player can click the "Admin Mode" button to see the secret string. This was implemented for testing purposes.

### Player Mode
At any time, te player can click the "Player Mode" button to hide the secret string. 

## Code Structure

The program is structured into three almost independent modules that perform clearly defined responsibilities: View, Hangman Game, and Controller. The Controller interacts with the View and the Hangman Game, acting as their mediator. View and HangmanGame are completely decoupled from each other. This approach was chosen because:
* A clear separation of responsibilities between modules provides a clear structure 
* Decoupling View and Hangman Game allows these two modules evolve independently of each other, and to change the implementation of View (e.g. switching a button for a toggle button) without affecting the Hangman Game implementation

Below is a brief description of each of the modules:

### The View
Implemented in the View class. It's responsible for rendering the UI elements and displaying the data received from the Controler module. It does not implement any game logic. The logic is delegated to the Controller. The view was developed with Java Swing.

### The Hangman Game
Implemented in the HangmanGame class and its subclasses. It models the hangman game by defining 1) attributes to manage game state data (e.g. attempts left, incorrect guesses) and 2) methods responsible for processing data according to the game rules (e.g. check if a guess is correct, check if player ran out of attempts). Hangman Game interacts with the Controller by receiving requests from the Controller and by providing game state data upon request. The Hangman Game does not directly interact with the View.

### The Controller
Implemented by the Controller class. It's an intermediary between the View and the Hangman Game. It listens to the events fired by the View components and defines the response. It responds to data received from the View and manipulates the Hangman Game appropriately. It also receives game data from the Hangman Game and updates the View appropriately.  

### Classes

#### HangmanGame
An abstract class that models a general hangman game by defining attributes to manage game state (e.g. attempts left, incorrect guesses) and methods to process data according to game rules (e.g. check if a guess is correct, check if player ran out of attempts). 
An abstract class implementation was chosen because:
* Subclasses extending this class will share all common attributes (e.g. player name, secret string, difficulty level, attempts left, etc) and methods (e.g. retrieve list of secret strings, reset game status, choose secret string, check if guess is correct, etc). Some method implementations are shared and some differ among subclasses (see HangmanWordMode and HangmanPhraseMode class descriptions for some examples)
* Subclasses share many common method implementations, which need to be implemented only once in the abstract class, therefore significantly reducing the amount of code in the subclasses. Examples of a shared method implementation are: 1) isGuessCorrect() which evaluates whether the guess was correct; 2) checkForWin() which checks whether player has guessed entire string before running out of attempts
* Standardizes the method signature of shared methods among subclasses

An abstract class implementation is more appropriate than an interface implementation due to:
* An abstract class describes what an object is (via attributes) and what it can do (via abstract and concrete methods), whereas an interface only describes what an object "can do" (via abstract methods). The different games clearly share both attributes and functionalities, rather than only sharing the functionalities. Therefore, abstract class is more approriate than interface.
* An abstract class allows to define and implement functionality (via abstract and concrete methods). An interface only allows to define functionality, but not implementat it (only abstract methods allowed). Because the different hangman games share many method impleentations, it is clear that the abstract class implementation has an advantage over interface.


#### HangmanWordMode
A subclass of HangmanGame that overrides methods to adapt them to the rules of the "Guess word" playing method. Examples of methods that were overriden are: 1) The abstract method getListOfStrings(). This implementation retrieves the list of strings from the LinkedIn provided API; 2) The abstract methodcalculateAndSaveScore(). This implementation calculates the score based on difficulty level.

#### HangmanPhraseMode
A subclass of HangmanGame that overrides methods to adapt them to the rules of the "Guess phrase" playing method. Examples of methods that were overriden are: 1) The abstract method getListOfStrings(). This implementation retrieves the list of strings from text files; 2) The abstract methodcalculateAndSaveScore(). This implementation calculates the score based on length of secret phrase.

#### Leaderboard
A class that manages the game results to be displayed in the leader board. It contains an ArrayList of LeaderboardResult objects. A ArrayList implementation was chosen over an Array because of the need to expand its size automatically as new elements are added. It is worth noting that both LinkedList or ArrayList data structures would be able to support the Leaderboard functionalities (adding elements to the end of a List and then sorting these elements), without significant difference in performance. The difference in performance between ArrayList and LinkedList arise when there is a need to insert into the middle of a list, or retrieving elements by index. Because these functionalities are not needed in this program, both ArrayList and LinkedList would be appropriate implementations. 

#### LeaderboardResult
A class that consists of player names and scores. The reason for creating a separate class is the need to implement sorting and serialization/deserialization functionalities. This class implements the Comparable interface in order to sort a list of LeaderboardResult objects. It also implements the Serializable interface to allow reading objects from file and writing objects to file to persist and retrieve object states.

#### Controller
A class that serves as intermediary between the View and the HangmanGame Model. It responds to data received from the View and manipulates the Hangman Game  appropriately; it also receives game data from the Hangman Game  and updates the View appropriately. This class also implements all the action listeners of the View's controls.

#### HangmanFactory
A class that creates objects of HangmanGame class and returns the implementation of HangmanWordMode or HangmanPhraseMode depending on the play mode chosen. This is done without exposing the creation logic to the client. This class was created to provide the client with an interface for creating objects extending the abstract class HangmanGame, which makes the client agnostic of the creation of HangmanGame objects. Another advantage of this class is that we may develop new implementations of HangmanGame (e.g. Guess words in Spanish) without the need to change existing client code.

#### LeaderboardException, WordListException
Custom exception classes created to 1) hide details of methods implementation throwing exceptions such as IOException, EOFException, 2) wrap exceptions thrown by the Leaderboard class methods.

### Additional Features Implemented

#### Guessing phrases
The "Guess Phrase" play mode allows to guess phrases instead of words. This was easily implemented by creating the HangmanPhraseMode class that extends the HangmanGame abstract class and overrides some methods.

#### Guessing numbers and words
The game currently supports alphanumeric guesses. Special characters and empty spaces are considered invalid entries.

#### Configurable difficulty level
Players may choose a difficulty level from 1 to 10. The computer then chooses a secret string based on the difficulty level selected.

#### Tracking of player scores and leaderboard
Player names and scores are persisted and retrieved in every game to display a leaderboard that shows results from highest to lowest score.

#### Admin Mode
Users may enter the "Admin mode" to view the secret word for testing purposes.


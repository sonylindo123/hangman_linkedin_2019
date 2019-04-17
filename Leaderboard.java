package mari.mvp3;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that stores past game results in an ArrayList of LeaderboardResult objects.
 * This class handles all functionality related to reading LeaderboardResult objects stored in a
 * file, writing LeaderboardResult objects to a file, addition of new LeaderboardResult to the list, and sorting
 * of LeaderboardResult objects
 * 
 * @author marianahu
 *
 */

public class Leaderboard {

	protected static ArrayList<LeaderboardResult> LeaderboardResults;

	protected Leaderboard() {
		LeaderboardResults = new ArrayList<>();
	}

	/**
	 * Adds a new LeaderboardResult object to the list
	 * @param nickname is the player name needed to instantiate the LeaderboardResult object
	 * @param score is the player's score needed to instantiate the LeaderboardResult object
	 */
	protected static void addResultToLeaderboard(String nickname, int score) {
		LeaderboardResults.add(new LeaderboardResult(nickname, score));
	}

	/**
	 * Writes the list of LeaderboardResult objects to a file (serialization) to persist their state
	 * @param filename is the filename storing the serialized data
	 */
	protected static void writeLeaderboardResultsToFile(String filename) {
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(LeaderboardResults);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Reads the list of LeaderboardResult objects from a file (deserialization) to retrieve the objects' states
	 * Data is read into the ArrayList of LeaderboardResult objects
	 * @param filename is the filename storing the serialized data
	 */
	protected static void readLeaderboardResultsFromFile(String filename) throws EOFException, IOException, ClassNotFoundException {
		File f = new File(filename);
		if (!f.exists()) {
			f.createNewFile();
			LeaderboardResults = new ArrayList<LeaderboardResult>();
		} else {

			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			LeaderboardResults = (ArrayList<LeaderboardResult>) in.readObject();
			in.close();
			fileIn.close();
		}
	}

	/**
	 * Sorts the ArrayList of LeaderboardResult objects
	 */
	protected static void sortByScore() {
		Collections.sort(LeaderboardResults);
	}

	protected static ArrayList<LeaderboardResult> getLeaderboard() {

		return LeaderboardResults;
	}

	protected ArrayList<LeaderboardResult> getLeaderboardResults() {
		return LeaderboardResults;

	}

}

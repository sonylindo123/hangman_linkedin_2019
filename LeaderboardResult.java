package mari.mvp3;


import java.io.Serializable;

/**
 * A class that consists of player names and scores. Implements the Comparable interface in order to sort a list of LeaderboardResult objects. 
 * It also implements the Serializable interface to allow reading objects from file and writing objects to file to persist and retrieve object states.
 * @author marianahu
 *
 */
public class LeaderboardResult implements Serializable, Comparable<LeaderboardResult>{

	protected String name;
	protected int score;

	protected LeaderboardResult() {
	}
	
	protected LeaderboardResult(String name, int score) {
		this.name = name;
		this.score = score;
	}

	/**
	 * Overrides the compareTo method from the Comparable interface to specify that LeaderboardResult objects must be sorted by score in descending order
	 */
	@Override
	public int compareTo(LeaderboardResult LeaderboardResult) {
		return LeaderboardResult.getScore() - this.score;
	}

	protected int getScore() {
		return this.score;
	}
	
	protected String getName() {
		return this.name;
	}

}



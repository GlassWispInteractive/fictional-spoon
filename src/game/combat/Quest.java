package game.combat;

public class Quest {
	public enum Goal {
		MONSTER, OPPONENT, CHEST, SHRINE, PORTAL, BOSS;
	}

	private Goal goal;
	private int current, total;
	
	/**
	 * constructs a new quest
	 * @param goal
	 * @param total
	 */
	public Quest(Goal goal, int total) {
		this.goal = goal;
		current = 0;
		this.total = total;
	}
	
	/**
	 * adds a point towards the quest
	 * @param goal
	 */
	public void add(Goal goal) {
		if (this.goal == goal) {
			current += 1;
		}
	}
	
	/**
	 * calculates the progress
	 * 0 <= progess <= 1
	 * @return
	 */
	public double progress() {
		// calculate the progess
		return (double) current / total;
	}
	
	@Override
	public String toString() {
		switch (goal) {
		case MONSTER:
			return "Kill " + total + " monster";
		case OPPONENT:
			return "Kill " + total + " opponent";
		case BOSS:
			return "Kill the boss";
		case PORTAL:
			return "Destroy " + total + " portals";
		case CHEST:
			return "Collect " + total + " chests";
		case SHRINE:
			return "Activate " + total + " shrines";
		}
		
		return "";
	}
}

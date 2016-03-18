package gen;

import static gen.environment.Ground.*;

import java.util.*;

import combat.Combo;
import entities.EntityFactory;
import gen.environment.Cell;
import gen.environment.DisjointSet;
import gen.environment.Map;

/**
 * 
 * 
 * @author Danny
 *
 */
public class Generator {
	// class variables

	private Random rand;
	private DisjointSet<Cell> cc; // connected components
	private Map map;
	private int n, m;
	private int roomTable[][], roomNum = 0;

	// constants
	final int ROOM_LIMIT = 300;
	final int[][] NEIGHS_ALL = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
	final int[][] NEIGHS_ODD = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };

	final int POWER = 3;

	/**
	 * constructor for generate levels with some fixed size
	 */
	public Generator(int n, int m) {
		// set size params
		this.n = n - (n + 1) % 2;
		this.m = m - (m + 1) % 2;

		// init pseudorandom generators
		rand = new Random();
		// rand.setSeed(42);

		// init room super array
		roomTable = new int[ROOM_LIMIT][];
	}

	/**
	 * generating functions
	 * 
	 * @return random level
	 */
	public Map newLevel() {
		map = new Map(n, m);

		// generate rooms and connecting floors between them
		cc = new DisjointSet<>();
		placeRooms();
		placeMaze();
		removeDeadends();

		// generate new floors to connect dead end rooms
		cc = new DisjointSet<>();
		placeLoops();
		placeMaze();
		removeDeadends();

		// get tiling numbers
//		placeTiles();

		// create objects like the player, monster, chests and shrines
		placeEntities();

		return map;
	}

	/**
	 * internal function to generate a room and place it into the level
	 * 
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 */
	private void placeRooms() {

	}

	/**
	 * internal function to generate a maze around the rooms this is done by a
	 * floodfill algorithm instead of some overengineering with MST
	 */
	private void placeMaze() {

	}

	/**
	 * internal function to remove the dead ends of the maze
	 */
	private void removeDeadends() {
		int count;
		boolean repeat = true, deadend[][];

		while (repeat) {
			// fresh inits for single execution of elemination
			deadend = new boolean[n][m];
			repeat = false;

			// dead end iff 3 neighbours are walls
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < m; y++) {
					if (map.getGround(x, y) == WALL) {
						continue;
					}

					count = 0;
					for (int j = 0; j < 4; j++) {
						if (map.getGround(x + NEIGHS_ALL[j][0], y + NEIGHS_ALL[j][1]) == WALL)
							count++;
					}

					if (count >= 3) {
						deadend[x][y] = true;
						repeat = true;
					}
				}
			}

			// remove dead ends
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < m; y++) {
					if (deadend[x][y])
						map.setGround(x, y, WALL);
				}
			}
		}
	}

	/**
	 * internal function to create loops inside the map
	 */
	private void placeLoops() {
		

	}


	
	private void placeEntities() {
	}

}

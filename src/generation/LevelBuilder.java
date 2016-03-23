package generation;

import static generation.Ground.FLOOR;
import static generation.Ground.ROOM;
import static generation.Ground.WALL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import combat.Combo;
import entities.EntityFactory;

/**
 * builder pattern class to generate level maps source:
 * http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 * 
 * @author Danny
 *
 */
public class LevelBuilder {
	// class constants
	final int ROOM_LIMIT = 300;
	final int[][] NEIGHS_ALL = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
	final int[][] NEIGHS_ODD = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
	final int POWER = 3;

	// class members
	private Map map;
	private Random rnd;
	private int rooms[][], roomNum = 0;
	private EntityFactory fac = EntityFactory.getFactory();
	private ArrayList<int[]> spawnPoints = new ArrayList<>();
	
	public LevelBuilder(int n, int m) {
		n = n - (n + 1) % 2;
		m = m - (m + 1) % 2;
		map = new Map(n, m);

		// get random object
		rnd = new Random();

		// init room super array
		rooms = new int[ROOM_LIMIT][];
	}

	/**
	 * static method to create a nice map
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	public static Map newRandomLevel(int n, int m) {
		return new LevelBuilder(n, m).genMap().genRandomEntities()
				.create();
	}
	
	/**
	 * general method to generate a random map
	 * 
	 */
	public LevelBuilder genMap() {
	    
	    return genRooms().genMaze().clearDeadends().genLoops().clearDeadends().genPlayer();
	}

	/**
	 * final method which is called to make a Map from a MapBuilder
	 */
	public Map create() {
		placeTiles();
		return map;
	}

	private void placeTiles() {
		// b_0 b_1 b_2 b_3 -> left right top bottom
		// binary counting with 1 means that area is walkable
		final int[] tileNumber = new int[] { 9 + 57 * 9, // fail
				2 + 57 * 0, // 0 0 0 1
				0 + 57 * 0, // 0 0 1 0
				1 + 57 * 0, // 0 0 1 1
				0 + 57 * 2, // 0 1 0 0
				4 + 57 * 1, // 0 1 0 1
				3 + 57 * 1, // 0 1 1 0
				6 + 57 * 0, // 0 1 1 1
				0 + 57 * 1, // 1 0 0 0
				4 + 57 * 0, // 1 0 0 1
				3 + 57 * 0, // 1 0 1 0
				6 + 57 * 1, // 1 0 1 1
				2 + 57 * 1, // 1 1 0 0
				5 + 57 * 1, // 1 1 0 1
				5 + 57 * 0, // 1 1 1 0
				1 + 57 * 1, // 1 1 1 1
		};

		for (int x = 0; x < map.getN(); x++) {
			for (int y = 0; y < map.getM(); y++) {
				int tile = 0;

				if (map.isWalkable(x - 1, y))
					tile += 1;
				if (map.isWalkable(x + 1, y))
					tile += 2;
				if (map.isWalkable(x, y - 1))
					tile += 4;
				if (map.isWalkable(x, y + 1))
					tile += 8;
				//
				// tile = tileNumber[num].clone();
				// if (map.getGround(x, y) == FLOOR) {
				// tile[0] += 20;
				// tile[1] += 12;
				// } else if (map.getGround(x, y) == ROOM) {
				// tile[0] += 34;
				// tile[1] += 12;
				// }

				map.setTileNumber(x, y, tileNumber[tile]);
			}
		}

	}

	public LevelBuilder genRooms() {
		// declare vars
		int xLen, yLen, xStart, yStart;

		// try to put up a new room in each iteration
		for (int i = 0; i < ROOM_LIMIT; i++) {
			// make sure to have valid room sizes
			// random num n transforms into 2n+1 -> odd
			do {
				xLen = (int) (4 + 2 * rnd.nextGaussian());
				xLen = 2 * xLen + 1;
				yLen = (int) (4 + 2 * rnd.nextGaussian());
				yLen = 2 * yLen + 1;
			} while (xLen < 4 || yLen < 4);

			// gen a position in the level
			// increment number if its even -> odd
			xStart = rnd.nextInt(map.getN() - xLen);
			xStart = xStart + (xStart + 1) % 2;
			yStart = rnd.nextInt(map.getM() - yLen);
			yStart = yStart + (yStart + 1) % 2;

			// check whether the position is valid
			if (!checkRoom(xStart, yStart, xLen, yLen)) {
				continue;
			}

			// place room
			map.setNewRoom(xStart, xLen, yStart, yLen);

			// insert room into memory
			rooms[roomNum] = new int[] { xStart, xLen, yStart, yLen };
			roomNum++;

		}

		// return updated builder object
		return this;
	}

	/**
	 * helper function to check for valid room positions
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xLen
	 * @param yLen
	 * @return
	 */
	private boolean checkRoom(int xStart, int yStart, int xLen, int yLen) {
		// be sure to only check for odd numbers (xStart, yStart are odd)
		for (int i = 0; i <= xLen; i += 2) {
			for (int j = 0; j <= yLen; j += 2) {

				if (map.getGround(xStart + i, yStart + j) != WALL) {
					return false;
				}
			}
		}

		// no collision -> valid placement
		return true;
	}

	/**
	 * internal function to generate a maze around the rooms this is done by a
	 * floodfill algorithm instead of some overengineering with MST
	 * 
	 * @return
	 */
	public LevelBuilder genMaze() {
		// create connected compontents
		DisjointSet<Cell> cc = new DisjointSet<>();

		for (int i = 0; i < roomNum; i++) {
			final int xStart = rooms[i][0], xLen = rooms[i][1], yStart = rooms[i][2], yLen = rooms[i][3];
			cc.makeSet(map.getCell(xStart, yStart));
			for (int x = xStart; x < xStart + xLen; x += 2) {
				for (int y = yStart; y < yStart + yLen; y += 2) {
					if (x == xStart && y == yStart)
						continue;

					cc.makeSet(map.getCell(x, y));
					cc.union(map.getCell(xStart, yStart), map.getCell(x, y));
				}
			}
		}

		ArrayList<int[]> q = new ArrayList<>();

		for (int i = 1; i < map.getN(); i += 2) {
			for (int j = 1; j < map.getM(); j += 2) {
				// fill in fixed cells on odd / odd coordinates
				if (map.getGround(i, j) == WALL) {
					map.setGround(i, j, FLOOR);
					cc.makeSet(map.getCell(i, j));
				}

				// queue neighbours when one corner is not a room
				if (i + 2 < map.getN() && map.getGround(i + 1, j) != ROOM)
					q.add(new int[] { i, j, i + 2, j });
				if (j + 2 < map.getM() && map.getGround(i, j + 1) != ROOM)
					q.add(new int[] { i, j, i, j + 2 });
			}
		}

		// choose connector in a random order
		Collections.shuffle(q);

		for (int[] e : q) {
			// rename array
			final int x1 = e[0], y1 = e[1], x2 = e[2], y2 = e[3];

			if (cc.findSet(map.getCell(x1, y1)) == null)
				continue;

			if (cc.findSet(map.getCell(x2, y2)) == null)
				continue;

			// check if two cells are already connected
			if (cc.findSet(map.getCell(x1, y1)) == cc.findSet(map.getCell(x2, y2)))
				continue;

			// merge two components by adding a connector
			cc.union(map.getCell(x1, y1), map.getCell(x2, y2));
			map.setGround((x1 + x2) / 2, (y1 + y2) / 2, FLOOR);
		}

		// return updated builder object
		return this;
	}

	public LevelBuilder genLoops() {
		DisjointSet<Cell> cc = new DisjointSet<>();

		for (int i = 0; i < roomNum; i++) {
			// declare variables
			final int xStart = rooms[i][0], xLen = rooms[i][1], yStart = rooms[i][2], yLen = rooms[i][3];

			// check if room only has one floor connection
			int count = 0;
			for (int x = 0; x < xLen; x++) {
				if (map.getGround(xStart + x, yStart - 1) == FLOOR)
					count++;

				if (map.getGround(xStart + x, yStart + yLen) == FLOOR)
					count++;
			}

			for (int y = 0; y < yLen; y++) {
				if (map.getGround(xStart - 1, yStart + y) == FLOOR)
					count++;

				if (map.getGround(xStart + xLen, yStart + y) == FLOOR)
					count++;
			}

			if (count > 1) {
				continue;
			}

			// create a connected component for each room
			cc.makeSet(map.getCell(xStart, yStart));
			for (int x = xStart; x < xStart + xLen; x += 2) {
				for (int y = yStart; y < yStart + yLen; y += 2) {
					if (x == xStart && y == yStart)
						continue;

					cc.makeSet(map.getCell(x, y));
					cc.union(map.getCell(xStart, yStart), map.getCell(x, y));
				}
			}
		}

		// after settings up the correct CC we just run the maze generation
		genMaze();
		clearDeadends();

		// return updated builder object
		return this;
	}

	public LevelBuilder clearDeadends() {
		int count;
		boolean repeat = true, deadend[][];

		while (repeat) {
			// fresh inits for single execution of elemination
			deadend = new boolean[map.getN()][map.getM()];
			repeat = false;

			// dead end iff 3 neighbours are walls
			for (int x = 0; x < map.getN(); x++) {
				for (int y = 0; y < map.getM(); y++) {
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
			for (int x = 0; x < map.getN(); x++) {
				for (int y = 0; y < map.getM(); y++) {
					if (deadend[x][y])
						map.setGround(x, y, WALL);
				}
			}
		}

		// return updated builder object
		return this;
	}
	
	/**
	 * 
	 *  spawn Entities on generated walkable points
	 * 
	 */
	
        private void generateSpawnPoints() {
        	spawnPoints.clear();
        
        	for (int i = 1; i < map.getN(); i += 2) {
        	    for (int j = 1; j < map.getM(); j += 2) {
        
        		if (map.isWalkable(i, j)) {
        		    spawnPoints.add(new int[] { i, j });
        		}
        	    }
        	}
        
        	Collections.shuffle(spawnPoints);
        }
        
        private int[] getSpawnPoint() {
            return getSpawnPoint(false);
        }
    
        private int[] getSpawnPoint(boolean roomOnly) {
            
            	if(spawnPoints.size() == 0) {
            	    generateSpawnPoints();
            	}
    
//        	Collections.shuffle(spawnPoints);
        	
        	int[] newSpawnPoint = null;
        	
        	if(roomOnly) {
        	    	for(int[] p : spawnPoints) {
                	    
        	    	    if(map.isWalkableRoom(p[0], p[1])){
        	        	newSpawnPoint = p;
        	        	spawnPoints.remove(p);
        	        	
        	        	return newSpawnPoint;
        	    	    }
                	}
        	}
        

            	newSpawnPoint = spawnPoints.get(0);
            	spawnPoints.remove(0);
        
        	return newSpawnPoint;
        }
        
        public LevelBuilder genPlayer() {
            
        	int[] newSpwan;
        
        	newSpwan = getSpawnPoint(true);
        	fac.makePlayer(newSpwan[0], newSpwan[1]);
        
        	return this;
        }
        
        public LevelBuilder genRandomEntities() {
            
            int monsterNum = rnd.nextInt(2*roomNum);
            int portalNum = rnd.nextInt(roomNum/20);
            int opponentNum = rnd.nextInt(roomNum/20); 
            int chestNum = rnd.nextInt(roomNum/10);
            int shrineNum = rnd.nextInt(roomNum/15);
            
            return genMonster(monsterNum).genPortal(portalNum).genOpponent(opponentNum).genChest(chestNum).genShrine(shrineNum);
        }
    
        public LevelBuilder genMonster(int num) {     
            
            	int[] newSpwan;
        	boolean roomOnly = true;
            	    
            	for(int i = 0; i < num; i++) {
            	    
        		int used = POWER, type, powers[] = new int[] { 0, 0, 0, 0, 0 };
            		// monsters can be of the given power or at most 2 points higher
            		used += rnd.nextInt(3);
            
            		// create monster of random type
            		type = rnd.nextInt(5);
            		powers[type] = used;
            		used -= used;
            		
        		newSpwan = getSpawnPoint(roomOnly);
        		fac.makeMonster(newSpwan[0], newSpwan[1], 100, powers, "monster");
        		
        		// last 30% can be in random positions
        		if(i > num*0.7) {
//        		    roomOnly = false;
        		}
            	}
            	    
            	return this;
        }
    
        public LevelBuilder genPortal(int num) {
    
        	int[] newSpwan;
        
        	for (int i = 0; i < num; i++) {
        
        	    newSpwan = getSpawnPoint();
        	    fac.makePortal(newSpwan[0], newSpwan[1], "Informatiker");
        	}
        
        	return this;
        }
    
        public LevelBuilder genOpponent(int num) {
        
        	int[] newSpwan;
        
        	for (int i = 0; i < num; i++) {
        
        	    newSpwan = getSpawnPoint();
        	    fac.makeOpponent(newSpwan[0], newSpwan[1], "Informatiker");
        	}
        
        	return this;
        }
    
        public LevelBuilder genChest(int num) {
    
        	int[] newSpwan;
        
        	for (int i = 0; i < num; i++) {
        
        	    newSpwan = getSpawnPoint();
        	    int len = rnd.nextInt(3);
        	    fac.makeChest(newSpwan[0], newSpwan[1], Combo.generate(2 + len));
        	}
        
        	return this;
        }
    
        public LevelBuilder genShrine(int num) {
    
        	int[] newSpwan;
        
        	for (int i = 0; i < num; i++) {
        
        	    newSpwan = getSpawnPoint();
        	    fac.makeShrine(newSpwan[0], newSpwan[1]);
        	}
        
        	return this;
        }


//	public LevelBuilder genEntities() {
//		// get factory
//		EntityFactory fac = EntityFactory.getFactory();
//
//		// create player
//		int x, y, room[] = rooms[0];
//		x = room[0] + rnd.nextInt(room[1]);
//		y = room[2] + rnd.nextInt(room[3]);
//
//		fac.makePlayer(x, y);
//
//		// create objects for every room
//		for (int i = 1; i < roomNum; i++) {
//			// declare variables
//			final int xStart = rooms[i][0], xLen = rooms[i][1], yStart = rooms[i][2], yLen = rooms[i][3];
//
//			// generate possible points in room
//			ArrayList<int[]> q = new ArrayList<>();
//			for (int j = 0; j < xLen; j++)
//				for (int k = 0; k < yLen; k++)
//					q.add(new int[] { xStart + j, yStart + k });
//			Collections.shuffle(q);
//
//			// choose those for putting on an object
//			int p[];
//			p = q.get(0);
//			q.remove(0);
//
//			int used = POWER, type, powers[] = new int[] { 0, 0, 0, 0, 0 };
//			// monsters can be of the given power or at most 2 points higher
//			used += rnd.nextInt(3);
//
//			// create monster of random type
//			type = rnd.nextInt(5);
//			powers[type] = used;
//			used -= used;
//
//			fac.makeMonster(p[0], p[1], 100, powers, "monster");
//
//			// create a Portal every 8 rooms
//			if (i % 8 == 0) {
//				p = q.get(0);
//				q.remove(0);
//				fac.makePortal(p[0], p[1], "Informatiker");
//			}
//
//			// create a opponent every 7 rooms
//			if (i % 8 == 0) {
//				p = q.get(0);
//				q.remove(0);
//				fac.makeOpponent(p[0], p[1], "Informatiker");
//			}
//
//			// create a chest every 3 rooms
//			if (i % 3 == 0) {
//				p = q.get(0);
//				q.remove(0);
//				int len = rnd.nextInt(5);
//				fac.makeChest(p[0], p[1], Combo.generate(2 + len));
//			}
//
//			// create a shrine every 10 rooms
//			if (i % 10 == 0) {
//				p = q.get(0);
//				q.remove(0);
//				fac.makeShrine(p[0], p[1]);
//			}
//		}
//
//		// return updated builder object
//		return this;
//	}
}

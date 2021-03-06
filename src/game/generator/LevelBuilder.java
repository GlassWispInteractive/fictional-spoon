package game.generator;

import static game.generator.Ground.FLOOR;
import static game.generator.Ground.ROOM;
import static game.generator.Ground.WALL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import game.entities.Chest;
import game.entities.Monster;
import game.entities.Opponent;
import game.entities.Player;
import game.entities.Portal;
import game.entities.Shrine;

/**
 * builder pattern class to generate level maps source:
 * http://journal.stuffwithstuff.com/2014/12/21/rooms-and-mazes/
 * 
 * @author Danny
 *        
 */
public class LevelBuilder {
    // layout types
    public enum Layout {
        MAZE, MAZE_WITH_ROOMS, SINGLE_CONN_ROOMS, LOOPED_ROOMS, DOUBLE_CONN_ROOMS
    };
    
    // class constants
    int ROOM_LIMIT = 300;
    final int[][] NEIGHS_ALL = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    final int[][] NEIGHS_ODD = new int[][] { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
    final int POWER = 3;
    
    // setting vars
    private boolean hasRooms = false, hasFloor = true;
    
    // class members
    private Map map;
    private Random rnd;
    private Room[] rooms;
    private int roomNum = 0;
    private ArrayList<int[]> floors;
    
    /**
     * constuctor
     * 
     * @param n
     *            the width
     * @param m
     *            the height
     * @param layout
     *            the layout
     */
    public LevelBuilder(int n, int m, Layout layout) {
        n = n - (n + 1) % 2;
        m = m - (m + 1) % 2;
        map = new Map(n, m);
        
        // get random object
        rnd = new Random();
        
        switch (layout) {
        case MAZE:
            // gen maze with no dead ends at first
            genFloors(ccFromNoRooms());
            
            break;
        case MAZE_WITH_ROOMS:
            // gen rooms
            genRooms(5);
            
            // gen maze with no dead ends at first
            genFloors(ccFromAllRooms());
            
            break;
            
        case SINGLE_CONN_ROOMS:
            // gen rooms
            genRooms(ROOM_LIMIT);
            
            // gen maze with no dead ends at first
            genFloors(ccFromAllRooms());
            clearDeadends();
            
            break;
            
        case LOOPED_ROOMS:
            // gen rooms
            genRooms(ROOM_LIMIT);
            
            // gen maze with no dead ends at first
            genFloors(ccFromAllRooms());
            clearDeadends();
            
            // add another maze for loops
            genFloors(ccFromEndRooms());
            
            // remove the dead ends finally
            clearDeadends();
            
            break;
        case DOUBLE_CONN_ROOMS:
            // gen rooms
            genRooms(ROOM_LIMIT);
            
            // gen maze with no dead ends at first
            genFloors(ccFromAllRooms());
            clearDeadends();
            
            // add another maze for loops
            genFloors(ccFromAllRooms());
            
            // remove the dead ends finally
            clearDeadends();
            
            break;
        }
        
        // complete floors computation for all layouts
        // (every layout so far has floors)
        completeFloors();
        
        // set up player
        genPlayer();
    }
    
    /**
     * internal function which generated a random number of rooms the limit is
     * just an upper bound and its not expected to be reached
     * 
     * @param limit
     * @return
     */
    private LevelBuilder genRooms(int limit) {
        // set room flag
        hasRooms = true;
        
        // init room super array
        rooms = new Room[limit];
        
        // declare vars
        int xLen, yLen, xStart, yStart;
        
        // try to put up a new room in each iteration
        for (int i = 0; i < limit; i++) {
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
            rooms[roomNum] = new Room(xStart, xLen, yStart, yLen);
            roomNum++;
            
        }
        
        // return updated builder object
        return this;
    }
    
    /**
     * helper function to check for valid room coordinates
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
     * create a disjoint set with a connected component for each room
     * 
     * @return
     */
    private DisjointSet<Cell> ccFromAllRooms() {
        // create connected compontents
        DisjointSet<Cell> cc = new DisjointSet<>();
        
        for (int i = 0; i < roomNum; i++) {
            cc.makeSet(map.getCell(rooms[i].getXStart(), rooms[i].getYStart()));
            
            for (int x = rooms[i].getXStart(); x < rooms[i].getXStart() + rooms[i].getXLen(); x += 2) {
                for (int y = rooms[i].getYStart(); y < rooms[i].getYStart() + rooms[i].getYLen(); y += 2) {
                    if (x == rooms[i].getXStart() && y == rooms[i].getYStart())
                        continue;
                        
                    cc.makeSet(map.getCell(x, y));
                    cc.union(map.getCell(rooms[i].getXStart(), rooms[i].getYStart()), map.getCell(x, y));
                }
            }
        }
        
        return cc;
    }
    
    /**
     * create a disjoint set with a connected component for each room
     * 
     * @return
     */
    private DisjointSet<Cell> ccFromEndRooms() {
        // add for rooms which only one floor connection a connected component
        DisjointSet<Cell> cc = new DisjointSet<>();
        
        for (int i = 0; i < roomNum; i++) {
            // declare variables
            final int xStart = rooms[i].getXStart(), xLen = rooms[i].getXLen(), yStart = rooms[i].getYStart(),
                    yLen = rooms[i].getYLen();
                    
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
            
            // skip the room on break condition
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
        
        return cc;
    }
    
    /**
     * create a disjoint set with no connected components at all
     * 
     * @return
     */
    private DisjointSet<Cell> ccFromNoRooms() {
        return new DisjointSet<>();
    }
    
    /**
     * internal function to generate a maze around the rooms
     * 
     * this is done by a floodfill algorithm instead of some overengineering
     * with MST
     * 
     * @return
     */
    private void genFloors(DisjointSet<Cell> cc) {
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
    }
    
    /**
     * internal function which deletes all dead ends of a maze
     * 
     * @return
     */
    private void clearDeadends() {
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
    }
    
    /**
     * internal helper function
     */
    private void completeFloors() {
        // set floor flag
        hasFloor = true;
        
        // create new array
        floors = new ArrayList<>();
        
        // fill in all the floor cells
        for (int i = 1; i < map.getN(); i += 2) {
            for (int j = 1; j < map.getM(); j += 2) {
                if (map.getGround(i, j) == Ground.FLOOR) {
                    floors.add(new int[] { i, j });
                }
            }
        }
        
        // spawn points is a random permuation
        Collections.shuffle(floors);
    }
    
    /**
     * internal function to generate the player
     * 
     * @return
     */
    private LevelBuilder genPlayer() {
        // declare int array
        int[] newSpwan;
        
        if (hasRooms) {
            // make player
            newSpwan = rooms[0].getUnusedRandomCell();
            new Player(newSpwan[0], newSpwan[1]);
            
            // make two chests
            newSpwan = rooms[0].getUnusedRandomCell();
            Chest.generate(newSpwan[0], newSpwan[1]);
            
            newSpwan = rooms[0].getUnusedRandomCell();
            Chest.generate(newSpwan[0], newSpwan[1]);
            
            // make a shrine to rescue
            newSpwan = rooms[0].getUnusedRandomCell();
            new Shrine(newSpwan[0], newSpwan[1]);
        } else if (hasFloor) {
            newSpwan = floors.get(0);
            new Player(newSpwan[0], newSpwan[1]);
            floors.remove(0);
        }
        
        return this;
    }
    
    /**
     * helper function to generate entities on the map
     * 
     * the function itself is only called by other functions.
     * 
     * @param perRoom
     * @param perFloor
     * @param creator
     * @return
     */
    private LevelBuilder genEntity(double perRoom, double perFloor, Creatable creator) {
        // room counter
        int[] newSpwan;
        double ctr = 0;
        
        // add one entity in a room when the quotient adding up exceeds 1
        // skip the first room => i = 1
        if (hasRooms) {
            for (int i = 1, j = i - 1; i < roomNum; i++) {
                ctr += perRoom;
                
                // break condition
                if (ctr < 1) {
                    continue;
                }
                
                // set correct room
                j += 1;
                
                // loop is true at least once
                while (ctr >= 1) {
                    // update j
                    ctr -= 1;
                    
                    // put up monster at a random room cell
                    newSpwan = rooms[j].getUnusedRandomCell();
                    creator.generate(newSpwan[0], newSpwan[1]);
                }
            }
        }
        
        // add one entity at a floor cell when the quotient adding up exceeds 1
        if (hasFloor) {
            for (int i = 0; i < floors.size(); i++) {
                ctr += perFloor;
                
                // break condition
                if (ctr < 1) {
                    continue;
                }
                
                // loop is true at least once
                while (ctr >= 1) {
                    // update j
                    ctr -= 1;
                    
                    // put up monster at a random floor cells
                    newSpwan = floors.get(0);
                    creator.generate(newSpwan[0], newSpwan[1]);
                    floors.remove(0);
                }
            }
        }
        
        return this;
    }
    
    /**
     * generate monsters in the map
     * 
     * @param perRoom
     * @param perFloor
     * @return
     */
    public LevelBuilder genMonster(double perRoom, double perFloor) {
        return genEntity(perRoom, perFloor, (x, y) -> {
            
            // generate monster parameter
            // MonsterType type =
            // MonsterType.values()[rnd.nextInt(MonsterType.values().length -
            // 1)];
            // int dmg = rnd.nextInt(3) + 1;
            
            new Monster(x, y, map.isWalkable(x, y));
        });
    }
    
    /**
     * generate portals in the map
     * 
     * @param perRoom
     * @param perFloor
     * @return
     */
    public LevelBuilder genPortal(double perRoom, double perFloor) {
        genEntity(perRoom, perFloor, (x, y) -> {
            new Portal(x, y);
        });
        
        return this;
    }
    
    /**
     * generate elite on the map
     * 
     * @param perRoom
     * @param perFloor
     * @return
     */
    public LevelBuilder genOpponent(double perRoom, double perFloor) {
        genEntity(perRoom, perFloor, (x, y) -> {
            Opponent.generate(x, y, map.isWalkable(x, y));
        });
        
        return this;
    }
    
    /**
     * generate a chest on the map
     * 
     * @param perRoom
     * @param perFloor
     * @return
     */
    public LevelBuilder genChest(double perRoom, double perFloor) {
        genEntity(perRoom, perFloor, (x, y) -> {
            Chest.generate(x, y);
        });
        
        return this;
    }
    
    /**
     * generate a shrine on the map
     * 
     * @param perRoom
     * @param perFloor
     * @return
     */
    public LevelBuilder genShrine(double perRoom, double perFloor) {
        genEntity(perRoom, perFloor, (x, y) -> {
            new Shrine(x, y);
        });
        
        return this;
    }
    
    /**
     * compute the files
     */
    private void computeTiles() {
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
    
    /**
     * function which is called finally to make a Map from a MapBuilder
     */
    public Map create() {
        computeTiles();
        return map;
    }
}

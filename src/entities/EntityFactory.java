package entities;

import java.util.ArrayList;

import combat.Combo;

/**
 * Factory for MobileObject objects
 * 
 * @author danny
 *
 */
public class EntityFactory {
	private static EntityFactory singleton;
	private ArrayList<Entity> ents;
	private ArrayList<Entity> deletEnts;
	private ArrayList<Entity> addEnts;
	private Entity player;

	/**
	 * private constructor
	 */
	private EntityFactory() {
		ents = new ArrayList<Entity>();
		deletEnts = new ArrayList<Entity>();
		addEnts = new ArrayList<Entity>();
	}

	/**
	 * get the singleton object
	 * 
	 * @return MobFactory
	 */
	public static EntityFactory getFactory() {
		if (singleton == null) {
			singleton = new EntityFactory();
		}

		return singleton;
	}
	
	public static void resetGame() {
	    singleton = new EntityFactory();
	}

	public ArrayList<Entity> getMobs() {
		return ents;
	}

	public Entity getPlayer() {
		return player;
	}

	public void deleteEntity(Entity ent) {
		deletEnts.add(ent);
	}

	public void smartDelete() {
		for (Entity ent : deletEnts) {
			ents.remove(ent);
		}
		deletEnts.clear();
	}

	public Player makePlayer(int x, int y) {
		Player player = new Player(x, y);
		this.player = player;
		return player;
	}

	public Monster makeMonster(int x, int y, int hp, int[] power, String name) {
		Monster monster = new Monster(x, y, hp, power, name);
		addEnts.add(monster);
		return monster;
	}
	
	public Portal makeSuperMonster(int x, int y, String name) {
		Portal superMonster = new Portal(x, y, name);
		addEnts.add(superMonster);
		return superMonster;
	}
	
	public Opponent makeOpponent(int x, int y, String name) {
		Opponent opponent = new Opponent(x, y, name);
		addEnts.add(opponent);
		return opponent;
	}

	public Chest makeChest(int x, int y, Combo combo) {		
		Chest chest = new Chest(x, y, combo);
		addEnts.add(chest);
		return chest;
	}

	public Shrine makeShrine(int x, int y) {
		Shrine shrine = new Shrine(x, y);
		addEnts.add(shrine);
		return shrine;
	}
	
	public void smartAdd(){
		for (Entity ent : addEnts) {
			ents.add(ent);
		}
		addEnts.clear();
	}

}

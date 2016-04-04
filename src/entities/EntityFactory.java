package entities;

import java.util.ArrayList;

import combat.Combo;
import entities.Monster.MonsterType;

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
	private Player player;

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

	public Player getPlayer() {
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

	public Monster makeMonster(int x, int y, boolean spawnIsInRoom, int hp, MonsterType type, int dmg, String name) {
		Monster monster = new Monster(x, y, spawnIsInRoom, hp, type, dmg, name);
		addEnts.add(monster);
		return monster;
	}
	
	public Portal makePortal(int x, int y, String name) {
		Portal portal = new Portal(x, y, name);
		addEnts.add(portal);
		return portal;
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

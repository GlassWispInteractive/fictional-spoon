package entities;

import java.util.ArrayList;

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
	private Entity player;

	/**
	 * private constructor
	 */
	private EntityFactory() {
		ents = new ArrayList<Entity>();
		deletEnts = new ArrayList<Entity>();
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

	public ArrayList<Entity> getMobs() {
		return ents;
	}
	public Entity getPlayer(){
		return player;
	}
	public void deleteEntity(Entity ent){
		deletEnts.add(ent);
	}
	public void smartDeletNow(){
		for(Entity ent : deletEnts){
			ents.remove(ent);
		}
		deletEnts.clear();
	}

	public Player makePlayer(int x, int y) {
		Player player = new Player(x, y);
		this.player = player;
		return player;
	}

	public Monster makeMonster(int x, int y, int hp, int[]power, String name) {
		Monster monster = new Monster(x, y, hp, power, name);
		ents.add(monster);
		return monster;
	}

	public Chest makeChest(int x, int y) {
		Chest chest = new Chest(x, y);
		ents.add(chest);
		return chest;
	}
	
	public Shrine makeShrine(int x, int y) {
		Shrine shrine = new Shrine(x, y);
		ents.add(shrine);
		return shrine;
	}

}

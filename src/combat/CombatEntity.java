package combat;

import entities.Entity;

public abstract class CombatEntity extends Entity {
	protected int maxLife, life, dmg;

	public CombatEntity(int x, int y, int maxHp, int dmg) {
		// call entity constructor
		super(x, y);

		this.maxLife = maxHp;
		this.life = maxHp;
		this.dmg = dmg;
	}

	/**
	 * function to damage the combat entity
	 * 
	 * @param target
	 * @param power
	 */
	public void attack(CombatEntity target, double power) {
		target.life -= dmg * power;
	}

	/**
	 * function to damage the combat entity
	 * 
	 * @param dmg
	 */
	public void attack(CombatEntity target) {
		attack(target, 1);
	}

	/**
	 * function which tells whether the entity is alive
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return life > 0;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @param maxLife
	 *            the maxLife to set
	 */
	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	/**
	 * @param dmg
	 *            the dmg to set
	 */
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

}

package combat;

public class Soul implements IAttackable{

	private String name;
	private int hp;
	private boolean soulDead = false;
//	private Attacks[][] attacks;
	private Attacks attack;

	public Soul(String name) {
		this.name = name;
//		attacks = new Attacks[][] { { new Attacks(), new Attacks() }, { new Attacks(), new Attacks() } };
		attack = new Attacks();
	}

	public String getName() {
		return name;
	}

//	public Attacks[][] getAttacks() {
//		return attack;
//	}
	public Attacks getAttack() {
		return attack;
	}
	
	public boolean isDead() {
		return soulDead;
	}

	@Override
	public void getDmg(int dmg) {
		hp -= dmg;
		
		if(hp <= 0){
			soulDead = true;
		}
	}
	
	public void doAttack(IAttackable focus){
		attack.doAttack(focus);
	}
	public void doAttack(IAttackable focus, Combo combo) {
		attack.doAttack(focus, combo);
	}

}

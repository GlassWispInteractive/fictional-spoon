package combat;

public class Soul {

	private String name;
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
}

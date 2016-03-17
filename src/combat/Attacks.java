package combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Attacks {
	
	private String name;
	private static ArrayList<String> attackNames = new ArrayList<String>(Arrays.asList(
			new String[] { "Feuerwerfer", "Wasserbombe", "Pustekuchen", "Erdloch", "Schmetterball", "Traubenwerfer",
					"Rutschschlag", "Faustschlag", "Ausruhen", "Sein Leben chillen", "Stecker ziehen" }));
	
	private int attackDmg;

	//generate name + attackDmg
	public Attacks() {
		this(new Random().nextInt(40) + 10, attackNames.get(new Random().nextInt(attackNames.size() - 1)));
	}
	
	//generate name
	public Attacks(int attackDmg) {
		this(attackDmg, attackNames.get(new Random().nextInt(attackNames.size() - 1)));
	}
	
	//save attackDmg and name
	public Attacks(int attackDmg, String name) {
		this.name = name;
		this.attackDmg = attackDmg;
	}	
	
	
	
	public String getName() {
		return name;
	}
	
	public void doAttack(IAttackable focus) {
		doAttack(focus, null);
	}
	public void doAttack(IAttackable focus, Combo combo) {
		int dmg = attackDmg;
		float luck = new Random().nextFloat() + 0.5f;
		dmg *= luck;
		if(combo != null){
			dmg += dmg * combo.getCombo().length / 10f;  //for each combo step get 10% extra dmg
		}
		focus.getDmg(dmg);
	}
	
	
	public int getDamage(){
		return attackDmg;
	}

}

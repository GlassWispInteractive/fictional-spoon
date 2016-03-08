package combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Attacks {
	
	private String name;
	private static ArrayList<String> attackNames = new ArrayList<String>(Arrays.asList(new String[]{"Feuerwerfer", "Wasserbombe", "Pustekuchen", "Erdloch", "Schmetterball", "Traubenwerfer", "Rutschschlag", "Faustschlag", "Ausruhen", "Sein Leben chillen", "Stecker ziehen"}));
	
	
	public Attacks(String name){
		this.name = name;
	}
	public Attacks(){
		Random rnd = new Random();
		this.name = attackNames.get(rnd.nextInt(attackNames.size()-1));
	}
	
	public String getName(){
		return name;
	}

}

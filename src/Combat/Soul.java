package Combat;

public class Soul {
	
	private String name;
	private Attacks[][] attacks;
	
	public Soul(String name){
		this.name = name;
		attacks = new Attacks[][]{{new Attacks(), new Attacks()},{new Attacks(), new Attacks()}};
	}
	
	public String getName(){
		return name;
	}
	public Attacks[][] getAttacks(){
		return attacks;
	}

}

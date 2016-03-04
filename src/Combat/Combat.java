package Combat;

import java.util.ArrayList;

import entities.EntityFactory;
import entities.Monster;
import game.Events;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Combat {
	
	private static Combat singleton;
	
	private ArrayList<Souls> souls;
	private ArrayList<Monster> monster;
	
	private int curSoul = 0;

	private enum CombatState{CHOOSE_SOUL, CHOOSE_ATTACK}
	private CombatState combatState = CombatState.CHOOSE_SOUL;
	
	public static Combat startCombat(ArrayList<Souls> souls, ArrayList<Monster> monster){
		if(singleton == null){
			singleton = new Combat();
		}
//		singleton.setSouls(souls);
//		singleton.setMonster(monster);
		
		singleton.setSouls(getHardCodedSouls());
		singleton.setMonster(getHardCodedMonster());
		
		return singleton;
	}
	
	private Combat(){
		curSoul = 0;
	}
	
	private static ArrayList<Monster> getHardCodedMonster(){
		ArrayList<Monster> list = new ArrayList<Monster>();
		
		list.add(EntityFactory.getFactory().makeMonster(13, 13, 0, 0, new int[]{1,2, 3, 4, 5},  "name"));
		list.add(EntityFactory.getFactory().makeMonster(12, 23, 0, 0, new int[]{1,2, 3, 4, 5},  "test"));
		list.add(EntityFactory.getFactory().makeMonster(14, 33, 0, 0, new int[]{1,2, 3, 4, 5},  "rudolf"));
		
		return list;
	}
	
	private static ArrayList<Souls> getHardCodedSouls(){
		ArrayList<Souls> list = new ArrayList<Souls>();
		
		list.add(new Souls("der beste"));
		list.add(new Souls("dannysahne"));
		list.add(new Souls("käse"));
		
		return list;
	}
	
	private void setSouls(ArrayList<Souls> souls){
		this.souls = souls;
	}
	private void setMonster(ArrayList<Monster> monster){
		this.monster = monster;
	}
	
	
	public void tick(double elapsedTime) {
		
		Events e = Events.getEvents();

		switch (combatState) {
		case CHOOSE_SOUL:
			if (e.isLeft()){
				curSoul = (curSoul + souls.size() - 1) % souls.size();
			}
			if (e.isRight()){
				curSoul = (curSoul + 1) % souls.size();
			}			
			if(e.isEnter()){
				combatState = CombatState.CHOOSE_ATTACK;
			}			
			break;
			
		case CHOOSE_ATTACK:
			
			break;

		default:
			break;
		}
			

			
		
		Events.getEvents().clear();
		
		
	}
	
	public void render(GraphicsContext gc) {
		
		double width = gc.getCanvas().getWidth();
		double height = gc.getCanvas().getHeight();
		
		//background
		gc.setFill(Paint.valueOf("#C0C0C0"));
		gc.fillRect(0, 0, width, height);
		
		//souls at 65% height
		for(int i = 0; i < souls.size(); i++){
			gc.setFill(Color.GREEN);
			gc.fillText(souls.get(i).getName(), 50 + i*120, height * 0.65 -5, 80);
			gc.fillRect(50 + i*120, height * 0.65, 80, 80);
			
			if(curSoul == i){
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(4);
				gc.strokeRect(50 + i*120, height * 0.65, 80, 80);
			}
		}
		
		//monster at 10% height
		for(int i = 0; i < monster.size(); i++){
			gc.setFill(Color.RED);
			gc.fillText(monster.get(i).getName(), width - 150 - i*120, height * 0.1 -5, 80);
			gc.fillRect(width - 150 - i*120, height * 0.1, 80, 80);
		}
		
		
		//textboxes
		gc.setFill(Color.WHITE);
		gc.fillRect(width - 620, height * 0.65, 250, 80);
		gc.fillRect(width - 350, height * 0.65, 250, 80);
		gc.fillRect(width - 620, height * 0.65 + 100, 250, 80);
		gc.fillRect(width - 350, height * 0.65 + 100, 250, 80);
		gc.fillRect(width - 350, height * 0.65 + 200, 250, 80);
		
	}

}

package display;

import java.awt.Color;
import java.awt.Graphics2D;

import dungeon.Map;

public class Level {
	
	/**SINGELTON*/
	private static Level myLevel;
	
	//draw Map
	private Map map;
	
	int offsetX = 0, offsetY = 30, viewSizeX = 139, viewSizeY = 86;
	final int fieldSize = 10, padding = 0, rectSize = fieldSize - padding;
	private Color[] color = { Color.decode("#454545"), Color.decode("#A1D490"), Color.decode("#D4B790"),
			Color.decode("#B39B7B"), Color.decode("#801B1B"), Color.decode("#000000") };
	
	
	public static Level getLevel(Map initMap){
		if(myLevel == null){
			myLevel = new Level(initMap);
		}
		return myLevel;
	}
	
	private Level (Map initMap) {
		this.map = initMap;
	}
	
	
	public void updateMap(Map newMap){
		this.map = newMap;
	}
	
	public void changeCurrentView(int offsetChangeX, int offsetChangeY){
		this.offsetX += offsetChangeX;
		this.offsetY += offsetChangeY;
		
		if(this.offsetX < 0){
			this.offsetX = 0;
		}
		if(this.offsetY < 0){
			this.offsetY = 0;
		}
		if(this.offsetX >= map.getN() - viewSizeX){
			this.offsetX = map.getN() - viewSizeX;
		}
		if(this.offsetY >= map.getM() - viewSizeY){
			this.offsetY = map.getM() - viewSizeY;
		}
	}
	
	
	public void drawCompleteMap(Graphics2D g2){
		for (int i = 0; i < map.getN(); i++) {
			int x =  i * fieldSize;
			for (int j = 0; j < map.getM(); j++) {
				int y = j * fieldSize;
				g2.setColor(color[map.getGround(i, j).ordinal()]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}
	
	public void drawPlayerView(Graphics2D g2){
		
		for (int i = 0; i < viewSizeX; i++) {
			int x =  i * fieldSize;
			for (int j = 0; j < viewSizeY; j++) {
				int y =  j * fieldSize;
				if(i >= map.getN() || j >= map.getM()){
					System.out.println("ERROR, out of map   (in Level.drawPlayerView)");
					return;
				}
				g2.setColor(color[map.getGround(i + offsetX, j + offsetY).ordinal()]);
				g2.fillRect(x, y, rectSize, rectSize);
			}
		}
	}

}

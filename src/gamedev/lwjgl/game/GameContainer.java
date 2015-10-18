package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.Map;

public class GameContainer {

	private Map map;
	private Player player;
	
	public void init() {
		map = AssetManager.getMap("level1");
		player = new Player(100, 1000);
		
		if(map == null) {
			Logger.message("GameContainer", "Map is null, shutting down...");
			System.exit(1);
		}
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Player getPlayer() {
		return player;
	}
}

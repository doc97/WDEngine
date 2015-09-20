package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.Map;

public class GameContainer {

	private Map map;
	private Player player;
	
	public void init() {
		map = AssetManager.getMap("level1");
		player = new Player(AssetManager.getTexture("Player"), 0, 500);
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
package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.Map;

public class GameContainer {

	private Map map;
	private Player player;
	
	public void init() {
		ModelTexture background = AssetManager.getTexture("map_background");
		ModelTexture parallax1 = AssetManager.getTexture("map_parallax1");
		ModelTexture parallax2 = AssetManager.getTexture("map_parallax2");
		map = new Map(background, parallax1, parallax2);
		
		player = new Player(AssetManager.getTexture("Player"), 0, 400);
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

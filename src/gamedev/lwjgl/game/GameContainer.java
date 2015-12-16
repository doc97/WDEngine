package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.levels.Level;

public class GameContainer implements Cleanable {

	private Level currentLevel;
	private Player player;
	
	public void init() {
		player = new Player(100, 1000);
	}
	
	public void cleanup() {
		player.cleanup();
		currentLevel.unload();
		currentLevel = null;
	}
	
	public void reload() {
		player.loadDatafiles();
	}
	
	public void setCurrentLevel(Level level) {
		currentLevel = level;
	}
	
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	public Player getPlayer() {
		return player;
	}
}

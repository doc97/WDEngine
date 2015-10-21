package gamedev.lwjgl.game;

import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.levels.Level;

public class GameContainer {

	private Level currentLevel;
	private Player player;
	
	public void init() {
		player = new Player(100, 1000);
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

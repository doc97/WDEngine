package gamedev.lwjgl.game.systems;

import java.util.HashMap;
import java.util.Map;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.levels.Level;
import gamedev.lwjgl.game.levels.Level1;
import gamedev.lwjgl.game.levels.Level2;

public class LevelSystem implements Cleanable {
	private Map<String, Level> levels = new HashMap<String, Level>();
	
	public void init() {
		addLevel(new Level1());
		addLevel(new Level2());
	}
	
	private void addLevel(Level level) {
		levels.put(level.getName(), level);
	}
	
	public void changeLevel(String name) {
		Level level = levels.get(name);
		if(level != null) {
			if(Game.INSTANCE.container.getCurrentLevel() != null)
				Game.INSTANCE.container.getCurrentLevel().unload();
			Game.INSTANCE.container.setCurrentLevel(level);
			Game.INSTANCE.container.getCurrentLevel().load();
		} else {
			Logger.message("LevelSystem", "Could change to level with name: " + name);
		}
	}
	
	public void cleanup() {
		for(String s : levels.keySet()) {
			levels.get(s).cleanup();
		}
		levels.clear();
	}
}
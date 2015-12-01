package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.game.map.Map;

public abstract class Level implements Cleanable {

	protected Map map;
	protected String name = "";
	
	protected abstract void init();
	public abstract void load();
	public abstract void unload();
	
	public void update() {
		map.update();
	}
	
	public Map getMap() {
		return map;
	}
	
	public String getName() {
		return name;
	}
	
	public void cleanup() {
		map.cleanup();
	}
}
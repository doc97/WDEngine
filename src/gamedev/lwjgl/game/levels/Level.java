package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.game.map.Map;

public abstract class Level {

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
}
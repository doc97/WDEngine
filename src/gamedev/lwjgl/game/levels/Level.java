package gamedev.lwjgl.game.levels;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.map.Map;

public abstract class Level implements Cleanable {

	protected Map map;
	protected String name = "";
	protected List<Entity> mapObjects = new ArrayList<Entity>();
	
	protected abstract void init();

	public void load() {
		Game.INSTANCE.entities.addEntity(Game.INSTANCE.container.getPlayer());		
		Game.INSTANCE.physics.setMap(map);
		System.out.println(map.getName());
		Game.INSTANCE.physics.addEntity(Game.INSTANCE.container.getPlayer());
	}

	public void unload() {
		Game.INSTANCE.physics.reset();
		Game.INSTANCE.interactions.clear();
	}
	
	public void update() {
		map.update();
	}
	
	public Map getMap() {
		return map;
	}
	
	public List<Entity> getMapObjects() {
		return mapObjects;
	}
	
	public String getName() {
		return name;
	}
	
	public void cleanup() {
		map.cleanup();
	}
}
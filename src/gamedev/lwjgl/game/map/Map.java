package gamedev.lwjgl.game.map;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public abstract class Map implements Cleanable {
	protected String name;
	protected List<Line> collisionMap = new ArrayList<Line>();
	protected List<ModelTexture> textures = new ArrayList<ModelTexture>();
	protected List<Water> waters = new ArrayList<Water>();
	
	public abstract void update();
	public abstract void renderBackground(SpriteBatch batch);
	public abstract void renderGround(SpriteBatch batch);
	public abstract void renderForeground(SpriteBatch batch);
	
	public void renderWater(SpriteBatch batch) {
		for(Water w : waters)
			w.render(batch);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setWaters(List<Water> waters) {
		this.waters = waters;
	}
	
	public void setTextures(List<ModelTexture> textures) {
		this.textures = textures;
	}
	
	public void setCollisionMap(List<Line> lines) {
		collisionMap = lines;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Line> getCollisionMap() {
		return collisionMap;
	}

	public List<Water> getWaters() {
		return waters;
	}
	
	public void cleanup() {
		waters.clear();
		textures.clear();
		collisionMap.clear();
	}
}

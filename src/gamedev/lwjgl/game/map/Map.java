package gamedev.lwjgl.game.map;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.textures.TextureRegion;
import gamedev.lwjgl.game.entities.ItemType;

public class Map {
	private List<Line> collisionMap;
	private ModelTexture level;
	private ModelTexture parallax1;
	private ModelTexture parallax2;
	private List<Water> waters;
	private float offsetFactor = 0.1f;
	private float camX, camY;
	
	public Map() {
		collisionMap = new ArrayList<Line>();
		waters = new ArrayList<Water>();
	}
	
	public void update(float dt) {
		camX = Engine.INSTANCE.camera.getX();
		camY = Engine.INSTANCE.camera.getY();
		for(Water w : waters)
			w.update();
	}
	
	public void renderParallax(SpriteBatch batch) {
		batch.draw(parallax2, 300 + camX * offsetFactor * 2, camY * offsetFactor * 2, parallax2.getWidth(), parallax2.getHeight());
		batch.draw(parallax1, 600 + camX * offsetFactor, camY * offsetFactor, parallax1.getWidth(), parallax2.getHeight());
	}
	
	public void renderWater(SpriteBatch batch) {
		for(Water w : waters)
			w.render(batch);
	}
	
	public void renderLevel(SpriteBatch batch) {
		batch.draw(level, 0, 0, level.getWidth(), level.getHeight());
	}
	
	public void setWaters(List<Water> waters) {
		this.waters = waters;
	}
	
	public void setTextures(ModelTexture background, ModelTexture parallax1, ModelTexture parallax2) {
		this.level = background;
		this.parallax1 = parallax1;
		this.parallax2 = parallax2;
	}
	
	public void setCollisionMap(List<Line> lines) {
		collisionMap = lines;
	}
	
	public List<Line> getCollisionMap() {
		return collisionMap;
	}
	
	public List<Water> getWaters() {
		return waters;
	}
}

package gamedev.lwjgl.game.map;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Map {
	private List<Line> collisionMap;
	private ModelTexture ground;
	private ModelTexture background1;
	private ModelTexture background2;
	private ModelTexture foreground;
	private List<Water> waters;
	private float offsetFactor = 0.1f;
	private float camX, camY;
	
	public Map() {
		collisionMap = new ArrayList<Line>();
		waters = new ArrayList<Water>();
	}
	
	public void update() {
		camX = Engine.INSTANCE.camera.getX();
		camY = Engine.INSTANCE.camera.getY();
	}
	
	public void renderParallax(SpriteBatch batch) {
		batch.draw(background2, 300 + camX * offsetFactor * 2, camY * offsetFactor * 2, background2.getWidth(), background2.getHeight());
		batch.draw(background1, 600 + camX * offsetFactor, camY * offsetFactor, background1.getWidth(), background2.getHeight());
	}
	
	public void renderBackground(SpriteBatch batch) {
		batch.draw(background2, 0, 0, background2.getWidth(), background2.getHeight());
		batch.draw(background1, 0, 0, background1.getWidth(), background1.getHeight());
	}
	
	public void renderWater(SpriteBatch batch) {
		for(Water w : waters)
			w.render(batch);
	}
	
	public void renderGround(SpriteBatch batch) {
		batch.draw(ground, 0, 0, ground.getWidth(), ground.getHeight());
		batch.draw(foreground, 0, 0, foreground.getWidth(), foreground.getHeight());
	}
	
	public void setWaters(List<Water> waters) {
		this.waters = waters;
	}
	
	public void setTextures(ModelTexture ground, ModelTexture foreground,
			ModelTexture background1, ModelTexture background2) {
		this.ground = ground;
		this.foreground = foreground;
		this.background1 = background1;
		this.background2 = background2;
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

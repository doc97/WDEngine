package gamedev.lwjgl.game.entities;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import gamedev.lwjgl.engine.utils.AssetManager;

public class Box extends Entity {

	private float width, height;

	public Box(float x, float y, float width, float height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
	
	public void init() {
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position.set(x, y);
		bodyDef.setGravityScale(0);
		
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width / 2, height / 2);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = ps;
		fd.friction = 0.5f;
		fd.userData = "base";
		addFixtureDef(fd);
		
		addTexture(AssetManager.getTexture("white_pixel"), -width / 2, -height / 2, width, height, 0, 0, 0);
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}

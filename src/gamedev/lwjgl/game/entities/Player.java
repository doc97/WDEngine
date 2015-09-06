package gamedev.lwjgl.game.entities;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Player extends Entity {

	private Circle collisionShape;
	
	public Player(ModelTexture texture, float x, float y) {
		super(x, y);
		friction = 0.88f;
		addTexture(texture, x - texture.getWidth() / 2, y - texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 0, 0, 0);
		collisionShape = new Circle(x, y, 64);
	}

	@Override
	public void addEntityPosition(float dx, float dy) {
		super.addEntityPosition(dx, dy);
		collisionShape.addPosition(dx, dy);
	}

	@Override
	public void setEntityPosition(float x, float y) {
		super.setEntityPosition(x, y);
		collisionShape.setPosition(x, y);
	}

	public Circle getCollisionShape() {
		return collisionShape;
	}
}
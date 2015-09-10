package gamedev.lwjgl.game.entities;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.AnimatedTexture;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class Player extends Entity {

	private Circle collisionShape;
	private AnimatedTexture animation;
	
	public Player(ModelTexture texture, float x, float y) {
		super(x, y);
		friction = 0.88f;
		addTexture(texture, -250, -250, 500, 500, 0, 0, 0);
		collisionShape = new Circle(x, y, 64);
		animation = new AnimatedTexture(AssetManager.loadAnimation("Test.anim"), 60/30);
	}
	
	
	
	@Override
	public void update(float dt) {
		animation.update(dt);
		super.update(dt);
	}



	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(Color.CUSTOM.setColor(1, 1, 0, 1));
		super.setTexture(0, animation.getCurrent());
		super.render(batch);
		batch.setColor(Color.WHITE);
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
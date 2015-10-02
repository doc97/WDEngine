package gamedev.lwjgl.game.entities;

import java.util.Map;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.AnimatedTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.ui.Inventory;

public class Player extends Entity {

	private AnimatedTexture animation;

	private int radius;

	private Inventory inventory;

	
	public Player(float x, float y) {
		super(x, y);
		init();
		collisionShape = new Circle(x, y, radius);
		//animation = new AnimatedTexture(AssetManager.loadAnimation("Test.anim"), 60/30);
		dynamic = true;
		inventory = new Inventory();
	}
	
	private void init() {
		Map<String, String> data = AssetManager.getData("player");
		String coretex = data.get("coretexture");
		String rad = data.get("radius");

		radius = Integer.parseInt(rad);
		addTexture(AssetManager.getTexture(coretex), -radius, -radius / 2, 2 * radius, 2 * radius, 0, 0, 0);
	}
	
	@Override
	public void update(float dt) {
//		animation.update(dt);
		collisionShape.setPosition(x, y);
		super.update(dt);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		//super.setTexture(0, animation.getCurrent());
		super.render(batch);
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
	
	public Inventory getInventory(){
		return inventory;
	}
	
}
package gamedev.lwjgl.game.entities;

import java.util.Map;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.CollisionBox;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.ResourceSystem;
import gamedev.lwjgl.game.ui.Inventory;

public class Player extends Entity {

//	private AnimatedTexture animation;

	private Timer dashTimer = new Timer();

	private Inventory inventory;

	private ResourceSystem resources;
	
	private int sin;

	
	public Player(float x, float y) {
		super(x, y);
		init();
		//animation = new AnimatedTexture(AssetManager.loadAnimation("Test.anim"), 60/30);
		dynamic = true;
		inventory = new Inventory();
		resources = new ResourceSystem();
	}
	
	private void init() {
		Map<String, String> data = AssetManager.getData("player");
		String coretex = data.get("coretexture");
		String rad = data.get("inner");
		String rad2 = data.get("outer");
		int r1 = Integer.parseInt(rad);
		int r2 = Integer.parseInt(rad2);
 		addTexture(AssetManager.getTexture(coretex), -r1, -r1, 2 * r1, 2 * r1, 0, 0, 0);
		collisionShape = new CollisionBox(x, y, r1, r2);
	}
	
	public void dash() {
		dashTimer.set(10);
		dashTimer.setActive(true);
	}
	
	@Override
	public void update() {
		collisionShape.setInnerOffset(0, (float)(Math.sin(sin++ / 18.0f) * 10));
		dashTimer.update();
		if(dashTimer.getPercentage() == 1)
			dashTimer.setActive(false);
		if(dashTimer.isActive())
			speed.set(Math.signum(speed.x) * 5 * maxSpeed, 0);
		
		// Add particles
		int xoffset = 30;
		int yoffset = 30;
		Circle c = collisionShape.getInner();
		if (speed.x == 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (c.getPosition().x + Math.random() * xoffset - xoffset / 2),
					(float) (c.getPosition().y + Math.random() * yoffset - yoffset / 4),
					0, -0.1f, 8, 0.1f);
		} else if (speed.x < 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (c.getPosition().x + c.getRadius() / 3 + Math.random() * xoffset - xoffset / 2),
					(float) (c.getPosition().y + Math.random() * yoffset - yoffset / 4),
					0, -0.1f, 8, 0.1f);
		} else if (speed.x > 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (c.getPosition().x - c.getRadius() / 3 + Math.random() * xoffset - xoffset / 2),
					(float) (c.getPosition().y + Math.random() * yoffset - yoffset / 4),
					0, -0.1f, 6, 0.1f);
		}
		
		super.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		//super.setTexture(0, animation.getCurrent());
		super.render(batch);
	}
	
	@Override
	public void addEntityPosition(float dx, float dy) {
		super.addEntityPosition(dx, dy);
	}

	@Override
	public void setEntityPosition(float x, float y) {
		super.setEntityPosition(x, y);
	}
	
	public boolean isDashing() {
		return dashTimer.isActive();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public ResourceSystem getResources() {
		return resources;
	}
}
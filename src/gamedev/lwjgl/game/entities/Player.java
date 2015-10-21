package gamedev.lwjgl.game.entities;

import java.util.Map;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.CollisionBox;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.systems.ResourceSystem;
import gamedev.lwjgl.game.ui.Inventory;

public class Player extends Entity {

	private Inventory inventory;
	private ResourceSystem resources;
	private Dash dash;
	private float sin;
	
	public Player(float x, float y) {
		super(x, y);
		init();
		dynamic = true;
		inventory = new Inventory();
		resources = new ResourceSystem();
		dash = new Dash(this);
	}
	
	private void init() {
		Map<String, String> data = AssetManager.getData("player");
		String coretex = data.get("coretexture");
		String rad = data.get("inner");
		String rad2 = data.get("outer");
		int inner = Integer.parseInt(rad);
		int outer = Integer.parseInt(rad2);
 		addTexture(AssetManager.getTexture(coretex), -inner, -inner, 2 * inner, 2 * inner, 0, 0, 0);
		collisionShape = new CollisionBox(x, y, inner, outer);
	}
	
	public void dash() {
		if(speed.x * speed.x >= 2) {
			dash.activate();
		}
	}
	
	@Override
	public void update() {
		// Bobbing
		if(speed.lengthSquared() >= 2 && sin % Math.PI < Math.PI / 16.0f) {
			collisionShape.setInnerOffset(0, 0);
			sin = 0;
		} else {
			collisionShape.setInnerOffset(0, (float)(Math.sin(sin) * 10));
			sin += 1 / 18.0f;
		}
		
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
		
		dash.update();
		super.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		dash.render(batch);
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
		return dash.isActive();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public ResourceSystem getResources() {
		return resources;
	}
}
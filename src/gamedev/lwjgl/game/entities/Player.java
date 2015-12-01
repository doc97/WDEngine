package gamedev.lwjgl.game.entities;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.data.PlayerData;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.ui.Inventory;

public class Player extends Entity implements Cleanable {

	private Inventory inventory;
	private Dash dash;
	
	public Player(float x, float y) {
		super(x, y);
		init();
		dynamic = true;
		inventory = new Inventory();
		dash = new Dash(this);
	}
	
	private void init() {
		loadDatafiles();
	}
	
	public void cleanup() {
		inventory.cleanup();
	}
	
	public void loadDatafiles() {
		PlayerData data = AssetManager.getPlayerData();
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(x, y);
		bodyDef.setLinearDamping(1);
		
		CircleShape cs = new CircleShape();
		cs.setRadius(data.innerRadius);
		
		fixtureDef.shape = cs;
		fixtureDef.friction = 1;
		
		float inner = data.innerRadius;
 		addTexture(AssetManager.getTexture(data.texture), -inner, -inner, 2 * inner, 2 * inner, 0, 0, 0);
	}
	
	public void dash() {
		if (Game.INSTANCE.resources.getEnergy() <= 0)
			return;
		Vec2 speed = Game.INSTANCE.physics.currentEntitySpeed(this);
		if(speed.x * speed.x >= 2.0) {
			dash.activate();
			Game.INSTANCE.resources.addEnergy(-1);
		}
	}
	
	@Override
	public void update() {
		// Bobbing
//		if(speed.lengthSquared() >= 2 && sin % Math.PI < Math.PI / 16.0f) {
//			collisionShape.setInnerOffset(0, 0);
//			sin = 0;
//		} else {
//			collisionShape.setInnerOffset(0, (float)(Math.sin(sin) * 10));
//			sin += 1 / 18.0f;
//		}
		// Add particles
		System.out.println(x + "   " + y);
		int xoffset = 30;
		int yoffset = 30;
		Vec2 speed = Game.INSTANCE.physics.currentEntitySpeed(this);
		Vec2 pos = Game.INSTANCE.physics.currentEntityPosition(this);
		if (speed.x == 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (pos.x + Math.random() * xoffset - xoffset / 2),
					(float) (pos.y + Math.random() * yoffset - yoffset / 4),
					0, -0.1f, 8, 0.1f);
		} else if (speed.x < 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (pos.x + getFixtureDef().shape.getRadius() / 3 + Math.random() * xoffset - xoffset / 2),
					(float) (pos.y + Math.random() * yoffset - yoffset / 4),
					0, -0.1f, 8, 0.1f);
		} else if (speed.x > 0) {
			Game.INSTANCE.particles.createPlayerParticle(
					(float) (pos.x - getFixtureDef().shape.getRadius() / 3 + Math.random() * xoffset - xoffset / 2),
					(float) (pos.y + Math.random() * yoffset - yoffset / 4),
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
	public void setEntityPosition(float x, float y) {
		super.setEntityPosition(x, y);
	}
	
	public boolean isDashing() {
		return dash.isActive();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
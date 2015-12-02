package gamedev.lwjgl.game.entities;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Item extends Entity {
	
	private ItemType type;
	
	public Item(ItemType type, float x, float y, float scale, boolean dynamic) {
		super(x, y);
		this.type = type;
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(x, y);
		bodyDef.setLinearDamping(1);
		
		ModelTexture texture = type.getTexture();
		addTexture(texture, -texture.getWidth() / 2 * scale, -texture.getHeight() / 2 * scale, texture.getWidth() * scale, texture.getHeight() * scale, 0, 0, 0);
		
		
		CircleShape cs = new CircleShape();
		cs.setRadius((texture.getWidth() / 2) * scale);
		
		fixtureDef.shape = cs;
		fixtureDef.friction = 1;
		this.dynamic = dynamic;
	}
	
	public void update() {
		super.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(type.getTintColor());
		super.render(batch);
		batch.setColor(Color.WHITE);
	}

	public ItemType getType() {
		return type;
	}
}

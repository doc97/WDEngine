package gamedev.lwjgl.game.entities;

import gamedev.lwjgl.engine.physics.CollisionBox;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Item extends Entity{
	
	private ItemType type;
	
	public Item(ItemType type, float x, float y, float scale) {
		super(x, y);
		this.type = type;
		dynamic = true;
		ModelTexture texture = type.getTexture();
		addTexture(texture, -texture.getWidth() / 2 * scale, -texture.getHeight() / 2 * scale, texture.getWidth() * scale, texture.getHeight() * scale, 0, 0, 0);
		collisionShape = new CollisionBox(x, y, (texture.getWidth() / 2) * scale, (texture.getWidth() / 2 * scale) * 1.5f);
	}
	
	public void update(){
		collisionShape.setPosition(x, y);
		super.update();
	}
	
	
	public ItemType getType(){
		return type;
	}
}

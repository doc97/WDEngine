package gamedev.lwjgl.game.graphics.effects.particles;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class PlayerParticle {
	
	private ModelTexture texture;
	private float x, y;
	private float dx, dy;
	private float size;
	private float speed;
	
	public PlayerParticle() {
		texture = AssetManager.getTexture("player_particle");
	}
	
	public void init(float x, float y, float dx, float dy, float size, float speed) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.size = size;
		this.speed = speed;
	}
	
	public boolean update() {
		size -= speed;
		x += dx;
		y += dy;
		
		return size <= 0;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, x - size / 2, y, size, size);
		
	}
}

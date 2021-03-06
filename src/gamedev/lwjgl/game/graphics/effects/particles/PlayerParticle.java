package gamedev.lwjgl.game.graphics.effects.particles;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class PlayerParticle extends Particle {
	
	private static ModelTexture texture;
	private float size;
	private float speed;
	
	public PlayerParticle() {
		if(texture == null)
			texture = AssetManager.getTexture("player_particle_2");
	}
	
	public void init(float x, float y, float dx, float dy, float size, float speed) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.size = size;
		this.speed = speed;
	}
	
	@Override
	public boolean update() {
		size -= speed;
		x += dx;
		y += dy;
		
		return size <= 0;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, x - size / 2, y, size, size);
	}
}

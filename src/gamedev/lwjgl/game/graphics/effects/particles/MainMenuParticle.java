package gamedev.lwjgl.game.graphics.effects.particles;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class MainMenuParticle extends Particle {
	
	private static ModelTexture texture;
	private float size;
	private float speed;
	
	public MainMenuParticle() {
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
		if(size < 20)
			size += speed;
		
		x += dx;
		y += dy;

		boolean outX = x + size / 2 < 0 || x - size / 2 > Engine.INSTANCE.camera.getWidth();
		boolean outY = y + size / 2 < 0 || y - size / 2 > Engine.INSTANCE.camera.getHeight();
		System.out.println(outX + ", " + outY);
		return outX || outY;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, x - size / 2, y - size / 2, size, size);
	}
}

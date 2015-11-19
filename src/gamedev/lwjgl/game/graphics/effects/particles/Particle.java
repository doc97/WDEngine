package gamedev.lwjgl.game.graphics.effects.particles;

import gamedev.lwjgl.engine.render.SpriteBatch;

public abstract class Particle {
	
	protected float x, y;
	protected float dx, dy;
	
	public abstract boolean update();
	public abstract void render(SpriteBatch batch);
}

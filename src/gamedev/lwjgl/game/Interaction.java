package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public abstract class Interaction {

	protected ModelTexture inRangeTexture;
	protected boolean finished;
	protected boolean inRange;
	public float x, y;
	public float radius;
	
	public abstract void init();
	public abstract void interact();
	public abstract void update();
	public abstract void render(SpriteBatch batch);
	
	public boolean isFinished() {
		return finished;
	}
	
	public boolean isInRange() {
		return inRange;
	}
}

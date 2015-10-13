package gamedev.lwjgl.game.graphics;

import gamedev.lwjgl.engine.render.SpriteBatch;

public interface SceneObject {
	public void update();
	public void render(SpriteBatch batch);
}

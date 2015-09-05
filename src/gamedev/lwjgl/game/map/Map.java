package gamedev.lwjgl.game.map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Map {
	private ModelTexture background;
	private ModelTexture parallax1;
	private ModelTexture parallax2;
	private float offsetFactor = 0.1f;
	
	public Map(ModelTexture background, ModelTexture parallax1, ModelTexture parallax2) {
		this.background = background;
		this.parallax1 = parallax1;
		this.parallax2 = parallax2;
	}
	
	public void render(SpriteBatch batch) {
		float camX = Engine.INSTANCE.camera.getX();
		float camY = Engine.INSTANCE.camera.getY();
		batch.draw(parallax1, camX * offsetFactor, camY * offsetFactor, parallax1.getWidth(), parallax2.getHeight());
		batch.draw(parallax2, camX * offsetFactor * 2, camY * offsetFactor * 2, parallax2.getWidth(), parallax2.getHeight());
		batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
	}
}

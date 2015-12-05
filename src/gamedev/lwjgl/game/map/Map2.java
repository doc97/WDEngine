package gamedev.lwjgl.game.map;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Map2 extends Map {

	@Override
	public void update() {

	}

	@Override
	public void renderBackground(SpriteBatch batch) {
		ModelTexture background = textures.get(0);
		if(background != null)
			batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
	}

	@Override
	public void renderGround(SpriteBatch batch) {

	}

	@Override
	public void renderForeground(SpriteBatch batch) {

	}
}
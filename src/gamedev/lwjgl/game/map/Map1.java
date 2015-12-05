package gamedev.lwjgl.game.map;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Map1 extends Map {
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void renderBackground(SpriteBatch batch) {
		ModelTexture background2 = textures.get(0);
		ModelTexture background1 = textures.get(1);
		
		if(background2 != null)
			batch.draw(background2, 0, 0, background2.getWidth(), background2.getHeight());
		if(background1 != null)
			batch.draw(background1, 0, 0, background1.getWidth(), background1.getHeight());
	}
	
	@Override
	public void renderGround(SpriteBatch batch) {
		ModelTexture ground = textures.get(2);
		if(ground != null)
			batch.draw(ground, 0, 0, ground.getWidth(), ground.getHeight());
	}
	
	@Override
	public void renderForeground(SpriteBatch batch) {
		ModelTexture foreground = textures.get(3);
		if(foreground != null)
			batch.draw(foreground, 0, 0, foreground.getWidth(), foreground.getHeight());
	}
}
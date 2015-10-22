package gamedev.lwjgl.game.ui;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;

public class EnergyBar {
	
	private ModelTexture texture;
	private ModelTexture glow;
	private float x, y;
	private int maxEnergy;
	
	public EnergyBar(int maxEnergy, int x, int y) {
		texture = AssetManager.getTexture("UI_energy");
		glow = AssetManager.getTexture("UI_energy_glow");
		this.x = x;
		this.y = y;
		this.maxEnergy = maxEnergy;
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch batch) {
		int energy = Game.INSTANCE.resources.getEnergy();
		for(int i = 0; i < maxEnergy; i++) {
			if(i + 1 <= energy) {
				batch.setColor(10f / 255f, 240f / 255f, 200f / 255f, 1);
				batch.draw(glow, (float) (x + (i - Math.floor(maxEnergy / 2.0f)) * (texture.getWidth() + 10) - texture.getWidth() / 2),
						y - texture.getHeight() / 2, texture.getWidth(), texture.getHeight());
			} else {
				batch.setColor(Color.WHITE);
			}
			
			batch.draw(texture,
					(float) (x + (i - Math.floor(maxEnergy / 2.0f)) * (texture.getWidth() + 10) - texture.getWidth() / 2),
					y - texture.getHeight() / 2, texture.getWidth(), texture.getHeight());
		}
	}
}

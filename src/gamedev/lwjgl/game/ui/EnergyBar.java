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
	private int maxEnergy = 5;
	
	public EnergyBar(int x, int y) {
		texture = AssetManager.getTexture("UI_energy");
		glow = AssetManager.getTexture("UI_energy_glow");
		this.x = x;
		this.y = y;
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch batch) {
		int energy = Game.INSTANCE.resources.getEnergy();
		for(int i = 0; i < maxEnergy; i++) {
			if(i + 1 <= energy) {
				batch.setColor(0.125f, 0.96f, 0.73f, 1);
				batch.draw(glow, (float) (x + (i - Math.floor(maxEnergy / 2.0f)) * (texture.getWidth() + 10) - texture.getWidth() / 2),
						y - texture.getHeight() / 2, texture.getWidth(), texture.getHeight());
			} else {
				batch.setColor(Color.WHITE);
			}
			
			batch.draw(texture,
					(float) (x + (i - Math.floor(maxEnergy / 2.0f)) * (texture.getWidth() + 10) - texture.getWidth() / 2),
					y - texture.getHeight() / 2, texture.getWidth(), texture.getHeight());
				System.out.println("Render energy @: " +
						(x + (i - Math.floor(maxEnergy / 2.0f)) * (texture.getWidth() + 10) - texture.getWidth() / 2) + ", " +
						(y - texture.getHeight() / 2));
				System.out.println(texture.getWidth() + ", " + texture.getHeight());
		}
	}
}

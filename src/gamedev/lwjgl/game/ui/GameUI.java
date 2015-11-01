package gamedev.lwjgl.game.ui;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.Game;

public class GameUI {

	private EnergyBar energyBar;
	
	public GameUI() {
		energyBar = new EnergyBar(Game.INSTANCE.resources.getMaxEnergy(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 50);
	}
	
	public void update() {
		energyBar.update();
	}
	
	public void render(SpriteBatch batch) {
		energyBar.render(batch);
	}
}

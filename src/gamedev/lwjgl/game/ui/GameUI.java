package gamedev.lwjgl.game.ui;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.states.GameState;

public class GameUI {

	private GameState gs;
	private EnergyBar energyBar;
	
	public GameUI(GameState gs) {
		this.gs = gs;
		energyBar = new EnergyBar(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() - 50);
	}
	
	public void update() {
		energyBar.update();
	}
	
	public void render(SpriteBatch batch) {
		energyBar.render(batch);
	}
}

package gamedev.lwjgl.game.dev;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.input.DeveloperInput;

public class DeveloperTools {

	private DeveloperInput input = new DeveloperInput();
	
	public void init() {
		Engine.INSTANCE.input.addListener(input);
	}
	
	public void fillUpEnergy() {
		Game.INSTANCE.resources.addEnergy(Game.INSTANCE.resources.getMaxEnergy());
	}
	
	public void resetPlayer() {
		Game.INSTANCE.container.getPlayer().setEntityPosition(100, 1000);
	}
	
	public void reloadDatafiles() {
		
	}
}
package gamedev.lwjgl.game.dev;

import java.io.IOException;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;

public class DeveloperTools {

	public void fillUpEnergy() {
		Game.INSTANCE.resources.addEnergy(Game.INSTANCE.resources.getMaxEnergy());
	}
	
	public void resetPlayer() {
		Game.INSTANCE.container.getPlayer().setEntityPosition(100, 1000);
	}
	
	public void reloadDatafiles() {
		try {
			AssetManager.loadDataFiles();
		} catch (IOException e) {
			Logger.error("DeveloperTools", "Error reloading data files");
		}
		Game.INSTANCE.reload();
		Logger.debug("DeveloperTools", "Data files reloaded");
	}
}
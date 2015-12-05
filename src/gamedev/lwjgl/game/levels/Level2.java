package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;

public class Level2 extends Level {

	public Level2() {
		init();
	}

	@Override
	protected void init() {
		name = "level2";
		map = AssetManager.getMap("level2");
		if(map == null)
			Logger.error("Level2", "Failed to load map");
	}

	@Override
	public void load() {
		super.load();
		Game.INSTANCE.container.getPlayer().setEntityPosition(100, 1000);
		
		Engine.INSTANCE.camera.setUpperLimits(
				4507 - Engine.INSTANCE.camera.getWidth() / 2,
				4960 - Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.camera.setLowerLimits(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
	}

	@Override
	public void unload() {
		super.unload();
	}
}
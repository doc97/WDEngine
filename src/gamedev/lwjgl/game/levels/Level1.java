package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.Interaction;

public class Level1 extends Level {

	public Level1() {
		init();
	}
	
	@Override
	protected void init() {
		name = "level1";
		map = AssetManager.getMap("level1");
		if(map == null)
			Logger.error("Level1", "Failed to load map");
	}

	@Override
	public void load() {
		Game.INSTANCE.interactions.addInteraction(new Interaction() {
			@Override
			public void init() {
				x = 2300;
				y = 800;
				radius = 100;
			}
			
			@Override
			public void interact() {
				finished = true;
			}
			
			@Override
			public void update() {
				float dx = x - Game.INSTANCE.container.getPlayer().getX();
				float dy = y - Game.INSTANCE.container.getPlayer().getY();
				inRange = dx * dx + dy * dy <= radius * radius;
			}

			@Override
			public void render(SpriteBatch batch) {
				if(inRange) {
					// Show something to player
				}
			}
		});
	}
	
	@Override
	public void unload() {
		Game.INSTANCE.interactions.clear();
	}
}

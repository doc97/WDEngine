package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.Interaction;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.entities.ItemType;

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
				x = 3610;
				y = 110;
				radius = 120;
				inRangeTexture = AssetManager.getTexture("interact_ball");
			}
			
			@Override
			public void interact() {
				if (Game.INSTANCE.entities.getEntities().size() == 1)
					Game.INSTANCE.entities.addEntity(new Item(ItemType.ENERGY, 2300, 800, 0.1f, false));
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
					batch.draw(inRangeTexture,
							x - inRangeTexture.getWidth() / 4,
							y - inRangeTexture.getHeight() / 4,
							inRangeTexture.getWidth() / 2,
							inRangeTexture.getHeight() / 2);
				}
			}
		});
	}
	
	@Override
	public void unload() {
		Game.INSTANCE.interactions.clear();
	}
	
}

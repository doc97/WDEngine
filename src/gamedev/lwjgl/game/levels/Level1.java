package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Engine;
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
		super.load();

		Game.INSTANCE.container.getPlayer().setEntityPosition(100, 1000);
		
		Item energy = new Item(ItemType.ENERGY, 2300, 800, 0.1f, false);
		Game.INSTANCE.entities.addEntity(energy);
		Game.INSTANCE.physics.addEntity(energy);
		
		Engine.INSTANCE.camera.setUpperLimits(
				1920 * 2 - Engine.INSTANCE.camera.getWidth() / 2,
				1080 - Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.camera.setLowerLimits(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		
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
				if(!Game.INSTANCE.dialogs.getCurrentDialogKey().equals("Pirate dialog"))
					Game.INSTANCE.dialogs.setCurrentDialog("Pirate dialog");
				Game.INSTANCE.dialogs.getCurrentDialog().nextText();
				Game.INSTANCE.dialogs.getCurrentDialog().show();
				
				if (Game.INSTANCE.dialogs.getCurrentDialog().getCurrentTextIndex() == -1) {
					if(Game.INSTANCE.entities.getEntities().size() == 1) {
						Item i = new Item(ItemType.ENERGY, 2300, 800, 0.1f, false);
						Game.INSTANCE.entities.addEntity(i);
						Game.INSTANCE.physics.addEntity(i);
					}
						
				}
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
		
		Game.INSTANCE.interactions.addInteraction(new Interaction() {

			@Override
			public void init() {
				x = 530;
				y = 530;
				radius = 120;
				inRangeTexture = AssetManager.getTexture("interact_ball");
			}

			@Override
			public void interact() {
				if(!Game.INSTANCE.dialogs.getCurrentDialogKey().equals("Initial dialog"))
					Game.INSTANCE.dialogs.setCurrentDialog("Initial dialog");
				Game.INSTANCE.dialogs.getCurrentDialog().nextText();
				Game.INSTANCE.dialogs.getCurrentDialog().show();
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
		super.unload();
	}
}

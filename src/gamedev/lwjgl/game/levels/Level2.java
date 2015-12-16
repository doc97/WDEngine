package gamedev.lwjgl.game.levels;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.Interaction;
import gamedev.lwjgl.game.entities.Box;
import gamedev.lwjgl.game.entities.ItemType;

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
		Game.INSTANCE.container.getPlayer().setEntityPosition(100, 200);
		
		Engine.INSTANCE.camera.setUpperLimits(
				4507 - Engine.INSTANCE.camera.getWidth() / 2,
				4960 - Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.camera.setLowerLimits(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		
		
		// Pick-up
		Game.INSTANCE.interactions.addInteraction(new Interaction() {
			@Override
			public void init() {
				x = 250;
				y = 250;
				radius = 100;
				inRangeTexture = AssetManager.getTexture("interact_ball");
			}

			@Override
			public void interact() {
				Game.INSTANCE.container.getPlayer().getInventory().addItem(ItemType.BOX);
				Game.INSTANCE.dialogs.setCurrentDialog("L02_pickup_text");
				Game.INSTANCE.dialogs.getCurrentDialog().hide();
				finished = true;
			}

			@Override
			public void update() {
				float dx = x - Game.INSTANCE.container.getPlayer().getX();
				float dy = y - Game.INSTANCE.container.getPlayer().getY();
				inRange = dx * dx + dy * dy <= radius * radius;
				if(Game.INSTANCE.dialogs.getCurrentDialogKey() == null ||
						!Game.INSTANCE.dialogs.getCurrentDialogKey().equals("L02_pickup_text")) {
					if(inRange) {
						Game.INSTANCE.dialogs.setCurrentDialog("L02_pickup_text");
						Game.INSTANCE.dialogs.getCurrentDialog().nextText();
						Game.INSTANCE.dialogs.getCurrentDialog().show();
					}
				} else {
					if(!inRange)
						Game.INSTANCE.dialogs.setCurrentDialog(null);
				}
			}

			@Override
			public void render(SpriteBatch batch) {
				if(inRange) {
					batch.draw(inRangeTexture,
							x - inRangeTexture.getWidth() / 4,
							y - inRangeTexture.getHeight() / 4,
							inRangeTexture.getWidth() / 2,
							inRangeTexture.getHeight() / 2
							);
				}
			}
		});
		
		// Place here
		Game.INSTANCE.interactions.addInteraction(new Interaction() {
			@Override
			public void init() {
				x = 550;
				y = 750;
				radius = 100;
				inRangeTexture = AssetManager.getTexture("interact_ball");
			}

			@Override
			public void interact() {
				if(Game.INSTANCE.container.getPlayer().getInventory().contains(ItemType.BOX)) {
					Game.INSTANCE.container.getPlayer().getInventory().removeItem(ItemType.BOX);
					Box box = new Box(x + 25, y + 100, 40, 40);
					box.init();
					Game.INSTANCE.physics.addEntity(box);
					mapObjects.add(box);
					Game.INSTANCE.dialogs.setCurrentDialog("L02_place_here");
					Game.INSTANCE.dialogs.getCurrentDialog().hide();
					finished = true;
				}
			}

			@Override
			public void update() {
				float dx = x - Game.INSTANCE.container.getPlayer().getX();
				float dy = y - Game.INSTANCE.container.getPlayer().getY();
				inRange = dx * dx + dy * dy <= radius * radius;
				if(Game.INSTANCE.dialogs.getCurrentDialogKey() == null ||
						!Game.INSTANCE.dialogs.getCurrentDialogKey().equals("L02_place_here")) {
					if(inRange) {
						Game.INSTANCE.dialogs.setCurrentDialog("L02_place_here");
						if(Game.INSTANCE.container.getPlayer().getInventory().contains(ItemType.BOX))
							Game.INSTANCE.dialogs.getCurrentDialog().setCurrentText(0);
						else
							Game.INSTANCE.dialogs.getCurrentDialog().setCurrentText(1);
						Game.INSTANCE.dialogs.getCurrentDialog().show();
					}
				} else {
					if(!inRange)
						Game.INSTANCE.dialogs.setCurrentDialog(null);
				}
			}

			@Override
			public void render(SpriteBatch batch) {
				if(inRange) {
					batch.draw(inRangeTexture,
							x - inRangeTexture.getWidth() / 4,
							y - inRangeTexture.getHeight() / 4,
							inRangeTexture.getWidth() / 2,
							inRangeTexture.getHeight() / 2
							);
				}
			}
		});
	}

	@Override
	public void unload() {
		super.unload();
	}
}
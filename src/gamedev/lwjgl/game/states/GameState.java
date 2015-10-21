package gamedev.lwjgl.game.states;


import java.util.ArrayList;
import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.entities.ItemType;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.input.GameInput;
import gamedev.lwjgl.game.ui.PauseMenu;

public class GameState extends State {
	
	private Player player;
	private Font basicFont;
	private Color fadeColor = new Color(0, 0, 0, 1);
	private Timer fadeTimer = new Timer();
	private PauseMenu pauseMenu;
	private GameInput gameInput;
	private boolean initialized;
	private boolean paused;
	
	public void init() {
		Map<String, String> data = AssetManager.getData("game");
		String fontname = data.get("font");
		
		gameInput = new GameInput(this);
		
		basicFont = AssetManager.getFont(fontname);
		basicFont.setAlignment(Alignment.LEFT);
		initialized = true;
		
		pauseMenu = new PauseMenu(this);
	}
	
	public void addEntity(Entity entity) {
		Game.INSTANCE.entities.addEntity(entity);
	}
	
	public void pause() {
		paused = true;
		pauseMenu.show();
		Engine.INSTANCE.input.removeListener(gameInput);
	}
	
	public void unpause() {
		paused = false;
		pauseMenu.hide();
		Engine.INSTANCE.input.addListener(gameInput);
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	@Override
	public void update() {
		Engine.INSTANCE.update();
		if(!paused) {
//			if (Game.INSTANCE.entities.getEntities().size() < 100)
//				addEntity(new Item(ItemType.COIN, (float) (Math.random() * 3800), 500, 0.2f));
//			else if (Math.random() < 0.1)
				//addEntity(new Item(ItemType.ENERGY, (float) (Math.random() * 3800), 3000, 2));
			if(fadeTimer.isActive()) {
				fadeTimer.update();
				float value = fadeTimer.getPercentage();
				fadeColor.setColor(0.5f + value / 2, 0.5f + value / 2, 0.5f + value / 2, 1);
				Engine.INSTANCE.display.setBackgroundColor(0.1f * value, 0.1f * value, 0.2f * value, 1);
				
				if(fadeTimer.getPercentage() == 1)
					fadeTimer.setActive(false);
			}
		
			Game.INSTANCE.physics.update();
			Game.INSTANCE.container.getMap().update();
			Game.INSTANCE.particles.update();
			
			for (Entity e : Game.INSTANCE.entities.getEntities()){
				e.update();
			}
			
			Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
		} else {
			pauseMenu.update();
		}

	}
	
	@Override
	public void render() {
		
		Engine.INSTANCE.display.clearDisplay();

		Engine.INSTANCE.batch.begin();
		
		Engine.INSTANCE.batch.setColor(fadeColor);
		//Game.INSTANCE.container.getMap().renderParallax(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getMap().renderBackground(Engine.INSTANCE.batch);
		
		for(Entity entity : Game.INSTANCE.entities.getEntities()) {
			entity.render(Engine.INSTANCE.batch);
		}
		
		Game.INSTANCE.particles.render(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getMap().renderWater(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getMap().renderGround(Engine.INSTANCE.batch);
		
		ArrayList<Item> questItems = Game.INSTANCE.quests.getAllItems();
		
		for (Item item : questItems) {
			item.render(Engine.INSTANCE.batch);
		}
		
		Engine.INSTANCE.batch.setColor(Color.WHITE);

		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		
		if(paused) {
			pauseMenu.render(Engine.INSTANCE.batch);
		}
		
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
		Engine.INSTANCE.batch.end();		
		
		Engine.INSTANCE.uiBatch.begin();
		
		Game.INSTANCE.quests.render();
		Game.INSTANCE.container.getPlayer().getInventory().render(Engine.INSTANCE.uiBatch);
		
		Engine.INSTANCE.uiBatch.end();
		
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
		player = Game.INSTANCE.container.getPlayer();
		player.setEntityPosition(100, 1000);
		addEntity(player);
		Engine.INSTANCE.input.addListener(gameInput);
		
		Engine.INSTANCE.batch.setColor(fadeColor);
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		
		fadeTimer.set(60);
		fadeTimer.setActive(true);
		
		paused = false;
		
		Engine.INSTANCE.camera.setUpperLimits(
				1920 * 2 - Engine.INSTANCE.camera.getWidth() / 2,
				1080 - Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.camera.setLowerLimits(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
		
		Game.INSTANCE.sounds.loopSound(AssetManager.getSound("background"));
		
		Item energyGem = new Item(ItemType.ENERGY, 2300, 800, 0.1f, false);
		addEntity(energyGem);
	}

	@Override
	public void exit() {
		Game.INSTANCE.entities.clear();
		Engine.INSTANCE.input.removeListener(gameInput);
		pauseMenu.hide();
		Game.INSTANCE.sounds.stopSound(AssetManager.getSound("background"));
	}
}

package gamedev.lwjgl.game.states;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.data.GameData;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.input.DeveloperInput;
import gamedev.lwjgl.game.input.GameInput;
import gamedev.lwjgl.game.ui.GameUI;
import gamedev.lwjgl.game.ui.PauseMenu;

public class GameState extends State {
	
	private Font basicFont;
	private Color fadeColor = new Color(0, 0, 0, 1);
	private Timer fadeTimer = new Timer();
	private PauseMenu pauseMenu;
	private GameInput gameInput;
	private GameUI gameUI;
	private DeveloperInput devInput;
	private boolean initialized;
	private boolean paused;
	
	public void init() {
		loadData();
		gameInput = new GameInput(this);
		pauseMenu = new PauseMenu(this);
		gameUI = new GameUI();
		
		basicFont.setAlignment(Alignment.LEFT);
		initialized = true;
		
		devInput = new DeveloperInput();
	}
	
	@Override
	public void loadData() {
		GameData data = AssetManager.getGameData();
		basicFont = AssetManager.getFont(data.font);
	}

	public void addEntity(Entity entity) {
		Game.INSTANCE.entities.addEntity(entity);
	}
	
	public void pause() {
		paused = true;
		pauseMenu.show();
		gameInput.reset();
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
			if(fadeTimer.isActive()) {
				fadeTimer.update();
				float value = fadeTimer.getPercentage();
				fadeColor.setColor(0.5f + value / 2, 0.5f + value / 2, 0.5f + value / 2, 1);
				
				if(fadeTimer.getPercentage() == 1)
					fadeTimer.setActive(false);
			}
		
			Game.INSTANCE.physics.update();
			Game.INSTANCE.container.getCurrentLevel().update();
			Game.INSTANCE.particles.update();
			Game.INSTANCE.interactions.update();
			gameUI.update();
			
			for(Entity e : Game.INSTANCE.entities.getEntities())
				e.update();
			for(Entity e : Game.INSTANCE.container.getCurrentLevel().getMapObjects())
				e.update();

		} else {
			pauseMenu.update();
		}

	}
	
	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();

		// Rendering to low-res fbo for blur
		if(paused) {
			Game.INSTANCE.pprocessor.screenCapture.bindFBO();
			Game.INSTANCE.pprocessor.screenCapture.bindTexture();
		}
		
		// Render game scene
		Engine.INSTANCE.camera.setPosition(
				Game.INSTANCE.container.getPlayer().getX(),
				Game.INSTANCE.container.getPlayer().getY()
		);

		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		
		Engine.INSTANCE.batch.setColor(fadeColor);
		Game.INSTANCE.container.getCurrentLevel().getMap().renderBackground(Engine.INSTANCE.batch);
		
		for(Entity entity : Game.INSTANCE.entities.getEntities())
			entity.render(Engine.INSTANCE.batch);
		
		Game.INSTANCE.particles.render(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getCurrentLevel().getMap().renderWater(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getCurrentLevel().getMap().renderGround(Engine.INSTANCE.batch);

		for(Entity entity : Game.INSTANCE.container.getCurrentLevel().getMapObjects())
			entity.render(Engine.INSTANCE.batch);
		
		for (Item item : Game.INSTANCE.quests.getAllItems())
			item.render(Engine.INSTANCE.batch);
		
		Game.INSTANCE.interactions.render(Engine.INSTANCE.batch);
		Game.INSTANCE.container.getCurrentLevel().getMap().renderForeground(Engine.INSTANCE.batch);
		
		Engine.INSTANCE.batch.setColor(Color.WHITE);

		Engine.INSTANCE.batch.setColor(Color.WHITE);
		Game.INSTANCE.dialogs.render(Engine.INSTANCE.batch);
		
		Engine.INSTANCE.batch.setColor(Color.WHITE);

		Engine.INSTANCE.batch.end();
		
		// Render UI
		Engine.INSTANCE.uiBatch.begin();
		
		Engine.INSTANCE.uiBatch.getCamera().setPosition(
				Engine.INSTANCE.uiBatch.getCamera().getWidth() / 2,
				Engine.INSTANCE.uiBatch.getCamera().getHeight() / 2
				);
		Game.INSTANCE.quests.render();
		Game.INSTANCE.container.getPlayer().getInventory().render(Engine.INSTANCE.uiBatch);
		gameUI.render(Engine.INSTANCE.uiBatch);
		
		Engine.INSTANCE.uiBatch.end();
		if(paused) {
			ModelTexture initial = Game.INSTANCE.pprocessor.screenCapture.getTexture();
			ModelTexture blur = Game.INSTANCE.pprocessor.blur(initial, 2);
			Game.INSTANCE.pprocessor.drawToScreen(blur);
			
			// Render pause menu
			Engine.INSTANCE.camera.setPosition(
					Engine.INSTANCE.camera.getWidth() / 2,
					Engine.INSTANCE.camera.getHeight() / 2
					);
			Engine.INSTANCE.batch.begin();
			pauseMenu.render(Engine.INSTANCE.batch);
			Engine.INSTANCE.batch.end();
		}
		
		
		
		Engine.INSTANCE.display.updateDisplay();
		Game.INSTANCE.pprocessor.reset();
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
		Engine.INSTANCE.input.addListener(gameInput);
		Engine.INSTANCE.input.addListener(devInput);
		
		Engine.INSTANCE.batch.setColor(fadeColor);
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		
		Engine.INSTANCE.camera.setPosition(
						Game.INSTANCE.container.getPlayer().getX(),
						Game.INSTANCE.container.getPlayer().getY()
						);
		
		Game.INSTANCE.sounds.loopSound(AssetManager.getSound("background"));
		
		Game.INSTANCE.container.reload();
		Game.INSTANCE.levels.changeLevel("level2");
		
		fadeTimer.set(60);
		fadeTimer.setActive(true);
		paused = false;
	}
	
	@Override
	public void exit() {
		Game.INSTANCE.entities.clear();
		Game.INSTANCE.particles.clear();
		Game.INSTANCE.container.cleanup();
		Game.INSTANCE.sounds.stopSound(AssetManager.getSound("background"));
		Engine.INSTANCE.input.removeListener(gameInput);
		Engine.INSTANCE.input.removeListener(devInput);
		pauseMenu.hide();
	}
}

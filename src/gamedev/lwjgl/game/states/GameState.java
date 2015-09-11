package gamedev.lwjgl.game.states;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.input.PlayerInput;
import gamedev.lwjgl.game.map.Map;

public class GameState extends State {
	
	private Player player;
	private Map map;
	private Font basicFont;
	private boolean initialized;
	
	public GameState() {
		
	}
	
	public void init() {
		basicFont = AssetManager.getFont("basic");
		initialized = true;
	}
	
	@Override
	public void render(float dt) {
		// Update
		Engine.INSTANCE.update(dt);
		Game.INSTANCE.physics.update(player);
		Game.INSTANCE.container.getPlayer().update(dt);
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
		
		// Render
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
	
		map.render(Engine.INSTANCE.batch);
	
		for(Entity entity : Game.INSTANCE.entities.getEntities()) {
			entity.render(Engine.INSTANCE.batch);
		}
		
		basicFont.drawString("abcdefg", 32, 0, 0);
		
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	public void addEntity(Entity entity) {
		Game.INSTANCE.entities.addEntity(entity);
	}
	
	public void update() {
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
		// Map initialisation
		map = Game.INSTANCE.container.getMap();
		
		// Player initialisation
		player = Game.INSTANCE.container.getPlayer();
		addEntity(player);
		Engine.INSTANCE.input.addListener(new PlayerInput(player));
	}

	@Override
	public void exit() {
		Game.INSTANCE.entities.clear();
	}
}

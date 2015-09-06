package gamedev.lwjgl.game.states;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.GamePhysics;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.input.PlayerInput;
import gamedev.lwjgl.game.map.Map;

public class GameState extends State {
	
	private GamePhysics physics;
	private Map map;
	private Player player;
	private Font basicFont;
	private boolean initialized;
	
	public GameState() {
		
	}
	
	public void init() {
		basicFont = AssetManager.getFont("basic");
		
		ModelTexture background = AssetManager.getTexture("map_background");
		ModelTexture parallax1 = AssetManager.getTexture("map_parallax1");
		ModelTexture parallax2 = AssetManager.getTexture("map_parallax2");
		map = new Map(background, parallax1, parallax2);
		
		player = new Player(AssetManager.getTexture("Player"), 0, 0);
		
		initialized = true;
	}
	
	@Override
	public void render(float dt) {
		// Update
		Engine.INSTANCE.update(dt);
		player.update(dt);
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
		
		// Render
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		
		map.render(Engine.INSTANCE.batch);
		
		for(Entity entity : Game.INSTANCE.entities.getEntities()) {
			entity.render(Engine.INSTANCE.batch);
		}
		
		basicFont.drawString("abcdefg", 64, 0, 0);
		
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	public void addEntity(Entity entity) {
		Game.INSTANCE.entities.addEntity(entity);
	}
	
	public void update() {
		physics.update();
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
		Engine.INSTANCE.input.addListener(new PlayerInput(player));
		addEntity(player);
	}

	@Override
	public void exit() {
		Game.INSTANCE.entities.clear();
	}
}

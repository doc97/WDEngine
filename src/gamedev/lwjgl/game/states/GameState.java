package gamedev.lwjgl.game.states;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.input.PlayerInput;

public class GameState extends State {
	
	private Player player;
	private Font basicFont;
	private boolean initialized;
	
	public void init() {
		basicFont = AssetManager.getFont("basic");
		basicFont.setAlignment(Alignment.LEFT);
		initialized = true;
	}
	
	@Override
	public void loop(float dt) {
		update(dt);
		render();
	}

	public void addEntity(Entity entity) {
		Game.INSTANCE.entities.addEntity(entity);
	}
	
	private void update(float dt) {
		Engine.INSTANCE.update(dt);
		Game.INSTANCE.physics.update();
		Game.INSTANCE.container.getPlayer().update(dt);
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
	}
	
	private void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
	
		Game.INSTANCE.container.getMap().render(Engine.INSTANCE.batch);
	
		for(Entity entity : Game.INSTANCE.entities.getEntities()) {
			entity.render(Engine.INSTANCE.batch);
		}
		
		basicFont.drawString("0123456789", basicFont.getOriginalSize(), 0, 0);
		
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
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

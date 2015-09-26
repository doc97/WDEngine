package gamedev.lwjgl.game.states;

import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.input.PlayerInput;

public class GameState extends State {
	
	private Player player;
	private Font basicFont;
	private boolean initialized;
	private Color fadeColor = new Color(0, 0, 0, 1);
	private Timer fadeTimer = new Timer();
	
	public void init() {
		Map<String, String> data = AssetManager.getData("intro");
		String fontname = data.get("font");
		
		basicFont = AssetManager.getFont(fontname);
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
		if(fadeTimer.isActive()) {
			fadeTimer.update(dt);
			float value = fadeTimer.getPercentage();
			fadeColor.setColor(0.5f + value / 2, 0.5f + value / 2, 0.5f + value / 2, 1);
			Engine.INSTANCE.display.setBackgroundColor(0.1f * value, 0.1f * value, 0.2f * value, 1);
			
			if(fadeTimer.getPercentage() == 1)
				fadeTimer.setActive(false);
		}
	
		Engine.INSTANCE.update(dt);
		Game.INSTANCE.physics.update();
		Game.INSTANCE.container.getPlayer().update(dt);
		Engine.INSTANCE.camera.setPosition(player.getX(), player.getY());
	}
	
	private void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.setColor(fadeColor);

		Game.INSTANCE.container.getMap().render(Engine.INSTANCE.batch);
	
		for(Entity entity : Game.INSTANCE.entities.getEntities()) {
			entity.render(Engine.INSTANCE.batch);
		}
		
		basicFont.drawString("Wille, Dani ja Reetu!?_,", basicFont.getOriginalSize(), 0, 300);

		Engine.INSTANCE.batch.setColor(Color.WHITE);
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
		
		Engine.INSTANCE.batch.setColor(fadeColor);
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		
		fadeTimer.set(60);
		fadeTimer.setActive(true);
	}

	@Override
	public void exit() {
		Game.INSTANCE.entities.clear();
	}
}

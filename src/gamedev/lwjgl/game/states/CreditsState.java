package gamedev.lwjgl.game.states;

import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.states.StateSystem.States;

public class CreditsState extends State {

	private CreditsInput input;
	
	private Font font;
	private float offsetY;
	private boolean initialized;
	private boolean exit;
	
	private static final String title = "CREDITS";
	private static final String programmersTitle = "PROGRAMMERS";
	private static final String artistsTitle = "ARTISTS";
	private static final String storyTitle = "STORY";
	private static final String gameDesignTitle = "GAME DESIGN";
	private static final String levelTitle = "LEVEL DESIGN";
	
	private static final String prog1 = "Daniel Riissanen";
	private static final String prog2 = "Wilhelm von Bergmann";
	private static final String artist1 = "Reetu Laine";
	private static final String artist2 = "Charlotta ";
	private static final String writer = "Jolanda Riissanen";
	
	private void init() {
		initialized = true;
		
		input = new CreditsInput(this);
		
		Map<String, String> data = AssetManager.getData("credits");
		font = AssetManager.getFont(data.get("font"));
		font.setAlignment(Alignment.CENTER);
	}
	
	public void setExit(boolean b) {
		exit = b;
	}
	
	@Override
	public void enter() {
		if(!initialized)
			init();
		
		Engine.INSTANCE.input.addListener(input);
		
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		offsetY = -Engine.INSTANCE.display.getWindowHeight() + 50;
		exit = false;
	}

	@Override
	public void update() {
		Engine.INSTANCE.update();
		
		if(offsetY > 900 || exit)
			Game.INSTANCE.states.enterState(States.MAINMENUSTATE);
		
		offsetY += 0.7f;
	}

	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		
		font.drawString(Engine.INSTANCE.batch, title, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 100 + offsetY
				);
		
		// Programmers
		font.drawString(Engine.INSTANCE.batch, programmersTitle, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 200 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, prog1, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 225 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, prog2, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 250 + offsetY
				);
		
		// Artists
		font.drawString(Engine.INSTANCE.batch, artistsTitle, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 350 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, artist1, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 375 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, artist2, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 400 + offsetY
				);
		
		// Game design
		font.drawString(Engine.INSTANCE.batch, gameDesignTitle, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 500 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, prog1, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 525 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, artist1, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 550 + offsetY
				);
		
		// Story
		font.drawString(Engine.INSTANCE.batch, storyTitle, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 650 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, writer, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 675 + offsetY
				);
		
		// Level design
		font.drawString(Engine.INSTANCE.batch, levelTitle, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 775 + offsetY
				);
		
		font.drawString(Engine.INSTANCE.batch, artist1, font.getOriginalSize(),
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() - 800 + offsetY
				);
		
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void exit() {
		Engine.INSTANCE.input.removeListener(input);
	}

}

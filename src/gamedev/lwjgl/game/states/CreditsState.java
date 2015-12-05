package gamedev.lwjgl.game.states;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.data.CreditsData;
import gamedev.lwjgl.engine.data.CreditsTextData;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.systems.StateSystem.States;

public class CreditsState extends State {

	private CreditsInput input;
	
	private Font font;
	private float scrollY;
	private float lengthY;
	private float scale;
	private boolean initialized;
	private boolean exit;
	
	private CreditsTextData[] texts;
	
	private void init() {
		initialized = true;
		loadData();
		input = new CreditsInput(this);
	}
	
	public void setExit(boolean b) {
		exit = b;
	}
	
	@Override
	public void loadData() {
		CreditsData data = AssetManager.getCreditsData();
		font = AssetManager.getFont(data.font);
		scale = font.getOriginalSize() / 16;
		
		texts = data.texts;
		for(int i = 0; i < texts.length; i++) {
			String type = texts[i].type;
			if(type.equals("title"))
				lengthY -= 75;
			else if(type.equals("content"))
				lengthY -= 25;
		}
	}

	@Override
	public void enter() {
		if(!initialized)
			init();
		
		Engine.INSTANCE.input.addListener(input);
		scrollY = -Engine.INSTANCE.display.getWindowHeight() + 150;
		exit = false;
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		font.setAlignment(Alignment.CENTER);
		font.setFadeEffect(false);
	}

	@Override
	public void update() {
		Engine.INSTANCE.update();
		
		if(scrollY > (-lengthY + 50) * scale || exit)
			Game.INSTANCE.states.enterState(States.MAINMENUSTATE);
		
		scrollY += 0.7f * scale / 2;
	}

	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		
		float offsetY = 0;
		float fontSizeFactor = 0;
		for(int i = 0; i < texts.length; i++) {
			String type = texts[i].type;
			if(type.equals("title")) {
				offsetY -= 75;
				fontSizeFactor = 1.0f;
			} else if(type.equals("content")) {
				offsetY -= 25;
				fontSizeFactor = 0.5f;
			}
			
			font.drawString(Engine.INSTANCE.batch, texts[i].text, (int) (fontSizeFactor * font.getOriginalSize()),
					Engine.INSTANCE.camera.getWidth() / 2,
					Engine.INSTANCE.camera.getHeight() + offsetY * scale + scrollY);
		}
		
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void exit() {
		scrollY = -Engine.INSTANCE.display.getWindowHeight() + 150;
		exit = false;
		Engine.INSTANCE.input.removeListener(input);
	}
}
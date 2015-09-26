package gamedev.lwjgl.game.states;

import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.textures.AnimatedTexture;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.states.StateSystem.States;

public class IntroState extends State {

	//private AnimatedTexture introCutscene;
	private ModelTexture texture;
	private Font font;
	private Color color = new Color(1, 1, 1, 1);
	private Timer timer = new Timer();
	private Timer fadeTimer = new Timer();
	private Timer blackTimer = new Timer();
	
	public IntroState() {
		Map<String, String> data = AssetManager.getData("intro");
		String cutscene = data.get("cutscene");
		String texname= data.get("introtexture");
		
		//introCutscene = new AnimatedTexture(AssetManager.getAnimationTexture(cutscene), 60 / 30, false);
		texture = AssetManager.getTexture(texname);
		font = AssetManager.getFont("font01_2");
		font.setAlignment(Alignment.LEFT);
	}
	
	@Override
	public void enter() {
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.display.getWindowWidth() / 2,
				Engine.INSTANCE.display.getWindowHeight() / 2
				);
		color.setColor(1, 1, 1, 1);
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		timer.set(240);
		fadeTimer.set(60);
		blackTimer.set(60);
		timer.setActive(true);
	}
	
	public void update(float dt) {
		if(timer.isActive()) {
			timer.update(dt);
			if(timer.getPercentage() == 1) {
				fadeTimer.setActive(true);
				timer.setActive(false);
			}
		}
		if(fadeTimer.isActive()) {
			fadeTimer.update(dt);
			float value = 1 - fadeTimer.getPercentage();
			color.setColor(value, value, value, 1);
			if(fadeTimer.getPercentage() == 1) {
				blackTimer.setActive(true);
				fadeTimer.setActive(false);
			}
		}
		if(blackTimer.isActive()) {
			blackTimer.update(dt);
			if(blackTimer.getPercentage() == 1) {
				blackTimer.setActive(false);
				Game.INSTANCE.states.enterState(States.GAMESTATE);
			}
		}
		
		Engine.INSTANCE.camera.addPosition(0.3f, 0);
	}
	
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.setColor(color);
		
		Engine.INSTANCE.batch.draw(texture, -100, -100,
				Engine.INSTANCE.camera.getWidth() + 200, Engine.INSTANCE.camera.getHeight() + 200);
		
		float textX = 100 + Engine.INSTANCE.camera.getX() - Engine.INSTANCE.camera.getWidth() / 2;
		float textY = 100 + Engine.INSTANCE.camera.getY() - Engine.INSTANCE.camera.getHeight() / 2;
		font.drawString("THE CASUALS", font.getOriginalSize(), textX, textY);
		
		Color color = Engine.INSTANCE.batch.getColor();
		color.setColor(color.r, color.g, color.b, (timer.getCurrentTime() - 60) / 120);
		font.drawString("presents", font.getOriginalSize(), textX, textY - font.getOriginalSize());

		Engine.INSTANCE.batch.setColor(1, 1, 1, 1);
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void loop(float dt) {
		update(dt);
		render();
	}

	@Override
	public void exit() {
	}

}

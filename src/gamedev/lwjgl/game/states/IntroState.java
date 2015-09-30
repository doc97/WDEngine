package gamedev.lwjgl.game.states;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.util.List;
import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.textures.AnimatedTexture;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.textures.TextureRegion;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.states.StateSystem.States;

public class IntroState extends State {

	private AnimatedTexture introCutscene;
	private ModelTexture texture1, texture2;
	private Font font;
	private Color color = new Color(1, 1, 1, 1);
	private InputListener introInput;
	private Timer showTimer = new Timer();
	private Timer fadeOutTimer = new Timer();
	private Timer fadeInTimer = new Timer();
	private Timer blackTimer = new Timer();
	private int scene;
	private int fontFades;
	private float fontOffsetX;
	
	public IntroState() {
		Map<String, String> data = AssetManager.getData("intro");
		String cutscene = data.get("cutscene");
		String texname1 = data.get("introtexture1");
		String texname2 = data.get("introtexture2");
		String fontname = data.get("font");

		List<TextureRegion> frames = AssetManager.getAnimationFrames(cutscene);
		introCutscene = new AnimatedTexture(frames, 60 / 1, false);
		texture1 = AssetManager.getTexture(texname1);
		texture2 = AssetManager.getTexture(texname2);
		font = AssetManager.getFont(fontname);
		font.setAlignment(Alignment.LEFT);
		
		introInput = new InputListener() {
			@Override
			public void update() {}
			
			@Override
			public boolean mouseReleased(int button) { return false; }
			
			@Override
			public boolean mousePressed(int button) { return false;	}
			
			@Override
			public boolean keyRepeat(int key) { return false; }
			
			@Override
			public boolean keyReleased(int key) { return false;	}
			
			@Override
			public boolean keyPressed(int key) {
				if(key == GLFW_KEY_ESCAPE) {
					Game.INSTANCE.states.enterState(States.GAMESTATE);
				}
				return false;
			}
		};
	}
	
	@Override
	public void enter() {
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		color.setColor(1, 1, 1, 1);
		Engine.INSTANCE.display.setBackgroundColor(0, 0, 0, 1);
		Engine.INSTANCE.input.addListener(introInput);
		
		scene = 0;
		fontFades = 0;
		fadeInTimer.set(60);
		fadeInTimer.setActive(true);
		font.setFadeTimer(60);
		font.setFadeEffect(true);
	}
	
	public void update(float dt) {
		Engine.INSTANCE.update(dt);
		if(showTimer.isActive()) {
			showTimer.update(dt);
			if(showTimer.getPercentage() == 1) {
				showTimer.setActive(false);
				fadeOutTimer.set(60);
				fadeOutTimer.setActive(true);
				scene++;
			}
		}
		if(fadeOutTimer.isActive()) {
			fadeOutTimer.update(dt);
			float value = 1 - fadeOutTimer.getPercentage();
			color.setColor(value, value, value, 1);
			if(fadeOutTimer.getPercentage() == 1) {
				Engine.INSTANCE.camera.setPosition(
						Engine.INSTANCE.camera.getWidth() / 2,
						Engine.INSTANCE.camera.getHeight() / 2
						);
				
				fadeOutTimer.setActive(false);
				if(scene == 2) {
					fadeInTimer.set(60);
					fadeInTimer.setActive(true);
					font.setAlignment(Alignment.CENTER);
				} else if(scene == 5) {
					fadeInTimer.set(60);
					fadeInTimer.setActive(true);
				} else if(scene == 8) {
					blackTimer.set(60);
					blackTimer.setActive(true);
				}
				scene++;
			}
		}
		if(fadeInTimer.isActive()) {
			fadeInTimer.update(dt);
			float value = fadeInTimer.getPercentage();
			if(scene > 0)
				color.setColor(value, value, value, 1);
			
			if(fadeInTimer.getPercentage() == 1) {
				fadeInTimer.setActive(false);
				
				if(scene == 0) {
					showTimer.set(300);
					font.getFadeTimer().setActive(true);
				} else if(scene == 6) {
					showTimer.set(introCutscene.getLength());
				} else {
					showTimer.set(180);
				}
				
				showTimer.setActive(true);
				scene++;
			}
		}
		if(blackTimer.isActive()) {
			blackTimer.update(dt);
			if(blackTimer.getPercentage() == 1) {
				blackTimer.setActive(false);
				Game.INSTANCE.states.enterState(States.GAMESTATE);
			}
		}
		
		if(scene > 5 && scene < 9)
			introCutscene.update(dt);

		font.update(dt);
		fontOffsetX += 0.1f;
	}
	
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.setColor(color);
		
		if(scene < 3) {
			Engine.INSTANCE.batch.draw(texture1, 0, 0,
					Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			
			float textX = 100 + Engine.INSTANCE.camera.getX() - Engine.INSTANCE.camera.getWidth() / 2 + fontOffsetX;
			float textY = 100 + Engine.INSTANCE.camera.getY() - Engine.INSTANCE.camera.getHeight() / 2;

			if(font.getFadeTimer().getPercentage() == 1) {
				fontFades++;
				font.getFadeTimer().set(120);
				font.getFadeTimer().setActive(true);
			}
			
			font.setFadeEffect(fontFades == 0);
			font.drawString("THE CASUALS", font.getOriginalSize(), textX, textY);
			
			// Uncomment to use whole text fade in
			/*Color color = Engine.INSTANCE.batch.getColor();
			color.setColor(color.r, color.g, color.b, (showTimer.getCurrentTime() - 60) / 120);*/
			
			if(fontFades > 0) {
				font.setFadeEffect(fontFades == 1);
				font.drawString("present", font.getOriginalSize(), textX, textY - font.getOriginalSize());
			}
		} else if(scene < 6) {
			Engine.INSTANCE.batch.draw(texture2, -100, -100,
					Engine.INSTANCE.camera.getWidth() + 200, Engine.INSTANCE.camera.getHeight() + 200);
			
			float textX = Engine.INSTANCE.camera.getX();
			float textY = Engine.INSTANCE.camera.getY();
			font.drawString("Wisp Of The Willow", font.getOriginalSize() * 4, textX, textY);
			font.drawString("", font.getOriginalSize() * 4, textX, textY);
		} else if(scene < 9) {
			TextureRegion frame = introCutscene.getCurrent();
			if(frame != null) {
				Engine.INSTANCE.batch.draw(introCutscene.getCurrent(), -100, -100,
					Engine.INSTANCE.camera.getWidth() + 200, Engine.INSTANCE.camera.getHeight() + 200);
			}
		}
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
		Engine.INSTANCE.input.removeListener(introInput);
	}
}

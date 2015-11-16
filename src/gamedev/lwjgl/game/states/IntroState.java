package gamedev.lwjgl.game.states;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.data.IntroData;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.graphics.Cutscene;
import gamedev.lwjgl.game.graphics.Scene;
import gamedev.lwjgl.game.graphics.SceneText;
import gamedev.lwjgl.game.graphics.SceneTexture;
import gamedev.lwjgl.game.systems.StateSystem.States;

public class IntroState extends State {

	private Cutscene introScene = new Cutscene();
	private Font font;
	private Color color = new Color(1, 1, 1, 1);
	private InputListener introInput;
	
	public IntroState() {
		loadData();
		
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
					return true;
				}
				return false;
			}
		};
	}
	
	@Override
	public void loadData() {
		IntroData data = AssetManager.getIntroData();
		
		font = AssetManager.getFont(data.font);
		font.setAlignment(Alignment.CENTER);

		Scene scene1 = new Scene(0, 0, Timer.getTicks(2000));
			SceneTexture s1Tex1 = new SceneTexture();
			s1Tex1.setTexture(AssetManager.getTexture(AssetManager.getMainMenuData().title_screen));
			s1Tex1.setPosition(0, 0);
			s1Tex1.setDimension(Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			scene1.addObject("Fade_texture", s1Tex1);
		introScene.addScene("Fade_scene", scene1);
			
		Scene scene2 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(1000));
			SceneText s2Text1 = new SceneText();
			s2Text1.setFont(font);
			s2Text1.setFontSize(font.getOriginalSize());
			s2Text1.setPosition(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() / 2);
			s2Text1.setText("The Casuals");
			scene2.addObject("Team_name", s2Text1);
		introScene.addScene("Team_scene", scene2);
		
		Scene scene3 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(2000));
			SceneText s3Text1 = new SceneText();
			s3Text1.setFont(font);
			s3Text1.setFontSize(font.getOriginalSize());
			s3Text1.setPosition(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() / 2 + font.getOriginalSize());
			s3Text1.setText("in association with");
			scene3.addObject("Association_text", s3Text1);
			
			SceneText s3Text2 = new SceneText();
			s3Text2.setFont(font);
			s3Text2.setFontSize(font.getOriginalSize());
			s3Text2.setPosition(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() / 2 - font.getOriginalSize());
			s3Text2.setText("GameDev-klubi");
			scene3.addObject("Club_text", s3Text2);
		introScene.addScene("Association_scene", scene3);
		
		Scene scene4 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(2000));
			SceneTexture s4Text1 = new SceneTexture();
			s4Text1.setTexture(AssetManager.getTexture(data.frame1));
			s4Text1.setPosition(0, 0);
			s4Text1.setDimension(Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			scene4.addObject("Intro_1", s4Text1);
		introScene.addScene("Intro_scene_1", scene4);
		
		Scene scene5 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(2000));
			SceneTexture s5Tex1 = new SceneTexture();
			s5Tex1.setTexture(AssetManager.getTexture(data.frame2));
			s5Tex1.setPosition(0, 0);
			s5Tex1.setDimension(Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			scene5.addObject("Intro_2", s5Tex1);
		introScene.addScene("Intro_scene_2", scene5);
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
		
		font.setAlignment(Alignment.CENTER);
		font.setFadeTimer(Timer.getTicks(60));
		font.setFadeEffect(true);
		
		introScene.start();
	}
	
	@Override
	public void update() {
		Engine.INSTANCE.update();

		introScene.update();
		if(introScene.hasFinished())
			Game.INSTANCE.states.enterState(States.GAMESTATE);
		
		font.update();
	}
	
	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.setColor(color);
		
		introScene.render(Engine.INSTANCE.batch);
		
		Engine.INSTANCE.batch.setColor(1, 1, 1, 1);
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}

	@Override
	public void exit() {
		Engine.INSTANCE.input.removeListener(introInput);
	}
}

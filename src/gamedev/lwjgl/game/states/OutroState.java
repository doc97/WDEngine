package gamedev.lwjgl.game.states;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.data.OutroData;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.graphics.Cutscene;
import gamedev.lwjgl.game.graphics.Scene;
import gamedev.lwjgl.game.graphics.SceneTexture;
import gamedev.lwjgl.game.systems.StateSystem.States;

public class OutroState extends State {

	private Cutscene outroScene = new Cutscene();
	private InputListener outroInput;

	public OutroState() {
		loadData();
		
		outroInput = new InputListener() {
			@Override
			public void update() {}

			@Override
			public boolean keyPressed(int key) {
				if(key == GLFW_KEY_ESCAPE) {
					Game.INSTANCE.states.enterState(States.MAINMENUSTATE);
					return true;
				}
				return false;
			}

			@Override
			public boolean keyRepeat(int key) {	return false; }

			@Override
			public boolean keyReleased(int key) { return false;	}

			@Override
			public boolean mousePressed(int button) { return false;	}

			@Override
			public boolean mouseReleased(int button) { return false; }
		};
	}
	
	@Override
	public void loadData() {
		OutroData data = AssetManager.getOutroData();
		outroScene.clear();
		
		Scene scene1 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(2000));
			SceneTexture s1Tex1 = new SceneTexture();
			s1Tex1.setTexture(AssetManager.getTexture(data.frames[0]));
			s1Tex1.setPosition(0, 0);
			s1Tex1.setDimension(Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			scene1.addObject("Outro_1", s1Tex1);
		outroScene.addScene("Outro_scene_1", scene1);
		
		Scene scene2 = new Scene(Timer.getTicks(2000), Timer.getTicks(1000), Timer.getTicks(2000));
			SceneTexture s2Tex1 = new SceneTexture();
			s2Tex1.setTexture(AssetManager.getTexture(data.frames[1]));
			s2Tex1.setPosition(0, 0);
			s2Tex1.setDimension(Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
			scene2.addObject("Outro_2", s2Tex1);
		outroScene.addScene("Outro_scene_2", scene2);
	}

	@Override
	public void enter() {
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		Engine.INSTANCE.input.addListener(outroInput);
		outroScene.start();
	}

	@Override
	public void exit() {
		Engine.INSTANCE.input.removeListener(outroInput);
	}

	@Override
	public void update() {
		Engine.INSTANCE.update();
		outroScene.update();
		if(outroScene.hasFinished())
			Game.INSTANCE.states.enterState(States.MAINMENUSTATE);
	}

	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		outroScene.render(Engine.INSTANCE.batch);
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}
}
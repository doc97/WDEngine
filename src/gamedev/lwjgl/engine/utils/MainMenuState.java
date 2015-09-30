package gamedev.lwjgl.engine.utils;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.Map;

import org.joml.Vector4f;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.states.State;
import gamedev.lwjgl.game.states.StateSystem.States;
import gamedev.lwjgl.game.states.Timer;
import gamedev.lwjgl.game.ui.Button;

public class MainMenuState extends State {

	private ModelTexture titleScreen;
	private Button startBtn;
	private Button optionsBtn;
	private Button exitBtn;
	private InputListener startBtnInput;
	private InputListener optionsBtnInput;
	private InputListener exitBtnInput;
	private Color color = new Color(1, 1, 1, 1);
	private Timer fadeTimer = new Timer();
	private boolean removeListeners;
	
	public MainMenuState() {
		Map<String, String> data = AssetManager.getData("mainmenu");
		String titleScreenTex = data.get("title_screen");
		String startBtnTex = data.get("start_button");
		String optionsBtnTex = data.get("options_button");
		String exitBtnTex = data.get("exit_button");
		
		titleScreen = AssetManager.getTexture(titleScreenTex);
		
		Vector4f startNormal = new Vector4f(0, 0, 128, 64);
		Vector4f startPressed= new Vector4f(129, 0, 128, 64);
		startBtn = new Button(startBtnTex, startNormal, startPressed);
		startBtn.setPosition(100, 64 + 400);
		startBtn.setSize(128, 64);
		startBtnInput = new InputListener() {
			
			@Override
			public void update() {}
			
			@Override
			public boolean keyRepeat(int key) {	return false; }
			
			@Override
			public boolean keyReleased(int key) { return false; }
			
			@Override
			public boolean keyPressed(int key) { return false; }

			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						startBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					startBtn.press();
				}
				return false;
			}

			@Override
			public boolean mouseReleased(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT) {
					startBtn.release();
					if(startBtn.isOver(
							Engine.INSTANCE.input.getTranslatedMouseX(),
							Engine.INSTANCE.input.getTranslatedMouseY()
							))
					{
						fadeTimer.setActive(true);
						removeListeners = true;
					}
				}
				return false;
			}
		};
		
		Vector4f optionsNormal = new Vector4f(0, 0, 128, 64);
		Vector4f optionsPressed = new Vector4f(129, 0, 128, 64);
		optionsBtn = new Button(optionsBtnTex, optionsNormal, optionsPressed);
		optionsBtn.setPosition(100, 64 + 250);
		optionsBtn.setSize(128, 64);
		optionsBtnInput = new InputListener() {
			
			@Override
			public void update() {}
			
			@Override
			public boolean keyRepeat(int key) {	return false; }
			
			@Override
			public boolean keyReleased(int key) { return false;	}
			
			@Override
			public boolean keyPressed(int key) { return false; }

			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						optionsBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					optionsBtn.press();
				}
				return false;
			}

			@Override
			public boolean mouseReleased(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT)
					optionsBtn.release();
				return false;
			}
		};
		
		Vector4f exitNormal = new Vector4f(0, 0, 128, 64);
		Vector4f exitPressed = new Vector4f(129, 0, 128, 64);
		exitBtn = new Button(exitBtnTex, exitNormal, exitPressed);
		exitBtn.setPosition(100, 64 + 100);
		exitBtn.setSize(128, 64);
		exitBtnInput = new InputListener() {
			
			@Override
			public void update() {}
			
			@Override
			public boolean keyRepeat(int key) {	return false; }
			
			@Override
			public boolean keyReleased(int key) { return false;	}
			
			@Override
			public boolean keyPressed(int key) { return false; }

			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						exitBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					exitBtn.press();
				}
				return false;
			}

			@Override
			public boolean mouseReleased(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT) {
					exitBtn.release();
					if(exitBtn.isOver(
							Engine.INSTANCE.input.getTranslatedMouseX(),
							Engine.INSTANCE.input.getTranslatedMouseY()
							))
					{
						glfwSetWindowShouldClose(Engine.INSTANCE.display.getWindow(), GL_TRUE);
					}
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
		
		Engine.INSTANCE.input.addListener(startBtnInput);
		Engine.INSTANCE.input.addListener(optionsBtnInput);
		Engine.INSTANCE.input.addListener(exitBtnInput);
		
		fadeTimer.set(120);
		color.setColor(1, 1, 1, 1);
	}

	public void update(float dt) {
		Engine.INSTANCE.update(dt);
		
		if(fadeTimer.isActive()) {
			fadeTimer.update(dt);
			float value = 1 - fadeTimer.getPercentage();
			color.setColor(1, 1, 1, value);
			if(fadeTimer.getPercentage() == 1) {
				fadeTimer.setActive(false);
				Game.INSTANCE.states.enterState(States.INTROSTATE);
			}
			if(removeListeners) {
				removeListeners = false;
				Engine.INSTANCE.input.removeListener(startBtnInput);
				Engine.INSTANCE.input.removeListener(optionsBtnInput);
				Engine.INSTANCE.input.removeListener(exitBtnInput);
			}
		}
	}
	
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(titleScreen, 0, 0,
				Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.setColor(color);
		startBtn.render(Engine.INSTANCE.batch);
		optionsBtn.render(Engine.INSTANCE.batch);
		exitBtn.render(Engine.INSTANCE.batch);
		Engine.INSTANCE.batch.setColor(Color.WHITE);
		Engine.INSTANCE.batch.end();
		Engine.INSTANCE.display.updateDisplay();
	}
	
	@Override
	public void loop(float dt) {
		update(dt);
		render();
	}

	@Override
	public void exit() {}
}
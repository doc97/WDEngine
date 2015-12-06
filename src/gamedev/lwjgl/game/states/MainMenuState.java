package gamedev.lwjgl.game.states;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector4f;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.data.MainMenuData;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.AnimatedTexture;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.textures.TextureRegion;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.systems.StateSystem.States;
import gamedev.lwjgl.game.ui.Button;

public class MainMenuState extends State {

	private ModelTexture titleScreen;
	private AnimatedTexture titleText;
	private Button startBtn;
	private Button optionsBtn;
	private Button exitBtn;
	private InputListener startBtnInput;
	private InputListener optionsBtnInput;
	private InputListener exitBtnInput;
	private Color color = new Color(1, 1, 1, 1);
	private Timer fadeTimer = new Timer();
	private States enterState;
	
	public MainMenuState() {
		loadData();
	}
	
	@Override
	public void loadData() {
		MainMenuData data = AssetManager.getMainMenuData();
		
		// Title screen
		titleScreen = AssetManager.getTexture(data.title_screen);

		// Title text
		titleText = new AnimatedTexture(AssetManager.getAnimationFrames(data.title_text), Timer.getTicks(1000.0f / 6.0f), true);
		
		// Start button data
		Vector4f startNormal = new Vector4f(data.play_btn.normalx, data.play_btn.normaly, data.play_btn.normalwidth, data.play_btn.normalheight);
		Vector4f startPressed = new Vector4f(data.play_btn.pressedx, data.play_btn.pressedy, data.play_btn.pressedwidth, data.play_btn.pressedheight);
		startBtn = new Button(data.play_btn.texture, startNormal, startPressed);
		startBtn.setPosition(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() * 11 / 20);
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
					return true;
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
						enterState = States.INTROSTATE;
						return true;
					}
				}
				return false;
			}
		};
		

		// Credits button data
		Vector4f creditsNormal = new Vector4f(data.credits_btn.normalx, data.credits_btn.normaly, data.credits_btn.normalwidth, data.credits_btn.normalheight);
		Vector4f creditsPressed= new Vector4f(data.credits_btn.pressedx, data.credits_btn.pressedy, data.credits_btn.pressedwidth, data.credits_btn.pressedheight);
		optionsBtn = new Button(data.credits_btn.texture, creditsNormal, creditsPressed);
		optionsBtn.setPosition(Engine.INSTANCE.camera.getWidth() / 2.0f, Engine.INSTANCE.camera.getHeight() * 2 / 5);
		optionsBtn.setSize(203, 47);
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
					return true;
				}
				return false;
			}

			@Override
			public boolean mouseReleased(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT) {
					optionsBtn.release();
					if(optionsBtn.isOver(
							Engine.INSTANCE.input.getTranslatedMouseX(),
							Engine.INSTANCE.input.getTranslatedMouseY()
							))
					{
						fadeTimer.setActive(true);
						enterState = States.CREDITSSTATE;
						return true;
					}
				}
				return false;
			}
		};
		
		
		// Exit button data
		Vector4f exitNormal = new Vector4f(data.exit_btn.normalx, data.exit_btn.normaly, data.exit_btn.normalwidth, data.exit_btn.normalheight);
		Vector4f exitPressed= new Vector4f(data.exit_btn.pressedx, data.exit_btn.pressedy, data.exit_btn.pressedwidth, data.exit_btn.pressedheight);
		exitBtn = new Button(data.exit_btn.texture, exitNormal, exitPressed);
		exitBtn.setPosition(Engine.INSTANCE.camera.getWidth() / 2, Engine.INSTANCE.camera.getHeight() / 4);
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
					return true;
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
						Engine.INSTANCE.display.closeDisplay();
						return true;
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
		Game.INSTANCE.sounds.loopSound(AssetManager.getSound("atmospheric_water"));
		fadeTimer.set(Timer.getTicks(1200));
		color.setColor(1, 1, 1, 1);
	}

	@Override
	public void update() {
		Engine.INSTANCE.update();
		titleText.update();
		
		if(fadeTimer.isActive()) {
			fadeTimer.update();
			float value = 1 - fadeTimer.getPercentage();
			color.setColor(1, 1, 1, value);
			if(fadeTimer.getPercentage() == 1) {
				fadeTimer.setActive(false);
				Game.INSTANCE.states.enterState(enterState);
			}
		}
	}
	
	@Override
	public void render() {
		Engine.INSTANCE.display.clearDisplay();
		
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(titleScreen, 0, 0,
				Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.setColor(color);
		TextureRegion current = titleText.getCurrent();
		Engine.INSTANCE.batch.draw(current,
				(Engine.INSTANCE.camera.getWidth() - current.getRegionWidth()) / 2,
				Engine.INSTANCE.camera.getHeight() - current.getRegionHeight() * 3 / 2,
				current.getRegionWidth(), current.getRegionHeight());
		startBtn.render(Engine.INSTANCE.batch);
		optionsBtn.render(Engine.INSTANCE.batch);
		exitBtn.render(Engine.INSTANCE.batch);
		Engine.INSTANCE.batch.setColor(Color.WHITE);
		Engine.INSTANCE.batch.end();
		
		Engine.INSTANCE.display.updateDisplay();
	}
	
	@Override
	public void exit() {
		Game.INSTANCE.sounds.stopSound(AssetManager.getSound("atmospheric_water"));
		Engine.INSTANCE.input.removeListener(startBtnInput);
		Engine.INSTANCE.input.removeListener(optionsBtnInput);
		Engine.INSTANCE.input.removeListener(exitBtnInput);
	}
}

package gamedev.lwjgl.game.ui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.states.GameState;
import gamedev.lwjgl.game.systems.StateSystem.States;

public class PauseMenu {

	private GameState gs;
	private ModelTexture pausemenuFrame;
	private ModelTexture pointerTexture;
	private Button resumeBtn;
	private Button optionsBtn;
	private Button menuBtn;
	private InputListener generalInput;
	private InputListener resumeBtnInput;
	private InputListener optionsBtnInput;
	private InputListener menuBtnInput;
	private Color pointerColor = new Color(1, 1, 1, 1);
	private float pointerY;
	
	public PauseMenu(GameState gs) {
		this.gs = gs;
		init();
	}
	
	private void init() {
		pausemenuFrame = AssetManager.getTexture("pausemenu_frame");
		pointerTexture = AssetManager.getTexture("pausemenu_pointer");
		
		// Button creation
		Vector4f resumeNormal = new Vector4f(0, 0, 304, 79);
		Vector4f resumePressed = new Vector4f(0, 0, 304, 79);
		resumeBtn = new Button("pausemenu_resume", resumeNormal, resumePressed);
		resumeBtn.setSize(304, 79);
		resumeBtn.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2 + 50,
				Engine.INSTANCE.camera.getHeight() / 2 + 128
				);
		
		Vector4f optionsNormal = new Vector4f(0, 0, 313, 104);
		Vector4f optionsPressed = new Vector4f(0, 0, 313, 104);
		optionsBtn = new Button("pausemenu_options", optionsNormal, optionsPressed);
		optionsBtn.setSize(313, 104);
		optionsBtn.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2 + 50,
				Engine.INSTANCE.camera.getHeight() / 2
				);
		
		Vector4f menuNormal = new Vector4f(0, 0, 379, 74);
		Vector4f menuPressed = new Vector4f(0, 0, 379, 74);
		menuBtn = new Button("pausemenu_mainmenu", menuNormal, menuPressed);
		menuBtn.setSize(379, 74);
		menuBtn.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2 + 50,
				Engine.INSTANCE.camera.getHeight() / 2 - 128
				);
		
		// Input listener creation
		resumeBtnInput = new InputListener() {
			@Override
			public void update() {}
			
			@Override
			public boolean mouseReleased(int button) {
				pointerColor.setColor(1, 1, 1, 1);
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						resumeBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					resumeBtn.release();
					gs.unpause();
					return true;
				}
				return false;
			}
			
			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						resumeBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					resumeBtn.press();
					pointerColor.setColor(0.0625f, 0.65625f, 0.65625f, 1);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean keyRepeat(int key) { return false; }
			
			@Override
			public boolean keyReleased(int key) { return false; }
			
			@Override
			public boolean keyPressed(int key) { return false; }
		};
		
		optionsBtnInput = new InputListener() {
			@Override
			public void update() {}
			
			@Override
			public boolean mouseReleased(int button) {
				pointerColor.setColor(1, 1, 1, 1);
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						optionsBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					optionsBtn.release();
					return true;
				}
				return false;
			}
			
			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						optionsBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					optionsBtn.press();
					pointerColor.setColor(0.0625f, 0.65625f, 0.65625f, 1);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean keyRepeat(int key) { return false; }
			
			@Override
			public boolean keyReleased(int key) { return false; }
			
			@Override
			public boolean keyPressed(int key) { return false; }
		};
		
		menuBtnInput = new InputListener() {
			@Override
			public void update() {}
			
			@Override
			public boolean mouseReleased(int button) {
				pointerColor.setColor(1, 1, 1, 1);
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						menuBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					menuBtn.release();
					Game.INSTANCE.states.enterState(States.MAINMENUSTATE);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean mousePressed(int button) {
				if(button == GLFW_MOUSE_BUTTON_LEFT &&
						menuBtn.isOver(
						Engine.INSTANCE.input.getTranslatedMouseX(),
						Engine.INSTANCE.input.getTranslatedMouseY()
						))
				{
					menuBtn.press();
					pointerColor.setColor(0.0625f, 0.65625f, 0.65625f, 1);
					return true;
				}
				return false;
			}
			
			@Override
			public boolean keyRepeat(int key) { return false; }
			
			@Override
			public boolean keyReleased(int key) { return false; }
			
			@Override
			public boolean keyPressed(int key) { return false; }
		};
		
		generalInput = new InputListener() {
			@Override
			public void update() {
				
			}
			
			@Override
			public boolean mouseReleased(int button) {
				return false;
			}
			
			@Override
			public boolean mousePressed(int button) {
				return false;
			}
			
			@Override
			public boolean keyRepeat(int key) {
				return false;
			}
			
			@Override
			public boolean keyReleased(int key) {
				return false;
			}
			
			@Override
			public boolean keyPressed(int key) {
				if(key == GLFW.GLFW_KEY_ESCAPE) {
					gs.unpause();
					return true;
				}
				
				return false;
			}
		};
	}
	
	public void update() {
		float diff = Engine.INSTANCE.input.getTranslatedMouseY() - Engine.INSTANCE.camera.getHeight() / 2;
		if(diff >= 70) {
			pointerY = Engine.INSTANCE.camera.getHeight() / 2 + 128;
		} else if(diff <= -70) {
			pointerY = Engine.INSTANCE.camera.getHeight() / 2 - 128;
		} else {
			pointerY = Engine.INSTANCE.camera.getHeight() / 2;
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.setColor(pointerColor);
		batch.draw(pointerTexture,
				(Engine.INSTANCE.camera.getWidth() - pausemenuFrame.getWidth() - pointerTexture.getWidth() + 150) / 2,
				pointerY,
				pointerTexture.getWidth(),
				pointerTexture.getHeight()
				);
		
		batch.draw(pointerTexture,
				(Engine.INSTANCE.camera.getWidth() + pausemenuFrame.getWidth() - pointerTexture.getWidth() - 150) / 2,
				pointerY,
				pointerTexture.getWidth(),
				pointerTexture.getHeight()
				);
		
		batch.setColor(Color.WHITE);
		batch.draw(pausemenuFrame,
				(Engine.INSTANCE.camera.getWidth() - pausemenuFrame.getWidth()) / 2,
				(Engine.INSTANCE.camera.getHeight() - pausemenuFrame.getHeight()) / 2,
				pausemenuFrame.getWidth(),
				pausemenuFrame.getHeight()
				);
		
		resumeBtn.render(Engine.INSTANCE.batch);
		optionsBtn.render(Engine.INSTANCE.batch);
		menuBtn.render(Engine.INSTANCE.batch);
	}
	
	public void show() {
		pointerColor.setColor(1, 1, 1, 1);
		Engine.INSTANCE.input.addListener(generalInput);
		Engine.INSTANCE.input.addListener(resumeBtnInput);
		Engine.INSTANCE.input.addListener(optionsBtnInput);
		Engine.INSTANCE.input.addListener(menuBtnInput);
	}
	
	public void hide() {
		Engine.INSTANCE.input.removeListener(generalInput);
		Engine.INSTANCE.input.removeListener(resumeBtnInput);
		Engine.INSTANCE.input.removeListener(optionsBtnInput);
		Engine.INSTANCE.input.removeListener(menuBtnInput);
	}
}

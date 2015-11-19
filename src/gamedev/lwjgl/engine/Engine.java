package gamedev.lwjgl.engine;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.input.InputManager;
import gamedev.lwjgl.engine.render.DisplayManager;
import gamedev.lwjgl.engine.render.SpriteBatch;

public enum Engine {
	INSTANCE;
	
	public final DisplayManager display = new DisplayManager();
	public final InputManager input = new InputManager();
	public final SpriteBatch batch = new SpriteBatch();
	public final SpriteBatch uiBatch = new SpriteBatch();
	public final Camera2d camera = batch.getCamera();
	
	
	public void init() {
		if(!display.createDisplay(1280, 720)) {
			Logger.error("GameLauncher", "Failed to initialize render engine");
			return;
		}
		
		display.setBackgroundColor(0, 0, 0, 1);

		batch.init();
		uiBatch.init();
		input.init(display.getWindow());

		Logger.setDebug(true);
		
		// We are not using depth, might speed up things a little
		glDisable(GL_DEPTH_TEST);
		
		// Enable blending and therefore transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void update() {
		input.update();
		camera.update();
	}
	
	public void cleanup() {
		input.cleanup();
		
		glfwDestroyWindow(display.getWindow());
		glfwTerminate();
	}
}

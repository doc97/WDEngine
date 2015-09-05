package gamedev.lwjgl.engine;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.GL11;

import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.render.DisplayManager;
import gamedev.lwjgl.engine.render.SpriteBatch;

public enum Engine {
	INSTANCE;
	
	public final DisplayManager display = new DisplayManager();
	public final InputManager input = new InputManager();
	public final SpriteBatch batch = new SpriteBatch();
	public final Camera2d camera = new Camera2d();
	
	public void init() {
		if(!display.createDisplay(1280, 720)) {
			Logger.error("GameLauncher", "Failed to initialize render engine");
			return;
		}
		
		display.setBackgroundColor(1, 0, 0, 0);
		batch.init();
		input.init(display.getWindow());
		camera.init(display);
		
		// Enable blending and therefore transparency
		glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void update(float delta) {
		camera.update();
	}
	
	public void cleanup() {
		input.cleanup();
		
		glfwDestroyWindow(display.getWindow());
		glfwTerminate();
	}
}

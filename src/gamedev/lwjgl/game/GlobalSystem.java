package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import gamedev.lwjgl.InputManager;
import gamedev.lwjgl.Logger;
import gamedev.lwjgl.renderEngine.ModelSystem;
import gamedev.lwjgl.renderEngine.RenderEngine;

public class GlobalSystem {
	
	private RenderEngine renderEngine;
	private InputManager inputManager;
	private ModelSystem modelSystem;
	
	public void init() {
		renderEngine = new RenderEngine(this);
		if(!renderEngine.init(1280, 720)) {
			Logger.error("GameLauncher", "Failed to initialize render engine");
			return;
		}
		
		inputManager = new InputManager();
		inputManager.init(renderEngine.getDisplayManager().getWindow());
		
		modelSystem = new ModelSystem();
	}
	
	public void render() {
		renderEngine.render();
	}
	
	public void update(float delta) {
		
	}
	
	public void cleanup() {
		renderEngine.cleanup();
		inputManager.cleanup();
		
		glfwDestroyWindow(renderEngine.getDisplayManager().getWindow());
		glfwTerminate();
	}
	
	public RenderEngine getRenderEngine() {
		return renderEngine;
	}
	
	public InputManager getInputManager() {
		return inputManager;
	}
	
	public ModelSystem getModelSystem() {
		return modelSystem;
	}
}

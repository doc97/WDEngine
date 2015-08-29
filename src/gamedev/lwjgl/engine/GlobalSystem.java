package gamedev.lwjgl.engine;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import gamedev.lwjgl.engine.render.EntitySystem;
import gamedev.lwjgl.engine.render.RenderEngine;

public class GlobalSystem {
	
	private RenderEngine renderEngine;
	private InputManager inputManager;
	private EntitySystem entitySystem;
	private Camera camera;
	
	public void init() {
		renderEngine = new RenderEngine(this);
		if(!renderEngine.init(1280, 720)) {
			Logger.error("GameLauncher", "Failed to initialize render engine");
			return;
		}
		
		inputManager = new InputManager();
		inputManager.init(renderEngine.getDisplayManager().getWindow());
		
		entitySystem = new EntitySystem();
		
		camera = new Camera(renderEngine.getDisplayManager());
	}
	
	public void render() {
		renderEngine.render();
	}
	
	public void update(float delta) {
		camera.update();
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
	
	public EntitySystem getEntitySystem() {
		return entitySystem;
	}
	
	public Camera getCamera() {
		return camera;
	}
}

package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import gamedev.lwjgl.engine.GlobalSystem;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class GameLauncher {
	public static void main(String[] args) {
		GlobalSystem gSys = new GlobalSystem();
		gSys.init();
		
		String[] models = { "cube" };
		String[] textures = { "lwjgl" };
		AssetManager.loadAssets(models, textures);
		
		ModelTexture texture = new ModelTexture(AssetManager.getTexture("lwjgl"));
		gSys.getModelSystem().createModel("Quad", texture);
		
		double lastTime = glfwGetTime();
		float deltaTime = 0;
		while(!gSys.getRenderEngine().getDisplayManager().displayShouldClose()) {
			// Update timers
			double currentTime = glfwGetTime();
			deltaTime = (float) (currentTime - lastTime);
			lastTime = currentTime;
			
			gSys.update(deltaTime);
			gSys.render();
			
			
		}
		
		AssetManager.cleanup();
		gSys.cleanup();
	}
}

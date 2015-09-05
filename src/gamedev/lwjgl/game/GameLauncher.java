package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import org.joml.Vector3f;

import gamedev.lwjgl.engine.Entity;
import gamedev.lwjgl.engine.GlobalSystem;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class GameLauncher {
	public static void main(String[] args) {
		GlobalSystem gSys = new GlobalSystem();
		gSys.init();
		
		String[] models = { };
		String[] textures = { "lwjgl"};
		AssetManager.loadAssets(models, textures);
		
		ModelTexture texture = AssetManager.getTexture("lwjgl");
		TexturedModel model = gSys.getEntitySystem().createModel("Quad", texture);
		Entity entity = new Entity(model, new Vector3f(0, 0, -2), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		gSys.getEntitySystem().addEntity(entity);
		
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

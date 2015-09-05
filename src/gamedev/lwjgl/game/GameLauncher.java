package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.states.State;

public class GameLauncher {
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();
		Engine.INSTANCE.init();
		Game.INSTANCE.init(launcher);
		
		String[] models = { };
		String[] textures = { "lwjgl"};
		String[] fonts = { "basic" };
		AssetManager.loadAssets(models, textures, fonts);
		
		ModelTexture texture = AssetManager.getTexture("lwjgl");
		Entity entity = new Entity(-300, -300);
		entity.addTexture(texture, 0, 0, 200, 200, 0, 0, 0);
		Game.INSTANCE.entities.addEntity(entity);

		double lastTime = glfwGetTime();
		float deltaTime = 0;
		while(!Engine.INSTANCE.display.displayShouldClose()) {
			// Update timers
			double currentTime = glfwGetTime();
			deltaTime = (float) (currentTime - lastTime);
			lastTime = currentTime;
			
			launcher.getCurrentState().render(deltaTime);
		}
		
		AssetManager.cleanup();
		Engine.INSTANCE.cleanup();
	}
	
	private State currentState;
	
	public void setState(State state) {
		if(currentState != null)
			currentState.exit();
		
		currentState = state;
		currentState.enter();
	}
	
	public State getCurrentState() {
		return currentState;
	}
}

package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.states.State;
import gamedev.lwjgl.game.states.StateSystem.States;

public class GameLauncher {
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();

		Engine.INSTANCE.init();
		
		// Load assets
		String[] models = { };
		String[] textures = { "lwjgl", "Player", "willowis2", "map_background", "map_parallax1", "map_parallax2" };
		String[] fonts = { "basic", "font01_2" };
		String[] maps = { "level1" };
		AssetManager.loadAssets(models, textures, fonts, maps);
		
		Game.INSTANCE.init(launcher);
//		Game.INSTANCE.container.getMap().getCollisionMap().addRectangle(rect);
		Game.INSTANCE.states.enterState(States.GAMESTATE);
		
		double lastTime = glfwGetTime();
		float deltaTime = 0;
		while(!Engine.INSTANCE.display.displayShouldClose()) {
			// Update timers
			double currentTime = glfwGetTime();
			deltaTime = (float) (currentTime - lastTime) * 60;
			lastTime = currentTime;
			
			launcher.getCurrentState().loop(deltaTime);
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

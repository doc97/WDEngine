package gamedev.lwjgl.game;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.states.State;
import gamedev.lwjgl.game.systems.StateSystem.States;

public class GameLauncher {
	public static void main(String[] args) {
		GameLauncher launcher = new GameLauncher();

		Engine.INSTANCE.init();
		
		// Load assets
		AssetManager.loadAssets("assets/WDEngine");

		
		Game.INSTANCE.init(launcher);

		Game.INSTANCE.states.enterState(States.MAINMENUSTATE);

		double lastTime = glfwGetTime();
		float updateTime = 0;
		while(!Engine.INSTANCE.display.displayShouldClose()) {
			// Update timers
			double currentTime = glfwGetTime();
			updateTime += (float) (currentTime - lastTime);
			lastTime = currentTime;
			
			while(updateTime >= 1.0f / GameSettings.UPS) {
				updateTime -= 1.0f / GameSettings.UPS;
				launcher.getCurrentState().update();
			}
			
			launcher.getCurrentState().render();
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

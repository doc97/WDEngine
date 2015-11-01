package gamedev.lwjgl.game.input;

import org.lwjgl.glfw.GLFW;

import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.game.Game;

public class DeveloperInput implements InputListener {

	@Override
	public void update() {
		
	}

	@Override
	public boolean keyPressed(int key) {
		if(key == GLFW.GLFW_KEY_F1) {
			Game.INSTANCE.devTools.fillUpEnergy();
			return true;
		}
		if(key == GLFW.GLFW_KEY_F2) {
			Game.INSTANCE.devTools.resetPlayer();
			return true;
		}
		
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
	public boolean mousePressed(int button) {
		return false;
	}

	@Override
	public boolean mouseReleased(int button) {
		return false;
	}
}

package gamedev.lwjgl.game.states;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import gamedev.lwjgl.engine.input.InputListener;

public class CreditsInput implements InputListener {

	private CreditsState cs;
	
	public CreditsInput(CreditsState cs) {
		this.cs = cs;
	}
	
	@Override
	public void update() {

	}

	@Override
	public boolean keyPressed(int key) {
		if(key == GLFW_KEY_ESCAPE)
			cs.setExit(true);
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

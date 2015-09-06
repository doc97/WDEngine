package gamedev.lwjgl.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.input.InputListener;

public class CameraInput implements InputListener {

	private boolean right, left, up, down;
	private int rightKey = GLFW_KEY_D, leftKey = GLFW_KEY_A, upKey = GLFW_KEY_W, downKey = GLFW_KEY_S;
	
	public void update() {
		moveCamera();
	}
	
	@Override
	public boolean keyPressed(int key) {
		if(key == rightKey) right 	= true;
		if(key == leftKey)	left 	= true;
		if(key == upKey)	up 		= true;
		if(key == downKey)	down 	= true;
		return false;
	}

	@Override
	public boolean keyRepeat(int key) {
		return false;
	}

	@Override
	public boolean keyReleased(int key) {
		if(key == rightKey) right 	= false;
		if(key == leftKey)	left 	= false;
		if(key == upKey)	up 		= false;
		if(key == downKey)	down 	= false;
		return false;
	}
	
	private void moveCamera() {
		float dx = 0, dy = 0;
		if(right)	dx += 2;
		if(left)	dx -= 2;
		if(up)		dy += 2;
		if(down)	dy -= 2;
		
		Engine.INSTANCE.camera.addSpeed(dx, dy);
	}
}

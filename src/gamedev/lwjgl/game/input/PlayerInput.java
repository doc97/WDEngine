package gamedev.lwjgl.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.game.entities.Player;

public class PlayerInput implements InputListener {

	private boolean right, left, up, down;
	private int rightKey = GLFW_KEY_D, leftKey = GLFW_KEY_A, upKey = GLFW_KEY_W, downKey = GLFW_KEY_S;
	private Player player;
	
	public PlayerInput(Player player) {
		this.player = player;
	}
	
	public void update() {
		float dx = 0, dy = 0;
		if(right)	dx += 2;
		if(left)	dx -= 2;
		if(up)		dy += 2;
		if(down)	dy -= 2;
		
		player.addSpeed(dx, dy);
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
	
	public void changeKeyBindings(int rightKey, int leftKey, int upKey, int downKey) {
		this.rightKey = rightKey;
		this.leftKey = leftKey;
		this.upKey = upKey;
		this.downKey = downKey;
	}
}

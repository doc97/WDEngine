package gamedev.lwjgl.game.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.utils.Maths;
import gamedev.lwjgl.game.entities.Player;

public class PlayerInput implements InputListener {

	private boolean right, left;
	private int rightKey = GLFW_KEY_D, leftKey = GLFW_KEY_A, jumpKey = GLFW_KEY_SPACE;
	private Player player;
	
	public PlayerInput(Player player) {
		this.player = player;
	}
	
	public void update() {
		float dx = 0, dy = 0;
		if(right)	dx += 2;
		if(left)	dx -= 2;
		
		Vector2f speed = player.getSpeed();
		float maxSpeed = player.getMaxSpeed();
		if(speed.lengthSquared() < maxSpeed * maxSpeed) {
			speed.x += dx;
			speed.y += dy;
			Maths.clampVector(speed, maxSpeed);
		}
	}
	
	@Override
	public boolean keyPressed(int key) {
		if(key == rightKey) right 	= true;
		if(key == leftKey)	left 	= true;
		if(key == jumpKey)
			player.addSpeed(0, 40.0f);
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
		return false;
	}
	
	public void changeKeyBindings(int rightKey, int leftKey, int jumpKey) {
		this.rightKey = rightKey;
		this.leftKey = leftKey;
		this.jumpKey = jumpKey;
	}
}

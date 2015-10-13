package gamedev.lwjgl.game.input;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.states.GameState;

public class GameInput implements InputListener {

	private boolean right, left, jump, dash;
	private int rightKey = GLFW_KEY_D, leftKey = GLFW_KEY_A;
	private int jumpKey = GLFW_KEY_SPACE, dashKey = GLFW_KEY_RIGHT_SHIFT;
	private Player player;
	private GameState gs;
	
	public GameInput(GameState gs) {
		this.player = Game.INSTANCE.container.getPlayer();
		this.gs = gs;
	}
	
	public void update() {
		float dx = 0;
		if(right)	dx += 2;
		if(left)	dx -= 2;
		
		Vector2f speed = player.getSpeed();
		float maxSpeed = player.getMaxSpeed();
		if(!player.isDashing()) {
			if((speed.x + dx) * (speed.x + dx) > maxSpeed * maxSpeed) {
				speed.x = Math.signum(speed.x) * maxSpeed;
			} else {
				speed.x += dx;
			}
		}
		
		if(jump && (player.isOnGround() || player.isInWater()))
			speed.y = 20.0f;
	}
	
	@Override
	public boolean keyPressed(int key) {
		if(key == rightKey)
			right = true;
		else if(key == leftKey)
			left = true;
		else if(key == jumpKey)
			jump = true;
		else if(key == dashKey)
			player.dash();
		else if(key == GLFW.GLFW_KEY_ESCAPE)
			gs.pause();
			
		return true;
	}

	@Override
	public boolean keyRepeat(int key) {
		return false;
	}

	@Override
	public boolean keyReleased(int key) {
		if(key == rightKey) right 	= false;
		if(key == leftKey)	left 	= false;
		if(key == jumpKey) 	jump	= false;
		if(key == dashKey)	dash	= false;
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
	
	public void changeKeyBindings(int rightKey, int leftKey, int jumpKey) {
		this.rightKey = rightKey;
		this.leftKey = leftKey;
		this.jumpKey = jumpKey;
	}
}

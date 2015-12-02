package gamedev.lwjgl.game.input;

import static org.lwjgl.glfw.GLFW.*;

import org.jbox2d.common.Vec2;
import org.lwjgl.glfw.GLFW;

import gamedev.lwjgl.engine.input.InputListener;
import gamedev.lwjgl.engine.sound.Sound;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.states.GameState;

public class GameInput implements InputListener {

	private boolean right, left, jump;
	private int rightKey = GLFW_KEY_D, leftKey = GLFW_KEY_A;
	private int jumpKey = GLFW_KEY_SPACE, dashKey = GLFW_KEY_ENTER;
	private int interactKey = GLFW_KEY_E;
	private Player player;
	private GameState gs;
	private boolean doubJump, jumpedOnCurrent;
	private float v = 80;
	
	public GameInput(GameState gs) {
		this.player = Game.INSTANCE.container.getPlayer();
		this.gs = gs;
	}
	
	public void update() {
		if(right) {
			Game.INSTANCE.physics.applyForceToMiddle(Game.INSTANCE.container.getPlayer(), new Vec2(10f, 0));
		}
		if(left) {
			Game.INSTANCE.physics.applyForceToMiddle(Game.INSTANCE.container.getPlayer(), new Vec2(-10f, 0));
		}
		
		if (doubJump && (player.isInWater() || player.isOnGround()))
			doubJump = false;
		if (jump) {
			if ((player.isOnGround() || player.isInWater()) && !jumpedOnCurrent) {
				Game.INSTANCE.physics.setEntitySpeed(Game.INSTANCE.container.getPlayer(), new Vec2(0, 0), false, true);
				Game.INSTANCE.physics.applyForceToMiddle(Game.INSTANCE.container.getPlayer(), new Vec2(0, 400));
				doubJump = false;
				jumpedOnCurrent = true;
			} else if (!doubJump && !jumpedOnCurrent) {
				Game.INSTANCE.physics.setEntitySpeed(Game.INSTANCE.container.getPlayer(), new Vec2(0, 0), false, true);

				Game.INSTANCE.physics.applyForceToMiddle(Game.INSTANCE.container.getPlayer(), new Vec2(0, 400));
				doubJump = true;
				jumpedOnCurrent = true;
			}
		}
	}
	
	@Override
	public boolean keyPressed(int key) {
		if(key == rightKey) {
			right = true;
			return true;
		}
		if(key == leftKey) {
			left = true;
			return true;
		}
		if(key == jumpKey) {
			jump = true;
			return true;
		}
		if(key == dashKey) {
			player.dash();
			return true;
		}
		if(key == interactKey) {
			Game.INSTANCE.interactions.interact();
			return true;
		}
		if(key == GLFW.GLFW_KEY_ESCAPE) {
			gs.pause();
			return true;
		}
		if(key == GLFW.GLFW_KEY_UP) {
			Game.INSTANCE.sounds.setVolume(AssetManager.getSound("background"), v = Math.min(v += 10, 100));
			return true;
		}
		else if(key == GLFW.GLFW_KEY_DOWN) {
			Game.INSTANCE.sounds.setVolume(AssetManager.getSound("background"), v = Math.max(v -= 10, 0));
			return true;
		}
		if(key == GLFW.GLFW_KEY_RIGHT) {
			Game.INSTANCE.sounds.setBalance(AssetManager.getSound("background"), Sound.RIGHT);
			return true;
		}
		if (key == GLFW.GLFW_KEY_LEFT) {
			Game.INSTANCE.sounds.setBalance(AssetManager.getSound("background"), Sound.LEFT);
			return true;
		}
		if (key == GLFW.GLFW_KEY_ENTER) {
			Game.INSTANCE.sounds.setBalance(AssetManager.getSound("background"), Sound.CENTER);
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
		if(key == rightKey) {
			right = false;
			return true;
		}
		if(key == leftKey) {
			left = false;
			return true;
		}
		if(key == jumpKey) {
			jump = false;
			jumpedOnCurrent = false;
			return true;
		}
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
	
	public void reset() {
		right = false;
		left = false;
		jump = false;
		jumpedOnCurrent = false;
		doubJump = false;
	}
}

package gamedev.lwjgl.engine.utils;

import gamedev.lwjgl.game.GameSettings;

public class Timer {

	private boolean active;
	private int currentTick;
	private int targetTick;
	
	public void update() {
		if(active && currentTick < targetTick)
			currentTick++;
	}
	
	public void set(int target) {
		active = false;
		currentTick = 0;
		targetTick = target;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public float getPercentage() {
		if(targetTick == 0) return 1;
		return Math.min((float) (currentTick) / (float) (targetTick), 1);
	}
	
	public int getTarget() {
		return targetTick;
	}
	
	public float getCurrentTime() {
		return currentTick;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public static int getTicks(float milliseconds) {
		return (int) (GameSettings.UPS * milliseconds / 1000.0f);
	}
}

package gamedev.lwjgl.game.states;

public class Timer {

	private boolean active;
	private float currentTime;
	private float targetTime;
	
	public void update(float dt) {
		if(getPercentage() < 1) {
			if(currentTime < targetTime)
				currentTime += dt;
			else
				currentTime = targetTime;
		}
	}
	
	public void set(float target) {
		active = false;
		currentTime = 0;
		this.targetTime = target;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public float getPercentage() {
		if(targetTime == 0) return 1;
		return Math.min(currentTime / targetTime, 1);
	}
	
	public float getTarget() {
		return targetTime;
	}
	
	public float getCurrentTime() {
		return currentTime;
	}
	
	public boolean isActive() {
		return active;
	}
}

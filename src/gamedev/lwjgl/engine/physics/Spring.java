package gamedev.lwjgl.engine.physics;

public class Spring {

	private float x, y;
	private float height;
	private float targetHeight;
	private float velocity;
	
	public Spring(float x, float y, float targetHeight) {
		this.x = x;
		this.y = y;
		this.targetHeight = targetHeight;
		height = targetHeight;
	}
	
	public void update(float k, float dampening) {
		float x = height - targetHeight;
		float acceleration = -k * x - dampening * velocity;
		
		height += velocity;
		velocity += acceleration;
	}
	
	public void addHeight(float height) {
		this.height += height;
	}
	
	public void addSpeed(float velocity) {
		this.velocity += velocity;
	}
	
	public void setSpeed(float velocity) {
		this.velocity = velocity;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

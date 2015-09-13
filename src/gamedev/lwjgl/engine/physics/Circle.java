package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class Circle {
	private Vector2f position = new Vector2f();
	private Vector2f velocity = new Vector2f();
	private float radius;
	
	public Circle(Vector2f position, float radius) {
		this.position = position;
		this.radius = radius;
	}
	
	public Circle(float x, float y, float radius) {
		position.x = x;
		position.y = y;
		this.radius = radius;
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public void addPosition(float x, float y) {
		position.x += x;
		position.y += y;
	}
	
	public void updatePosition(float delta) {
		Vector2f dvel = new Vector2f();
		velocity.mul(delta, dvel);
		position.add(dvel);
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
}

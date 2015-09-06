package gamedev.lwjgl.engine.cameras;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.render.DisplayManager;
import gamedev.lwjgl.engine.utils.Maths;

public class Camera2d {
	
	private Vector2f position = new Vector2f();
	private Vector2f speed = new Vector2f();
	private float width, height;
	private float friction = 0.9f;
	private float maxSpeed = 10;
	
	public void init(DisplayManager display) {
		width = display.getWindowWidth();
		height = display.getWindowHeight();
	}
	
	public void update() {
		position.add(speed);
		speed.mul(friction);
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public void addPosition(float dx, float dy) {
		position.x += dx;
		position.y += dy;
	}
	
	public void setSpeed(float dx, float dy) {
		speed.set(dx, dy);
		speed = Maths.maxLength(speed, maxSpeed);
	}
	
	public void addSpeed(float dx, float dy) {
		speed.x += dx;
		speed.y += dy;
		speed = Maths.maxLength(speed, maxSpeed);
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getSpeedX() {
		return speed.x;
	}
	
	public float getSpeedY() {
		return speed.y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}

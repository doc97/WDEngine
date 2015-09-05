package gamedev.lwjgl.engine.cameras;

import gamedev.lwjgl.engine.render.DisplayManager;

public class Camera2d {
	
	private float x, y;
	private float width, height;
	
	public void init(DisplayManager display) {
		width = display.getWindowWidth();
		height = display.getWindowHeight();
	}
	
	public void update() {
		
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addPosition(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}

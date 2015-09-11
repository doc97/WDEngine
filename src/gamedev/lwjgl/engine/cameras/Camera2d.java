package gamedev.lwjgl.engine.cameras;

import gamedev.lwjgl.engine.render.DisplayManager;

public class Camera2d {
	
	private float x, y;
	private int width, height;
	
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}

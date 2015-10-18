package gamedev.lwjgl.engine.cameras;

import gamedev.lwjgl.engine.render.DisplayManager;

public class Camera2d {
	
	private float x, y;
	private int width, height;
	private int lowerXLimit, lowerYLimit;
	private int upperXLimit, upperYLimit;
	
	public void init(DisplayManager display) {
		width = display.getWindowWidth();
		height = display.getWindowHeight();
	}
	
	public void update() {
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		
		if(upperXLimit != lowerXLimit && upperYLimit != lowerYLimit) {
			if(this.x > upperXLimit)
				this.x = upperXLimit;
			else if(this.x < lowerXLimit)
				this.x = lowerXLimit;
			
			if(this.y > upperYLimit)
				this.y = upperYLimit;
			else if(this.y < lowerYLimit)
				this.y = lowerYLimit;
		}
	}
	
	public void addPosition(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public void setUpperLimits(int x, int y) {
		upperXLimit = x;
		upperYLimit = y;
	}
	
	public void setLowerLimits(int x, int y) {
		lowerXLimit = x;
		lowerYLimit = y;
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

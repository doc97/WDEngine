package gamedev.lwjgl.game.text;

import gamedev.lwjgl.engine.font.Font;

public class Text {
	private String text;
	private float x, y;
	private int fontSize;
	private Font font;
	
	public Text(String text) {
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public String getText() {
		return text;
	}
	
	public Font getFont() {
		return font;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

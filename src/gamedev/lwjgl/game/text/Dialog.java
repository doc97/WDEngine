package gamedev.lwjgl.game.text;

import java.util.ArrayList;

import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;

public class Dialog {
	private ArrayList<String> texts = new ArrayList<String>();
	private String currentText;
	private String fontName;
	private int fontSize;
	private float x, y;
	private boolean showing;
	
	public void addText(String text) {
		texts.add(text);
	}
	
	public void reset() {
		currentText = null;
	}
	
	public void nextText() {
		if(getCurrentTextIndex() < texts.size() - 1)
			currentText = texts.get(getCurrentTextIndex() + 1);
		else
			currentText = null;
	}
	
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setCurrentText(int index) {
		currentText = texts.get(index);
	}
	
	public int getCurrentTextIndex() {
		return texts.indexOf(currentText);
	}
	
	public Font getFont() {
		return AssetManager.getFont(fontName);
	}
	
	public void show() {
		showing = true;
	}
	
	public void hide() {
		showing = false;
	}
	
	public void render(SpriteBatch batch) {
		if(showing && currentText != null) {
			getFont().setFadeEffect(false);
			getFont().drawString(
					batch,
					currentText,
					fontSize,
					x, y
					);
		}
	}
}

package gamedev.lwjgl.game.text;

import java.util.ArrayList;

import gamedev.lwjgl.engine.data.FontData;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Font.Alignment;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;

public class Dialog {
	private ArrayList<String> texts = new ArrayList<String>();
	private String currentText;
	private FontData fontData;
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
		fontData.name = fontName;
	}
	
	public void setFontSize(int fontSize) {
		fontData.size = fontSize;
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
		return AssetManager.getFont(fontData.name);
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

			// Setting font's alignment
			if(fontData.alignment.equals("LEFT"))
				getFont().setAlignment(Alignment.LEFT);
			else if(fontData.alignment.equals("CENTER"))
				getFont().setAlignment(Alignment.CENTER);
			else if(fontData.alignment.equals("RIGHT"))
				getFont().setAlignment(Alignment.RIGHT);

			getFont().drawString(
					batch,
					currentText,
					fontData.size,
					x, y
					);
		}
	}
}

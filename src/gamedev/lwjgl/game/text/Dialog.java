package gamedev.lwjgl.game.text;

import java.util.ArrayList;

import gamedev.lwjgl.engine.render.SpriteBatch;

public class Dialog {
	private ArrayList<Text> texts = new ArrayList<Text>();
	private Text currentText;
	private boolean showing;
	
	public void addText(Text text) {
		texts.add(text);
	}
	
	public void nextText() {
		if(getCurrentTextIndex() < texts.size() - 1)
			currentText = texts.get(getCurrentTextIndex() + 1); 
	}
	
	public void setCurrentText(int index) {
		currentText = texts.get(index);
	}
	
	public int getCurrentTextIndex() {
		return texts.indexOf(currentText);
	}
	
	public void show() {
		showing = true;
	}
	
	public void hide() {
		showing = false;
	}
	
	public void render(SpriteBatch batch) {
		if(showing && currentText != null) {
			currentText.getFont().drawString(
					batch,
					currentText.getText(),
					currentText.getFontSize(),
					currentText.getX(),
					currentText.getY()
					);
		}
	}
}

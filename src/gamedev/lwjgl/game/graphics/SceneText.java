package gamedev.lwjgl.game.graphics;

import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.render.SpriteBatch;

public class SceneText implements SceneObject {

	private Font font;
	private String text;
	private int fontSize;
	private float x, y;
	private boolean fade;
	
	@Override
	public void update() {
		font.update();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		font.setFadeEffect(fade);
		font.drawString(batch, text, fontSize, x, y);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setFade(boolean fade) {
		this.fade = fade;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}

package gamedev.lwjgl.engine.font;

import java.util.HashMap;
import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;

import gamedev.lwjgl.engine.textures.Color;

import gamedev.lwjgl.engine.render.SpriteBatch;

import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.game.states.Timer;

public class Font {
	public enum Alignment {
		LEFT, RIGHT, CENTER;
	}
	
	private Map<Integer, Glyph> glyphs = new HashMap<Integer, Glyph>();
	private String name;
	private int originalFontSize;
	private Alignment alignment = Alignment.CENTER;
	private Timer fadeTimer = new Timer();
	private boolean fadeEffect;
	
	public Font(String name, int fontSize, ModelTexture texture, Map<Integer, Glyph> glyphs) {
		this.name = name;
		this.glyphs = glyphs;
		originalFontSize = fontSize;
	}
	
	public void update(float dt) {
		if(fadeTimer.isActive()) {
			fadeTimer.update(dt);
			if(fadeTimer.getPercentage() == 1)
				fadeTimer.setActive(false);
		}
	}
	
	public void drawString(SpriteBatch batch, String text, int fontSize, float x, float y) {

		float currentOffset = 0;
		float textWidth = 0;
		float scale = (float) fontSize / (float) originalFontSize;
		Glyph[] characters = new Glyph[text.length()];
		
		// Calculate text width and place glyphs in array
		for(int i = 0; i < text.length(); i++) {
			int code = (int) text.charAt(i);
			Glyph glyph = glyphs.get(code);
			if(glyph == null) {
				Logger.message("Font", "No such character loaded as: " + code);
				continue;
			}
			
			textWidth += glyph.getAdvanceX();
			characters[i] = glyph;
		}

		// Draw characters
		for(int i = 0; i < characters.length; i++) {
			Glyph glyph = characters[i];
			float width = glyph.getWidth() * scale;
			float height = glyph.getHeight() * scale;
			
			float drawX = 0;
			float drawY = 0;
			switch(alignment) {
			case LEFT :
				drawX = x + currentOffset + glyph.getOffsetX();
				drawY = y;
				break;
			case CENTER :
				drawX = x + currentOffset + glyph.getOffsetX() - scale * textWidth / 2;
				drawY = y;
				break;
			case RIGHT :
				drawX = x + currentOffset + glyph.getOffsetX() - scale * textWidth;
				drawY = y;
				break;
			}
			
			if(fadeEffect) {
				if(fadeTimer.isActive()) {
					Color color = Engine.INSTANCE.batch.getColor();
					color.a = fadeTimer.getCurrentTime() / ((i + 1) * fadeTimer.getTarget() / characters.length);
				} else if(fadeTimer.getPercentage() < 1){
					Engine.INSTANCE.batch.setColor(Color.TRANSPARENT);
				}
			}
			
			batch.draw(glyph.getTexture(), drawX, drawY,

					width, height, glyph.getTexture().getUVs(),
					0, 0, 0);
			currentOffset += glyph.getAdvanceX() * scale;
		}
	}
	
	public void setFadeEffect(boolean fade) {
		fadeEffect = fade;
	}
	
	public void setFadeTimer(float timer) {
		fadeTimer.set(timer);
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public Timer getFadeTimer() {
		return fadeTimer;
	}
	
	public int getOriginalSize() {
		return originalFontSize;
	}
	
	public String getName() {
		return name;
	}
}

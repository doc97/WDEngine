package gamedev.lwjgl.engine.font;

import java.util.HashMap;
import java.util.Map;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Font {
	public enum Alignment {
		LEFT, RIGHT, CENTER;
	}
	
	private Map<Integer, Glyph> glyphs = new HashMap<Integer, Glyph>();
	private String name;
	private int originalFontSize;
	private Alignment alignment = Alignment.CENTER;
	
	public Font(String name, int fontSize, ModelTexture texture, Map<Integer, Glyph> glyphs) {
		this.name = name;
		this.glyphs = glyphs;
		originalFontSize = fontSize;
	}
	
	public void drawString(String text, int fontSize, float x, float y) {
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
		for(Glyph glyph : characters) {
			float width = glyph.getWidth() * scale;
			float height = glyph.getHeight() * scale;
			
			float drawX = 0;
			float drawY = 0;
			switch(alignment) {
			case LEFT :
				drawX = x + currentOffset + glyph.getOffsetX();
				drawY = y;// - glyph.getOffsetY();
				break;
			case CENTER :
				drawX = x + currentOffset + glyph.getOffsetX() - scale * textWidth / 2;
				drawY = y - glyph.getOffsetY();
				break;
			case RIGHT :
				drawX = x + currentOffset + glyph.getOffsetX() - scale * textWidth;
				drawY = y - glyph.getOffsetY();
				break;
			}
			
			Engine.INSTANCE.batch.draw(glyph.getTexture(), drawX, drawY,
					width, height, glyph.getTexture().getUVs(),
					0, 0, 0);
			currentOffset += glyph.getAdvanceX() * scale;
		}
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public int getOriginalSize() {
		return originalFontSize;
	}
	
	public String getName() {
		return name;
	}
}

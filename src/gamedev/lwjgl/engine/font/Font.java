package gamedev.lwjgl.engine.font;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Font {
	public enum Alignment {
		LEFT, RIGHT, CENTER;
	}
	
	private ModelTexture[] characters;
	private String name;
	private Alignment alignment = Alignment.CENTER;
	
	public Font(String name, ModelTexture[] characters) {
		this.name = name;
		this.characters = characters;
	}
	
	public ModelTexture getCharacterTexture(char character) {
		int index = (int) (character - 'a');
		if(index < characters.length)
			return characters[index];
		else
			return null;
	}
	
	public void drawString(String text, float fontSize, float x, float y) {
		float wordLength = 0;
		float width = 0;
		float height = fontSize;
		
		for(int i = 0; i < text.length(); i++) {
			ModelTexture charTex = getCharacterTexture(text.charAt(i));
			if(charTex == null) {
				Logger.error("Font", "No such character in font: " + name);
				return;
			}
			
			width = (height / charTex.getHeight()) * charTex.getWidth();
			wordLength += width;
		}
		
		float currentPosition = 0;
		for(int i = 0; i < text.length(); i++) {
			ModelTexture charTex = getCharacterTexture(text.charAt(i));
			width = (height / charTex.getHeight()) * charTex.getWidth();
			
			float charX = 0;
			if(alignment == Alignment.CENTER) {
				charX = -wordLength / 2 + currentPosition;
			} else if(alignment == Alignment.LEFT) {
				charX = currentPosition;
			} else if(alignment == Alignment.RIGHT) {
				charX = -wordLength + currentPosition;
			}
			Engine.INSTANCE.batch.draw(charTex, charX, 0, width, height, 0, 0, 0);
			currentPosition += width;
		}
		
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public String getName() {
		return name;
	}
}

package gamedev.lwjgl.engine.font;

import gamedev.lwjgl.engine.textures.TextureRegion;

public class Glyph {
	private int asciiCode;
	private int width, height;
	private int xoffset, yoffset;
	private int xadvance;
	private TextureRegion region;
	
	public Glyph(TextureRegion region, int asciiCode, int width, int height,
			int xoffset, int yoffset, int xadvance) {
		this.asciiCode = asciiCode;
		this.region = region;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
	}
	
	public int getASCIICode() {
		return asciiCode;
	}
	
	public char getCharacter() {
		return (char) asciiCode;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getOffsetX() {
		return xoffset;
	}
	
	public int getOffsetY() {
		return yoffset;
	}
	
	public int getAdvanceX() {
		return xadvance;
	}
	
	public TextureRegion getTexture() {
		return region;
	}
}

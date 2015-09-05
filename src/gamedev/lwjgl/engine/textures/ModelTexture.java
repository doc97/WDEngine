package gamedev.lwjgl.engine.textures;

public class ModelTexture {
	private int textureID;
	private float width;
	private float height;
	
	public ModelTexture(int textureID, float width, float height) {
		this.textureID = textureID;
		this.width = width;
		this.height = height;
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}

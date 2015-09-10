package gamedev.lwjgl.engine.textures;

public class ModelTexture {
	private int textureID;
	private float width;
	private float height;
	protected float[] uvs;
	
	public ModelTexture(int textureID, float width, float height) {
		this.textureID = textureID;
		this.width = width;
		this.height = height;
		uvs = new float[]{0,0,1,1};
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
	
	public float[] getUVs(){
		return uvs;
	}
	
	public ModelTexture getTexture(){
		return this;
	}
	
}

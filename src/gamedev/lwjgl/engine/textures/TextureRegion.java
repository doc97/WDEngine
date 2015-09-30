package gamedev.lwjgl.engine.textures;

public class TextureRegion extends ModelTexture{
	
	private ModelTexture texture;
	private float regionWidth;
	private float regionHeight;
	
	public TextureRegion(ModelTexture texture, float x, float y, float regionWidth, float regionHeight){
		super(texture.getTextureID(), texture.getWidth(), texture.getHeight());
		this.uvs = new float[]{
				x / texture.getWidth(), y / texture.getHeight(), (x + regionWidth) / texture.getWidth(), (y + regionHeight) / texture.getHeight()
		};
		this.texture = texture;
		this.regionWidth = regionWidth;
		this.regionHeight = regionHeight;
	}
	
	public ModelTexture getTexture(){
		return texture;
	}
	
	public float getRegionWidth() {
		return regionWidth;
	}
	
	public float getRegionHeight() {
		return regionHeight;
	}
}

package gamedev.lwjgl.engine.textures;

public class TextureRegion extends ModelTexture{
	
	private ModelTexture texture;
	
	public TextureRegion(ModelTexture texture, float x, float y, float regionWidth, float regionHeight){
		super(texture.getTextureID(), texture.getWidth(), texture.getHeight());
		this.uvs = new float[]{
				x / texture.getWidth(), y / texture.getHeight(), (x + regionWidth) / texture.getWidth(), (y + regionHeight) / texture.getHeight()
		};
		this.texture = texture;
	}
	
	public ModelTexture getTexture(){
		return texture;
	}
	
}

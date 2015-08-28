package gamedev.lwjgl.engine.models;

import gamedev.lwjgl.engine.textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public void setTexCoords(float[] texcoords) {
		rawModel.storeDataInAttributeList(2, rawModel.getTBO(), 2, texcoords);
	}

	public RawModel getRawModel() {
		return rawModel;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}

}

package gamedev.lwjgl.engine.render;

import java.util.ArrayList;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class ModelSystem {

	private ArrayList<TexturedModel> models;
	
	public ModelSystem() {
		models = new ArrayList<TexturedModel>();
	}
	
	public void createModel(String objFile, ModelTexture texture) {
		if(objFile == null || objFile == "") {
			Logger.error("MeshSystem", "No object file given");
			return;
		}
		
		RawModel rawModel = AssetManager.getModel(objFile);
		TexturedModel model = new TexturedModel(rawModel, texture);
		models.add(model);
	}
	
	public void removeModel(TexturedModel model) {
		models.remove(model);
	}
	
	public ArrayList<TexturedModel> getModels() {
		return models;
	}
}

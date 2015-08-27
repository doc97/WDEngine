package gamedev.lwjgl.renderEngine;

import java.util.ArrayList;

import gamedev.lwjgl.Logger;
import gamedev.lwjgl.models.RawModel;
import gamedev.lwjgl.models.TexturedModel;
import gamedev.lwjgl.textures.ModelTexture;
import gamedev.lwjgl.utils.AssetManager;

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

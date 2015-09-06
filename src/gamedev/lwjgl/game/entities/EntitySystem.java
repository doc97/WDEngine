package gamedev.lwjgl.game.entities;

import java.util.ArrayList;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class EntitySystem {

	private ArrayList<Entity> entities;
	
	public EntitySystem() {
		entities = new ArrayList<Entity>();
	}
	
	public TexturedModel createModel(String objFile, ModelTexture texture) {
		if(objFile == null || objFile == "") {
			Logger.error("MeshSystem", "No object file given");
			return null;
		}
		
		RawModel rawModel = AssetManager.getModel(objFile);
		TexturedModel model = new TexturedModel(rawModel, texture);
		return model;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public void clear() {
		entities.clear();
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
}

package gamedev.lwjgl.game.entities;


import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public enum ItemType {
	
	COIN(AssetManager.getTexture("coin")),
	ENERGY(AssetManager.getTexture("burg"));
	
	private final ModelTexture texture;
	
	private ItemType(ModelTexture tex){
		this.texture = tex;
		
	}
	
	public ModelTexture getTexture(){
		return texture;
	}
	
}

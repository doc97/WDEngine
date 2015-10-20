package gamedev.lwjgl.game.entities;


import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public enum ItemType {
	
	COIN(AssetManager.getTexture("coin"), Color.WHITE),
	ENERGY(AssetManager.getTexture("energy_gem"), new Color(0.125f, 0.96f, 0.73f, 1));

	private final ModelTexture texture;
	private final Color tintColor;
	
	private ItemType(ModelTexture tex, Color tintColor) {
		this.texture = tex;
		this.tintColor = tintColor;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
	
	public Color getTintColor() {
		return tintColor;
	}
}

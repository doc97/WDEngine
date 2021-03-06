package gamedev.lwjgl.game.entities;


import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public enum ItemType {
	
	COIN(AssetManager.getTexture("coin"), Color.WHITE),
	ENERGY(AssetManager.getTexture("energy_orb"), new Color(10f / 255f, 240f / 255f, 200f / 255f, 1)),
	ORB(AssetManager.getTexture("energy_orb"), new Color(220f / 255f, 180f / 255f, 20f / 255f, 1)),
	BOX(AssetManager.getTexture("white_pixel"), Color.WHITE);

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

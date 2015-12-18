package gamedev.lwjgl.game.ui;

import java.util.HashMap;
import java.util.Map.Entry;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.ItemType;

public class Inventory implements Cleanable {
	
	private HashMap<ItemType, Integer> items;
	private Font font;
	
	public Inventory() {
		items = new HashMap<ItemType, Integer>();
		font = AssetManager.getFont("basic");
	}
	
	public void addItem(ItemType type) {
		if (items.containsKey(type)) {
			items.put(type, items.get(type) + 1);
		} else {
			items.put(type, 1);
		}
	}
	
	public void removeItem(ItemType type) {
		if(items.containsKey(type))
			items.remove(type);
	}
	
	public void update(float delta){
		
	}
	
	public void render(SpriteBatch batch) {
		int itemsAmount = items.entrySet().size();
		Camera2d c = batch.getCamera();
		for (Entry<ItemType, Integer> e : items.entrySet()){
			ModelTexture tex = e.getKey().getTexture();
			int i = itemsAmount--;
			batch.draw(tex, c.getWidth() / 2 - 50 - (i * 100), -c.getHeight() / 2 + 50, 100, 100, tex.getUVs(), 0, 0, 0);
			font.drawString(batch, Integer.toString(e.getValue()), (int)(font.getOriginalSize() * 0.7), c.getWidth() / 2 - 50 - (i * 100), -c.getHeight() / 2 + 50);
		}
	}
	
	public boolean contains(ItemType type) {
		return items.containsKey(type);
	}
	
	public int amount(ItemType type) {
		if(!contains(type))
			return 0;
		return items.get(type);
	}
	
	public void cleanup() {
		items.clear();
	}
}
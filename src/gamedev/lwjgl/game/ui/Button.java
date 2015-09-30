package gamedev.lwjgl.game.ui;

import org.joml.Vector4f;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.TextureRegion;
import gamedev.lwjgl.engine.utils.AssetManager;

public class Button {

	private TextureRegion normalTexture;
	private TextureRegion pressedTexture;
	private boolean pressed;
	private boolean hasPressed;
	private float x;
	private float y;
	private float width;
	private float height;
	
	public Button(String file, Vector4f normalData, Vector4f pressedData) {
		normalTexture = new TextureRegion(AssetManager.getTexture(file),
				normalData.x, normalData.y, normalData.z, normalData.w);
		
		if(pressedData != null) {
			pressedTexture = new TextureRegion(AssetManager.getTexture(file),
					pressedData.x, pressedData.y, pressedData.z, pressedData.w);
			hasPressed = true;
		}
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public boolean isOver(float x, float y) {
		return (
				x >= this.x - width / 2 &&
				x <= this.x + width / 2 &&
				y >= this.y - height / 2 &&
				y <= this.y + height / 2
				);
	}
	
	public void render(SpriteBatch batch) {
		if(hasPressed && pressed) {
			batch.draw(pressedTexture,
					x - pressedTexture.getRegionWidth() / 2,
					y - pressedTexture.getRegionHeight() / 2,
					pressedTexture.getRegionWidth(),
					pressedTexture.getRegionHeight()
					);
		} else if(!pressed) {
			batch.draw(normalTexture,
					x - normalTexture.getRegionWidth() / 2,
					y - normalTexture.getRegionHeight() / 2,
					normalTexture.getRegionWidth(),
					normalTexture.getRegionHeight()
					);
		}
	}
	
	public void press() {
		pressed = true;
	}
	
	public void release() {
		pressed = false;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}

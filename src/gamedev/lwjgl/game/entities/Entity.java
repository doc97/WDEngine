package gamedev.lwjgl.game.entities;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Entity {
	private List<ModelTexture> textures = new ArrayList<ModelTexture>();
	private List<Float> positions = new ArrayList<Float>();
	private List<Float> dimensions = new ArrayList<Float>();
	private List<Float> anchors = new ArrayList<Float>();
	private List<Float> rotations = new ArrayList<Float>();
	private float x, y;
	private float anchorX, anchorY;
	private float rotation;
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addTexture(ModelTexture texture, float x, float y, float width, float height, float anchorX, float anchorY, float rotation) {
		textures.add(texture);
		positions.add(x);
		positions.add(y);
		dimensions.add(width);
		dimensions.add(height);
		anchors.add(anchorX);
		anchors.add(anchorY);
		rotations.add(rotation);
	}
	
	public void render(SpriteBatch batch) {
		for(int i = 0; i < textures.size(); i++) {
			batch.draw(textures.get(i), x + positions.get(i),  y + positions.get(i + 1),
					dimensions.get(i), dimensions.get(i + 1),
					rotation + rotations.get(i), anchorX + anchors.get(i), anchorY + anchors.get(i + 1));
		}
	}
	
	public void addEntityPosition(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public void addEntityRotation(float rotation) {
		this.rotation += rotation;
	}
	
	public void setEntityPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setEntityAnchorPoint(float x, float y) {
		anchorX = x;
		anchorY = y;
	}
	
	public void setEntityRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public void scale(float x, float y) {
		for(int i = 0; i < textures.size(); i++)
			setTextureSize(i, dimensions.get(i) * x, dimensions.get(i) * y);
	}

	public void addTexturePosition(int textureIndex, float dx, float dy) {
		positions.set(textureIndex, positions.get(textureIndex) + dx);
		positions.set(textureIndex + 1, positions.get(textureIndex + 1) + dy);
	}
	
	public void addTextureRotation(int textureIndex, float rotation) {
		rotations.set(textureIndex, rotations.get(textureIndex) + rotation);
	}
	
	public void setTexturePosition(int textureIndex, float x, float y) {
		positions.set(textureIndex, x);
		positions.set(textureIndex + 1, y);
	}
	
	public void setTextureAnchorPoint(int textureIndex, float x, float y) {
		anchors.set(textureIndex, x);
		anchors.set(textureIndex + 1, y);
	}
	
	public void setTextureSize(int textureIndex, float width, float height) {
		dimensions.set(textureIndex, width);
		dimensions.set(textureIndex + 1, height);
	}
	
	public void setTextureRotation(int textureIndex, float rotation) {
		rotations.set(textureIndex, rotation);
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}

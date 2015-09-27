package gamedev.lwjgl.game.entities;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Entity {
	private List<ModelTexture> textures = new ArrayList<ModelTexture>();
	private List<Float> positions = new ArrayList<Float>();
	private List<Float> dimensions = new ArrayList<Float>();
	private List<Float> anchors = new ArrayList<Float>();
	private List<Float> rotations = new ArrayList<Float>();
	protected Vector2f speed = new Vector2f();
	protected float maxSpeed = 8;
	protected float x, y;
	private float anchorX, anchorY;
	private float rotation;
	protected boolean dynamic;
	private Vector2f waterLift = new Vector2f();
	
	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public int addTexture(ModelTexture texture, float x, float y, float width, float height, float anchorX, float anchorY, float rotation) {
		textures.add(texture);
		positions.add(x);
		positions.add(y);
		dimensions.add(width);
		dimensions.add(height);
		anchors.add(anchorX);
		anchors.add(anchorY);
		rotations.add(rotation);
		return textures.size() - 1;
	}
	
	public void setTexture(int index, ModelTexture texture) {
		textures.set(index, texture);
	}
	
	public void update(float dt) {
	}
	
	public void render(SpriteBatch batch) {
		for(int i = 0; i < textures.size(); i++) {
			batch.draw(textures.get(i), x + positions.get(2 * i),  y + positions.get(2 * i + 1),
					dimensions.get(2 * i), dimensions.get(2 * i + 1), textures.get(i).getUVs(),
					rotation + rotations.get(i), anchorX + anchors.get(2 * i), anchorY + anchors.get(2 * i + 1));
		}
	}
	
	public void addEntityPosition(float dx, float dy) {
		x += dx;
		y += dy;
	}
	
	public void addEntityRotation(float rotation) {
		this.rotation += rotation;
	}
	
	public void addSpeed(float dx, float dy) {
		speed.x += dx;
		speed.y += dy;
	}

	public void setEntityPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setEntityAnchorPoint(float x, float y) {
		anchorX = x;
		anchorY = y;
	}
	
	public void setSpeed(float dx, float dy) {
		speed.x = dx;
		speed.y = dy;
		if(Math.abs(speed.x) < 0.01f) speed.x = 0;
		if(Math.abs(speed.y) < 0.01f) speed.y = 0;
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
	
	public void setWaterLift(float lift) {
		waterLift.y = lift;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}
	
	public Vector2f getSpeed() {
		return speed;
	}
	
	public boolean isDynamic() {
		return dynamic;
	}
	
	public Vector2f getWaterLift() {
		return waterLift;
	}
}

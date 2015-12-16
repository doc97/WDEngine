package gamedev.lwjgl.game.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.sound.Sound;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;

public class Entity {
	private List<ModelTexture> textures = new ArrayList<ModelTexture>();
	private List<Float> positions = new ArrayList<Float>();
	private List<Float> dimensions = new ArrayList<Float>();
	private List<Float> anchors = new ArrayList<Float>();
	private List<Float> rotations = new ArrayList<Float>();
	protected float x, y;
	private float anchorX, anchorY;
	private float rotation;
	protected boolean dynamic;
	private boolean isInWater;
	private boolean isOnGround;
	protected BodyDef bodyDef;
	protected Map<String, FixtureDef> fixtureDefs = new HashMap<String, FixtureDef>();
	
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
	
	public void changeTexture(int index, ModelTexture texture, float x, float y,
			float width, float height, float anchorX, float anchorY, float rotation) {
		textures.set(index, texture);
		positions.set(2 * index, x);
		positions.set(2 * index + 1, y);
		dimensions.set(2 * index, width);
		dimensions.set(2 * index + 1, height);
		anchors.set(2 * index, anchorX);
		anchors.set(2 * index + 1, anchorY);
		rotations.set(index, rotation);
	}
	
	public void setTexture(int index, ModelTexture texture) {
		textures.set(index, texture);
	}
	
	public void update() {
		x = Game.INSTANCE.physics.currentEntityPosition(this).x;
		y = Game.INSTANCE.physics.currentEntityPosition(this).y;
	}
	
	public void render(SpriteBatch batch) {
		for(int i = 0; i < textures.size(); i++) {
			batch.draw(textures.get(i), x + positions.get(2 * i),
					y + positions.get(2 * i + 1),
					dimensions.get(2 * i), dimensions.get(2 * i + 1), textures.get(i).getUVs(),
					rotation + rotations.get(i), anchorX + anchors.get(2 * i), anchorY + anchors.get(2 * i + 1));
		}
	}
	
	public void addEntityRotation(float rotation) {
		this.rotation += rotation;
	}

	public void setEntityPosition(float x, float y) {
		Game.INSTANCE.physics.setEntityPosition(this, new Vec2(x, y));
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
	
	public void setInWater(boolean b){
		if (b && !isInWater && x > Game.INSTANCE.container.getPlayer().x - Engine.INSTANCE.camera.getWidth() / 2
				&& x < Game.INSTANCE.container.getPlayer().x + Engine.INSTANCE.camera.getWidth() / 2) {
			Sound s = AssetManager.getSound("splash");
			if (x > Game.INSTANCE.container.getPlayer().x)
				Game.INSTANCE.sounds.setBalance(s, Sound.RIGHT);
			else if (x < Game.INSTANCE.container.getPlayer().x)
				Game.INSTANCE.sounds.setBalance(s, Sound.LEFT);
			Game.INSTANCE.sounds.playSound(s);
		}
		isInWater = b;
	}
	
	public void setOnGround(boolean b) {
		isOnGround = b;
	}

	public void addFixtureDef(FixtureDef fixDef) {
		fixtureDefs.put((String) fixDef.userData, fixDef);
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
		
	public boolean isDynamic() {
		return dynamic;
	}
	
	public ModelTexture getTexture(int i) {
		return textures.get(i);
	}	

	public boolean isInWater() {
		return isInWater;
	}
	
	public boolean isOnGround() {
		return isOnGround;
	}
	
	public BodyDef getBodyDef() {
		return bodyDef;
	}
	
	public FixtureDef getFixtureDef(String key) {
		return fixtureDefs.get(key);
	}
	
	public Map<String, FixtureDef> getFixtureDefs() {
		return fixtureDefs;
	}
}

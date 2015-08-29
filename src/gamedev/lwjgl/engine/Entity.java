package gamedev.lwjgl.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.utils.Maths;

public class Entity {
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public void addPosition(Vector3f pos) {
		position.add(pos);
	}
	
	public void addRotation(Vector3f rot) {
		rotation.add(rot);
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public void scale(Vector3f factor) {
		scale.mul(factor);
	}
	
	public Matrix4f getTransformationMatrix() {
		return Maths.createTransformationMatrix(position, rotation, scale);
	}
}

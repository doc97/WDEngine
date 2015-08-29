package gamedev.lwjgl.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import gamedev.lwjgl.engine.render.DisplayManager;
import gamedev.lwjgl.engine.utils.Maths;

public class Camera {
	
	/*
	 * Coordinate System
	 * X => Right
	 * Y => Up
	 * Z => Forward
	 */
	
	private Matrix4f projection;
	private Matrix4f view;
	private Vector3f position = new Vector3f(0);
	private double verticalAngle;
	private double horizontalAngle;
	private float width, height;
	
	public Camera(DisplayManager display) {
		width = display.getWindowWidth();
		height = display.getWindowHeight();
		projection = Maths.createProjectionMatrix((float) Math.toRadians(80), width / height, 0.1f, 1000.0f);
		view = new Matrix4f();
	}
	
	public void update() {
		view = Maths.createViewMatrix(position, (float) verticalAngle, (float) horizontalAngle);
	}
	
	public void setPosition(Vector3f pos) {
		position = pos;
	}
	
	public void move(Vector3f pos) {
		position.add(pos, position);
	}
	
	public void setRotation(float vertical, float horizontal) {
		verticalAngle = vertical;
		horizontalAngle = horizontal;
	}
	
	public void rotate(float vertical, float horizontal) {
		verticalAngle += vertical;
		horizontalAngle += horizontal;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projection;
	}
	
	public Matrix4f getViewMatrix() {
		return view;
	}
	
	public Matrix4f getViewProjectionMatrix() {
		Matrix4f mat = new Matrix4f();
		projection.mul(view, mat);
		return mat;
	}
}

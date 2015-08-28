package gamedev.lwjgl.engine;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

import java.nio.DoubleBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import gamedev.lwjgl.engine.render.DisplayManager;
import gamedev.lwjgl.engine.utils.Maths;

public class Camera {
	private DisplayManager display;
	private Matrix4f projection;
	private Matrix4f view;
	private Vector3f position = new Vector3f(0);
	private Vector3f direction = new Vector3f(0);
	private Vector3f up = new Vector3f(0, 1, 0);
	private double verticalAngle;
	private double horizontalAngle;
	private double oldx, oldy;
	private float sensitivity;
	
	public Camera(DisplayManager display) {
		this.display = display;
		projection = Maths.createProjectionMatrix((float) (Math.toRadians(45)),
				(float) (display.getWindowWidth()) / (float) (display.getWindowHeight()),
				0.1f, 1000f);
		view = new Matrix4f();
	}
	
	public void update() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		
		glfwGetCursorPos(display.getWindow(), x, y);
		x.rewind();
		y.rewind();
		
		double xpos = x.get();
		double ypos = y.get();
		
		horizontalAngle += sensitivity * (oldx - xpos);
		verticalAngle += sensitivity * (oldy - ypos);
		
		direction = new Vector3f(
				(float) (Math.cos(verticalAngle) * Math.sin(horizontalAngle)),
				(float) (Math.sin(verticalAngle)),
				(float) (Math.cos(verticalAngle) * Math.cos(horizontalAngle))
				);
		
		Vector3f center = new Vector3f();
		position.add(direction, center);
		view.lookAt(position, center, up, view);
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

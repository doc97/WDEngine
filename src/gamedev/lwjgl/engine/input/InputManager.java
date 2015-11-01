package gamedev.lwjgl.engine.input;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import gamedev.lwjgl.engine.Engine;

public class InputManager {
	private static GLFWErrorCallback errCallback;
	private static GLFWKeyCallback keyCallback;
	private static GLFWMouseButtonCallback mouseCallback;
	
	private List<InputListener> listeners = new ArrayList<InputListener>();
	private List<InputListener> addListeners = new ArrayList<InputListener>();
	private List<InputListener> remListeners = new ArrayList<InputListener>();
	private ByteBuffer xpos = BufferUtils.createByteBuffer(8);
	private ByteBuffer ypos = BufferUtils.createByteBuffer(8);
	private float mouseX, mouseY;
	private long window;
	private boolean invoking;
	
	public void init(long window) {
		this.window = window;
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glfwSetErrorCallback(errCallback = errorCallbackPrint(System.err));
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				invoking = true;
				for(InputListener il : listeners) {
					if(action == GLFW_REPEAT) {
						if(il.keyRepeat(key))
							break;
					} else if(action == GLFW_RELEASE) {
						if(il.keyReleased(key))
							break;
					} else if(action == GLFW_PRESS) {
						if(il.keyPressed(key))
							break;
					}
				}
				invoking = false;
			}
		});
		glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				invoking = true;
				for(InputListener il : listeners) {
					if(action == GLFW_PRESS) {
						if(il.mousePressed(button))
							break;
					} else if(action == GLFW_RELEASE) {
						if(il.mouseReleased(button))
							break;
					}
				}
				invoking = false;
			}
		});
	}
	
	public void update() {
		for(InputListener listener : listeners)
			listener.update();
		
		if(!invoking) {
			for(InputListener add : addListeners)
				listeners.add(add);
			
			for(InputListener rem : remListeners)
				listeners.remove(rem);
			
			addListeners.clear();
			remListeners.clear();
		}
		
		glfwGetCursorPos(window, xpos, ypos);
		mouseX = (float) xpos.getDouble();
		mouseY = (float) ypos.getDouble();
		xpos.clear();
		ypos.clear();
	}

	public void addListener(InputListener listener) {
		addListeners.add(listener);
	}
	
	public void removeListener(InputListener listener) {
		remListeners.add(listener);
	}
	
	public void removeAllListeners() {
		listeners.clear();
	}
	
	public float getRawMouseX() {
		return mouseX;
	}
	
	public float getRawMouseY() {
		return mouseY;
	}
	
	public float getTranslatedMouseX() {
		return (mouseX / Engine.INSTANCE.display.getWindowWidth()) * Engine.INSTANCE.camera.getWidth();
	}
	
	public float getTranslatedMouseY() {
		return ((Engine.INSTANCE.display.getWindowHeight() - mouseY) / Engine.INSTANCE.display.getWindowHeight()) * Engine.INSTANCE.camera.getHeight();
	}

	public void cleanup() {
		keyCallback.release();
		errCallback.release();
		mouseCallback.release();
	}
}

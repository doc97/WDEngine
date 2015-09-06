package gamedev.lwjgl.engine.input;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

public class InputManager {
	private static GLFWErrorCallback errCallback;
	private static GLFWKeyCallback keyCallback;
	
	private List<InputListener> listeners = new ArrayList<InputListener>();
	
	public void init(long window) {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		glfwSetErrorCallback(errCallback = errorCallbackPrint(System.err));
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE);
				
				// TODO implement observer pattern and input listeners
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
			}
		});
	}
	
	public void update() {
		for(InputListener listener : listeners)
			listener.update();
	}
	
	public void addListener(InputListener listener) {
		listeners.add(listener);
	}
	
	public void cleanup() {
		keyCallback.release();
		errCallback.release();
	}
}

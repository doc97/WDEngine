package gamedev.lwjgl.engine.render;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import gamedev.lwjgl.engine.Logger;

public class DisplayManager {
	
	private static GLFWWindowSizeCallback windowResizeCallback;
	
	public static final String title = "LWJGL Game";
	
	private long window;
	private int width, height;
	private boolean shouldResize;
	
	public boolean createDisplay(int width, int height) {
		if(window != 0) {
			Logger.error("DisplayManager", "Window has already been created");
			return false;
		}
		
		this.width = width;
		this.height = height;
		
		if(glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if(window == NULL)
			throw new RuntimeException("Failed to create GLFW window");
		
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwSetWindowSizeCallback(window, windowResizeCallback = new GLFWWindowSizeCallback() {
			
			@Override
			public void invoke(long window, int width, int height) {
				DisplayManager.this.width = width;
				DisplayManager.this.height = height;
				shouldResize = true;
			}
			
		});
		glfwShowWindow(window);
		
		GL.createCapabilities();

		glEnable(GL_ALPHA_TEST);
		
		return true;
	}
	
	public void setBackgroundColor(float r, float g, float b, float  a) {
		glClearColor(r, g, b, a);
	}
	
	public void clearDisplay() {
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void updateDisplay() {
		if (shouldResize) {
			GL11.glViewport(0, 0, width, height);
			shouldResize = false;
		}
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public void closeDisplay() {
		glfwSetWindowShouldClose(window, GL_TRUE);
	}
	
	public boolean displayShouldClose() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	public int getWindowWidth() {
		return width;
	}
	
	public int getWindowHeight() {
		return height;
	}
	
	public long getWindow() {
		return window;
	}
	
}

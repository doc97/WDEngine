package gamedev.lwjgl.engine.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.utils.Buffers;

public abstract class Shader {
	private int program;
	private String shaderFile;
	
	public Shader(String shaderFile) {
		program = loadShaderProgram(shaderFile);
		getAllUniformLocations();
	}
	
	private int loadShaderProgram(String shaderFile) {
		StringBuilder source = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(shaderFile));
			String line;
			while((line = br.readLine()) != null) {
				source.append(line + "\n");
			}
			br.close();
		} catch (IOException e) {
			Logger.error("Shader", "Could not read file: " + shaderFile);
			e.printStackTrace();
			System.exit(1);
		}
		
		String[] shaders = source.toString().split("@");
		String vertSrc = shaders[0];
		String fragSrc = shaders[1];
		
		int vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertShader, vertSrc);
		glCompileShader(vertShader);
		
		if(glGetShaderi(vertShader, GL_COMPILE_STATUS) == GL_FALSE) {
			Logger.debug("Shader", glGetShaderInfoLog(vertShader, 500));
			Logger.error("Shader", "Couldn't compile vertex shader");
			System.exit(1);
		}
		
		
		int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragShader, fragSrc);
		glCompileShader(fragShader);
		
		if(glGetShaderi(fragShader, GL_COMPILE_STATUS) == GL_FALSE) {
			if(Logger.isDebug())
				Logger.message("Shader", glGetShaderInfoLog(fragShader, 500));
			Logger.error("Shader", "Couldn't compile fragment shader");
			System.exit(1);
		}
		
		int shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertShader);
		glAttachShader(shaderProgram, fragShader);
		bindAttributes();
		glLinkProgram(shaderProgram);
		if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			if(Logger.isDebug())
				Logger.message("Shader", glGetProgramInfoLog(shaderProgram, 500));
			Logger.error("Shader", "Error linking shader program");
		}
		
		glDeleteShader(vertShader);
		glDeleteShader(fragShader);
		
		return shaderProgram;
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformLocations();
	
	protected void loadInt(int location, int value) {
		glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value) {
		glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector) {
		glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadBoolean(int location, boolean bool) {
		loadFloat(location, bool ? 1 : 0);
	}
	
	protected void loadMatrix(int location, Matrix4f mat) {
		FloatBuffer buf = Buffers.fillMatrixBuffer(mat);
		glUniformMatrix4fv(location, false, buf);
	}
	
	protected int getUniformLocation(String variableName) {
		return glGetUniformLocation(program, variableName);
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		glBindAttribLocation(program, attribute, variableName);
	}
	
	public void start() {
		glUseProgram(program);
	}
	
	public void stop() {
		glUseProgram(0);
	}
	
	public int getShaderProgram() {
		return program;
	}
	
	public String getFileName() {
		return shaderFile;
	}
}

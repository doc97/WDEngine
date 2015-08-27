package gamedev.lwjgl.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import gamedev.lwjgl.Logger;

public abstract class Shader {
	private int program;
	private String shaderFile;
	
	public Shader(String shaderFile) {
		program = loadShaderProgram(shaderFile);
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
			if(Logger.isDebug())
				Logger.message("Shader", glGetShaderInfoLog(vertShader, 500));
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

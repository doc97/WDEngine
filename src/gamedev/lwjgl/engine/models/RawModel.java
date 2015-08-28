package gamedev.lwjgl.engine.models;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import gamedev.lwjgl.engine.shaders.Shader;
import gamedev.lwjgl.engine.shaders.StaticShader;

public class RawModel {

	private int vao;
	private int vbo;
	private int ibo;
	private int tbo;
	
	private String filename;
	private int indexCount;
	private Shader shader;
	
	public RawModel(String filename, float[] positions, int[] indices, float[] texcoords) {
		this.filename = filename;
		indexCount = indices.length;
		shader = new StaticShader();
		
		createVAO();
		storeIndicesInBuffer(indices);
		storeDataInAttributeList(0, vbo, 3, positions);
		storeDataInAttributeList(1, tbo, 2, texcoords);
		unbindVAO();
	}
	
	public RawModel(String filename, float[] positions, int[] indices) {
		this.filename = filename;
		indexCount = indices.length;
		shader = new StaticShader();
		
		createVAO();
		storeIndicesInBuffer(indices);
		storeDataInAttributeList(0, vbo, 3, positions);
		unbindVAO();
	}
	
	private void createVAO() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
	}
	
	private void unbindVAO() {
		glBindVertexArray(0);
	}
	
	public void storeDataInAttributeList(int attribute, int vbo, int size, float[] data) {
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void storeIndicesInBuffer(int[] indices) {
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer,  GL_STATIC_DRAW);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public int getVAO() {
		return vao;
	}
	
	public int getVBO() {
		return vbo;
	}
	
	public int getIBO() {
		return ibo;
	}
	
	public int getTBO() {
		return tbo;
	}
	
	public int getIndexCount() {
		return indexCount;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public Shader getShader() {
		return shader;
	}
}

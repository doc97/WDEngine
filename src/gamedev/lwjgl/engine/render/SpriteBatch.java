package gamedev.lwjgl.engine.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.shaders.StaticShader;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class SpriteBatch {
	
	private Camera2d camera;
	private ModelTexture lastTexture;
	private int idx = 0;
	private int[] indices;
	private float[] vertices;
	private float[] texCoords;
	private boolean isDrawing;
	private StaticShader shader;
	private int vao;
	private int ibo;
	
	public void init() {
		camera = Engine.INSTANCE.camera;
		shader = new StaticShader();
		indices = new int[6000];
		vertices = new float[12000];
		texCoords = new float[8000];
		for (int i = 0, j = 0; i < 6000; i += 6, j += 4){
			indices[i] = j;
			indices[i+1] = (j+1);
			indices[i+2] = (j+2);
			indices[i+3] = (j+2);
			indices[i+4] = (j+3);
			indices[i+5] = j;
		}
		vao = GL30.glGenVertexArrays();
	}
	
	public void begin() {
		if (isDrawing)
			return;
		isDrawing = true;
		shader.start();
	}
	
	public void end() {
		if (!isDrawing)
			return;
		flush();
		shader.stop();
		lastTexture = null;
		isDrawing = false;
	}
	
	public void flush() {
		if (idx == 0)
			return;
		int sprites = idx / 12;
		
		loadToVAO(vertices, texCoords, indices);
		bindVAO();
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawElements(GL11.GL_TRIANGLES, sprites * 6, GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		unbindVAO();
		idx = 0;
	}
	
	public void changeTexture(ModelTexture texture) {
		flush();
		lastTexture = texture;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height) {
		draw(texture, xCoord, yCoord, width, height, 0, 0, texture.getWidth(), texture.getHeight(), 0,0,0);
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
		draw(texture, xCoord, yCoord, width, height, srcX, srcY, srcWidth, srcHeight, 0,0,0);
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height, float rotation, float anchorX, float anchorY) {
		draw(texture, xCoord, yCoord, width, height, 0, 0, texture.getWidth(), texture.getHeight(), rotation, anchorX, anchorY);
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight, float rotation, float anchorPX, float anchorPY) {
		if(!isDrawing)
			return;
		if (texture != lastTexture)
			changeTexture(texture);
		if (idx == vertices.length)
			flush();
		
		float x1 = xCoord / camera.getWidth();
		float y1 = yCoord / camera.getHeight();
		float x2 = (xCoord + width) / camera.getWidth();
		float y2 = (yCoord + height) / camera.getHeight();
		
		//coords for the vertices
		
		float vx1 = x1;
		float vy1 = y2;
		float vx2 = x2;
		float vy2 = y2;
		float vx3 = x2;
		float vy3 = y1;
		float vx4 = x1;
		float vy4 = y1;
		
		int idx = this.idx;
		int tdx = (idx / 3) << 1;
		
		if (rotation != 0){
			float p1d = (float) Math.hypot(anchorPX - vx1, anchorPY - vy1);
			float p2d = (float) Math.hypot(anchorPX - vx2, anchorPY - vy2);
			float p3d = (float) Math.hypot(anchorPX - vx3, anchorPY - vy3);
			float p4d = (float) Math.hypot(anchorPX - vx4, anchorPY - vy4);
			
			double angle = Math.atan2(vy1 - anchorPY, vx1 - anchorPX);
			vx1 = (float) (anchorPX + Math.cos(rotation + angle) * p1d);
			vy1 = (float) (anchorPY + Math.sin(rotation + angle) * p1d);
			
			angle = Math.atan2(vy2 - anchorPY, vx2 - anchorPX);
			vx2 = (float) (anchorPX + Math.cos(rotation + angle) * p2d);
			vy2 = (float) (anchorPY + Math.sin(rotation + angle) * p2d);
			
			angle = Math.atan2(vy3 - anchorPY, vx3 - anchorPX);
			vx3 = (float) (anchorPX + Math.cos(rotation + angle) * p3d);
			vy3 = (float) (anchorPY + Math.sin(rotation + angle) * p3d);
			
			angle = Math.atan2(vy4 - anchorPY, vx4 - anchorPX);
			vx4 = (float) (anchorPX + Math.cos(rotation + angle) * p4d);
			vy4 = (float) (anchorPY + Math.sin(rotation + angle) * p4d);
			
		}
		
		vertices[idx++] = vx1;
		vertices[idx++] = vy1;
		vertices[idx++] = 0;
		vertices[idx++] = vx2;
		vertices[idx++] = vy2;
		vertices[idx++] = 0;
		vertices[idx++] = vx3;
		vertices[idx++] = vy3;
		vertices[idx++] = 0;
		vertices[idx++] = vx4;
		vertices[idx++] = vy4;
		vertices[idx++] = 0;
		
		float tWidth = texture.getWidth();
		float tHeight = texture.getHeight();
		
		texCoords[tdx++] = srcX / tWidth;
		texCoords[tdx++] = srcY / tHeight;
		
		texCoords[tdx++] = (srcX + srcWidth) / tWidth;
		texCoords[tdx++] = srcY / tHeight;
		
		texCoords[tdx++] = (srcX + srcWidth) /  tWidth;
		texCoords[tdx++] = (srcY + srcHeight) / tHeight;
		
		texCoords[tdx++] = srcX / tWidth;
		texCoords[tdx++] = (srcY + srcHeight) / tHeight;
		
		this.idx = idx;
	}
	
	
	private void loadToVAO(float[] positions, float[] texCoords, int[] indices) {
		bindVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, texCoords);
		unbindVAO();
	}
	
	private void bindVAO() {
		GL30.glBindVertexArray(vao);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private void storeDataInAttributeList(int attribute, int size, float[] data) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}

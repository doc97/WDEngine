package gamedev.lwjgl.engine.render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.shaders.StaticShader;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class SpriteBatch {
	
	private Camera2d camera;
	private ModelTexture lastTexture;
	private int idx = 0;
	private int[] indices;
	private float[] vertices;
	private float[] texCoords;
	private float[] colors;
	private Color currentColor;
	private boolean isDrawing;
	private StaticShader shader;
	private int vao;
	private int ibo;
	private ArrayList<Integer> vbos;
	private IntBuffer intBuff;
	private FloatBuffer floatBuff1, floatBuff2, floatBuff3;
	
	public void init() {
		camera = Engine.INSTANCE.camera;
		shader = new StaticShader();
		indices = new int[6000];
		vertices = new float[12000];
		texCoords = new float[8000];
		colors = new float[16000];
		vbos = new ArrayList<>();
		intBuff = BufferUtils.createIntBuffer(6000);
		floatBuff1 = BufferUtils.createFloatBuffer(12000);
		floatBuff2 = BufferUtils.createFloatBuffer(8000);
		floatBuff3 = BufferUtils.createFloatBuffer(16000);
		currentColor = Color.WHITE;
		for (int i = 0, j = 0; i < 6000; i += 6, j += 4){
			indices[i] = j;
			indices[i+1] = (j+1);
			indices[i+2] = (j+2);
			indices[i+3] = (j+2);
			indices[i+4] = (j+3);
			indices[i+5] = j;
		}
		vao = glGenVertexArrays();
		ibo = glGenBuffers();
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
		
		bindVAO();
		loadToVAO(vertices, texCoords, colors, indices);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glDrawElements(GL_TRIANGLES, sprites * 6, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		unbindVAO();
		glDeleteBuffers(ibo);
		for (int vbo : vbos){
			glDeleteBuffers(vbo);
		}
		vbos.clear();
		idx = 0;
	}
	
	public void changeTexture(ModelTexture texture) {
		flush();
		lastTexture = texture.getTexture();
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height) {
		draw(texture, xCoord, yCoord, width, height, 0,0,0);
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height, float rotation, float anchorPX, float anchorPY) {
		if(!isDrawing)
			return;
		if (texture.getTexture() != lastTexture)
			changeTexture(texture);
		if (idx == vertices.length)
			flush();
		
		float x1 = 2 * (-camera.getX() + xCoord) / camera.getWidth();
		float y1 = 2 * (-camera.getY() + yCoord) / camera.getHeight();
		float x2 = 2 * (-camera.getX() + xCoord + width) / camera.getWidth();
		float y2 = 2 * (-camera.getY() + yCoord + height) / camera.getHeight();
		
//		System.out.println(xCoord + ", " + yCoord);
		
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
		int vertexCount = idx / 3;
		int cdx = vertexCount << 2;
		int tdx = vertexCount << 1;
		
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
		
		float[] uvs = texture.getUVs();
		
		texCoords[tdx++] = uvs[0];
		texCoords[tdx++] = uvs[1];
		
		texCoords[tdx++] = uvs[2];
		texCoords[tdx++] = uvs[1];
		
		texCoords[tdx++] = uvs[2];
		texCoords[tdx++] = uvs[3];
		
		texCoords[tdx++] = uvs[0];
		texCoords[tdx++] = uvs[3];
		
		for (int i = 0; i < 4; i++){
			colors[cdx++] = currentColor.red;
			colors[cdx++] = currentColor.green;
			colors[cdx++] = currentColor.blue;
			colors[cdx++] = currentColor.alpha;
		}
		
		this.idx = idx;
	}
	
	
	private void loadToVAO(float[] positions, float[] texCoords, float[] colors, int[] indices) {
		bindIndicesBuffer(indices, intBuff);
		storeDataInAttributeList(0, floatBuff1, 3, positions);
		storeDataInAttributeList(1, floatBuff2, 2, texCoords);
		storeDataInAttributeList(2, floatBuff3, 4, colors);
	}
	
	private void bindVAO() {
		glBindVertexArray(vao);
	}
	
	private void unbindVAO() {
		glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices, IntBuffer buffer) {
		ibo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		storeDataInIntBuffer(buffer, indices);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(IntBuffer buffer, int[] data) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private void storeDataInAttributeList(int attribute, FloatBuffer buffer, int size, float[] data) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		storeDataInFloatBuffer(buffer,data);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(FloatBuffer buffer, float[] data) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public Color getColor(){
		return currentColor;
	}
	
	public void setColor(Color color){
		this.currentColor = color;
	}
}

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

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.cameras.Camera2d;
import gamedev.lwjgl.engine.shaders.HBlurShader;
import gamedev.lwjgl.engine.shaders.SelectionShader;
import gamedev.lwjgl.engine.shaders.Shader;
import gamedev.lwjgl.engine.shaders.StaticShader;
import gamedev.lwjgl.engine.shaders.VBlurShader;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class SpriteBatch implements Cleanable{
	
	public static StaticShader staticShader;
	public static VBlurShader vBlurShader;
	public static HBlurShader hBlurShader;
	public static SelectionShader selectShader;
	
	private ArrayList<Integer> vbos;
	private IntBuffer intBuff;
	private FloatBuffer floatBuff1, floatBuff2, floatBuff3;
	private Camera2d camera = new Camera2d();
	private ModelTexture lastTexture;
	private Color currentColor;
	private Shader shader;
	private int vao;
	private int ibo;
	private int idx = 0;
	private int[] indices;
	private float[] vertices;
	private float[] texCoords;
	private float[] colors;
	private float scale = 1;
	private boolean isDrawing;
	
	public void init() {
		camera.init();
		staticShader = new StaticShader();
		vBlurShader = new VBlurShader();
		hBlurShader = new HBlurShader();
		selectShader = new SelectionShader();
		shader = staticShader;
		indices = new int[6000];
		vertices = new float[12000];
		texCoords = new float[8000];
		colors = new float[16000];
		vbos = new ArrayList<>();
		intBuff = BufferUtils.createIntBuffer(6000);
		floatBuff1 = BufferUtils.createFloatBuffer(12000);
		floatBuff2 = BufferUtils.createFloatBuffer(8000);
		floatBuff3 = BufferUtils.createFloatBuffer(16000);

		currentColor = new Color(1, 1, 1, 1);

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
	
	public void cleanup() {
		glDeleteBuffers(ibo);
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		vbos.clear();
		
		BufferUtils.zeroBuffer(intBuff);
		BufferUtils.zeroBuffer(floatBuff1);
		BufferUtils.zeroBuffer(floatBuff2);
		BufferUtils.zeroBuffer(floatBuff3);
	}
	
	public void begin() {
		if (isDrawing)
			return;
		isDrawing = true;
		shader.start();
		shader.load();
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
		glEnableVertexAttribArray(3);
		glDrawElements(GL_TRIANGLES, sprites * 6, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		
		unbindVAO();
		glDeleteBuffers(ibo);
		for (int vbo : vbos) {
			glDeleteBuffers(vbo);
		}
		vbos.clear();
		idx = 0;
	}
	
	private void changeTexture(ModelTexture texture) {
		flush();
		if(texture != null) {
			lastTexture = texture;
			glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		} else {
			lastTexture = null;
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}
	
	public void draw(ModelTexture texture, float xCoord, float yCoord, float width, float height) {
		draw(texture, xCoord, yCoord, width, height, texture.getUVs(), 0, 0, 0);
	}
	
	public void draw(ModelTexture texture, float x, float y, float width, float height,
			float[] uvs, float rotation, float anchorPX, float anchorPY) {
		Color[] colors = { currentColor, currentColor, currentColor, currentColor };
		draw(texture, x, y + height, x + width, y + height, x + width, y, x, y, uvs,
				colors, rotation, anchorPX, anchorPY);
	}
	
	public void draw(ModelTexture texture, float x1, float y1, float x2, float y2,
			float x3, float y3, float x4, float y4, float[] uvs,
			Color[] colours, float rotation, float anchorPX, float anchorPY) {
		
		if(!isDrawing)
			return;
		if (texture != lastTexture)
			changeTexture(texture);
		if (idx == vertices.length)
			flush();
		
		//coords for the vertices
		float vx1 = 2 * scale * (-camera.getX() + x1) / camera.getWidth() - (1 - scale);
		float vy1 = 2 * scale * (-camera.getY() + y1) / camera.getHeight() - (1 - scale);
		float vx2 = 2 * scale * (-camera.getX() + x2) / camera.getWidth() - (1 - scale);
		float vy2 = 2 * scale * (-camera.getY() + y2) / camera.getHeight() - (1 - scale);
		float vx3 = 2 * scale * (-camera.getX() + x3) / camera.getWidth() - (1 - scale);
		float vy3 = 2 * scale * (-camera.getY() + y3) / camera.getHeight() - (1 - scale);
		float vx4 = 2 * scale * (-camera.getX() + x4) / camera.getWidth() - (1 - scale);
		float vy4 = 2 * scale * (-camera.getY() + y4) / camera.getHeight() - (1 - scale);
		
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
		
		if(texture != null) {
			texCoords[tdx++] = uvs[0];
			texCoords[tdx++] = uvs[1];
			
			texCoords[tdx++] = uvs[2];
			texCoords[tdx++] = uvs[1];
			
			texCoords[tdx++] = uvs[2];
			texCoords[tdx++] = uvs[3];
			
			texCoords[tdx++] = uvs[0];
			texCoords[tdx++] = uvs[3];
		}

		for (int i = 0; i < 4; i++) {
			colors[cdx++] = colours[i].r;
			colors[cdx++] = colours[i].g;
			colors[cdx++] = colours[i].b;
			colors[cdx++] = colours[i].a;
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
	
	private void storeDataInAttributeList(int attribute, IntBuffer buffer, int size, int[] data) {
		int vbo = glGenBuffers();
		vbos.add(vbo);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		storeDataInIntBuffer(buffer,data);
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
	
	public Color getColor() {
		return new Color(currentColor.r, currentColor.g, currentColor.b, currentColor.a);
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setColor(Color color) {
		currentColor.r = color.r;
		currentColor.g = color.g;
		currentColor.b = color.b;
		currentColor.a = color.a;
	}
	
	public void setColor(float r, float g, float b, float a) {
		currentColor.r = r;
		currentColor.g = g;
		currentColor.b = b;
		currentColor.a = a;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public float getScale() {
		return scale;
	}
	
	public Camera2d getCamera(){
		return camera;
	}
	
	public Shader getShader() {
		return shader;
	}
}

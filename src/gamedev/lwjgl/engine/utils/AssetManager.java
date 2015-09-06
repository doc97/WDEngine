package gamedev.lwjgl.engine.utils;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class AssetManager {
	
	private static final String MODEL_PATH = "models/";
	private static final String TEXTURE_PATH = "textures/";
	private static final String FONT_PATH = "fonts/";
	private static final String[] CHARACTERS = { "a", "b", "c", "d", "e", "f", "g", "h", "i" };
	private static Map<String, RawModel> models = new HashMap<String, RawModel>();
	private static Map<String, ModelTexture> textures = new HashMap<String, ModelTexture>();
	private static Map<String, Font> fonts = new HashMap<String, Font>();
	
	public static void loadAssets(String[] modelNames, String[] textureNames, String[] fontNames) {
		loadModels(modelNames);
		loadTextures(textureNames);
		loadFonts(fontNames);
	}
	
	private static void loadModels(String[] modelNames) {
		for(String name : modelNames) {
			if(models.containsKey(name)) {
				Logger.message("AssetManager", "Model with name: " + name + " already loaded");
				continue;
			}
			RawModel model = loadOBJModel(name);
			if(model != null)
				models.put(name, model);
		}
		
		RawModel quad = loadQuad();
		if(quad != null)
			models.put("Quad", quad);
	}
	
	private static void loadTextures(String[] textureNames) {
		for(String name : textureNames) {
			if(textures.containsKey(name)) {
				Logger.message("AssetManager", "Texture with name: " + name + " already loaded");
				continue;
			}
			ModelTexture tex = loadTexture(TEXTURE_PATH + name);
			if(tex != null)
				textures.put(name, tex);
		}
	}
	
	private static void loadFonts(String[] fontNames) {
		for(String name : fontNames) {
			loadFont(name, CHARACTERS);
		}
	}
	
	private static void loadFont(String fontName, String[] characterNames) {
		ModelTexture[] characters = new ModelTexture[characterNames.length];
		for(int i = 0; i < characterNames.length; i++) {
			characters[i] = loadTexture(FONT_PATH + fontName + "/" + characterNames[i]);
		}
		Font font = new Font(fontName, characters);
		fonts.put(fontName, font);
	}
	
	public static RawModel getModel(String filename) {
		return models.get(filename);
	}
	
	public static TexturedModel getTexturedModel(String modelName, String textureName) {
		return new TexturedModel(getModel(modelName), getTexture(textureName));
	}
	
	public static ModelTexture getTexture(String filename) {
		return textures.get(filename);
	}
	
	public static Font getFont(String name) {
		if(!fonts.containsKey(name))
			return fonts.get("serif");
		return fonts.get(name);
	}
	
	/**
	 * Temporary method for creating an easy testing model
	 * @return
	 */
	public static RawModel loadQuad() {
		float[] vertices = {
				-0.5f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.5f, 0.5f, 0.0f
		};
		
		int[] indices = {
			0, 1, 3,
			3, 1, 2
		};
		
		float[] texcoords = {
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		
		return new RawModel("Quad", vertices, indices, texcoords);
	}
	
	private static RawModel loadOBJModel(String filename) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(MODEL_PATH + filename + ".obj"));
		} catch (FileNotFoundException e) {
			Logger.error("AssetManager", "No object file found with name: " + filename + ".obj");
			return null;
		}
		
		BufferedReader br = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texcoords = new ArrayList<Vector2f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] texcoordsArray = null;
		int[] indicesArray = null;
		
		try {
			while(true) {
				line = br.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if(line.startsWith("vt ")) {
					Vector2f uv = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					texcoords.add(uv);
				} else if(line.startsWith("f ")) {
					texcoordsArray = new float[vertices.size() * 2];
					break;
				}
			}
			
			while(line != null) {
				if(line.startsWith("f ")) {
					line = br.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[1].split("/");
				String[] vertex3 = currentLine[1].split("/");
				
				processVertex(vertex1, indices, texcoords, texcoordsArray);
				processVertex(vertex2, indices, texcoords, texcoordsArray);
				processVertex(vertex3, indices, texcoords, texcoordsArray);
				line = br.readLine();
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		// Copying over vertices
		int vertexPointer = 0;
		for(Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		// Copying over indices
		int indexPointer = 0;
		for(int index : indices) {
			indicesArray[indexPointer++] = index;
		}
		
		return new RawModel(filename, verticesArray, indicesArray, texcoordsArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
			List<Vector2f> texcoords, float[] texArray) {
		// Indices
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; // Obj-format
		indices.add(currentVertexPointer);
		
		// Texture coordinates
		Vector2f currentTex = texcoords.get(Integer.parseInt(vertexData[1]) - 1);
		texArray[currentVertexPointer*2] = currentTex.x;
		texArray[currentVertexPointer*2 + 1] = 1 - currentTex.y; // Blender starts bottom-left, not top-left		
	}
	
	private static ModelTexture loadTexture(String filename) {
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
			InputStream in = new FileInputStream(filename + ".png");
			PNGDecoder decoder = new PNGDecoder(in);
			
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();
			
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		int textureID = glGenTextures();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tWidth, tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return new ModelTexture(textureID, tWidth, tHeight);
	}
	
	public static void cleanup() {
		for(String key : models.keySet()) {
			glDeleteVertexArrays(models.get(key).getVAO());
			glDeleteBuffers(models.get(key).getVBO());
			glDeleteBuffers(models.get(key).getIBO());
		}
		
		for(String key : textures.keySet()) {
			glDeleteTextures(textures.get(key).getTextureID());
		}
		models.clear();
	}
}
package gamedev.lwjgl.engine.utils;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
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
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.data.AnimationData;
import gamedev.lwjgl.engine.data.CreditsData;
import gamedev.lwjgl.engine.data.GameData;
import gamedev.lwjgl.engine.data.IntroData;
import gamedev.lwjgl.engine.data.MainMenuData;
import gamedev.lwjgl.engine.data.MapData;
import gamedev.lwjgl.engine.data.OutroData;
import gamedev.lwjgl.engine.data.PhysicsData;
import gamedev.lwjgl.engine.data.PlayerData;
import gamedev.lwjgl.engine.data.WaterData;
import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.font.Glyph;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.sound.Sound;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.textures.TextureRegion;
import gamedev.lwjgl.game.text.Dialog;

public class AssetManager {
	
	private static final String ASSET_PATH = "assets/";
	private static final String MAP_PATH = ASSET_PATH + "maps/";
	private static final String MODEL_PATH = ASSET_PATH + "assets/models/";
	private static final String TEXTURE_PATH = ASSET_PATH + "textures/";
	private static final String FONT_PATH = ASSET_PATH + "fonts/";
	private static final String ANIMATION_PATH = ASSET_PATH + "animations/";
	private static final String DATA_FILE_PATH = ASSET_PATH + "data/";
	private static final String SOUND_FILE_PATH = ASSET_PATH + "sounds/";
	private static final String DIALOG_PATH = ASSET_PATH + "dialogs/";
	private static Map<String, RawModel> models = new HashMap<String, RawModel>();
	private static Map<String, ModelTexture> textures = new HashMap<String, ModelTexture>();
	private static Map<String, Font> fonts = new HashMap<String, Font>();
	private static Map<String, List<TextureRegion>> animationTextures = new HashMap<String, List<TextureRegion>>();
	private static Map<String, gamedev.lwjgl.game.map.Map> maps = new HashMap<String, gamedev.lwjgl.game.map.Map>();
	private static Map<String, Sound> sounds = new HashMap<String, Sound>();
	private static Map<String, Dialog> dialogs = new HashMap<String, Dialog>();
	private static CreditsData creditsData;
	private static GameData gameData;
	private static IntroData introData;
	private static OutroData outroData;
	private static MainMenuData mainmenuData;
	private static PhysicsData physicsData;
	private static PlayerData playerData;
	
	public static void loadAssets(String assetFile) throws IOException {
		Logger.debug("AssetManager", "------------------");
		Logger.debug("AssetManager", "Loading assets");
		FileReader fr = null;
		try {
			fr = new FileReader(new File(assetFile + ".assets"));
		} catch(FileNotFoundException e) {
			Logger.error("AssetManager", "No asset file found with name: " + assetFile);
		}
		
		List<String> modelNames = new ArrayList<String>();
		List<String> textureNames = new ArrayList<String>();
		List<String> animationNames = new ArrayList<String>();
		List<String> fontNames = new ArrayList<String>();
		List<String> mapNames = new ArrayList<String>();
		List<String> soundFileNames = new ArrayList<String>();
		List<String> dialogNames = new ArrayList<String>();
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null) {
				String assetLine = line.substring(line.indexOf("="));
				assetLine = assetLine.replace("=", "");
				
				String[] values = assetLine.split(";");
				
				if(values[0].equals(""))
					continue;
				
				if(line.startsWith("models")) {
					for(String value : values)
						modelNames.add(value);
				} else if(line.startsWith("textures")) {
					for(String value : values)
						textureNames.add(value);
				} else if(line.startsWith("animations")) {
					for(String value : values)
						animationNames.add(value);
				} else if(line.startsWith("fonts")) {
					for(String value : values)
						fontNames.add(value);
				} else if(line.startsWith("maps")) {
					for(String value : values)
						mapNames.add(value);
				} else if (line.startsWith("sounds")) {
					for (String value : values)
						soundFileNames.add(value);
				} else if (line.startsWith("dialogs")) {
					for (String value : values)
						dialogNames.add(value);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		loadDataFiles();
		loadModels(modelNames);
		loadTextures(textureNames);
		loadAnimations(animationNames);
		loadFonts(fontNames);
		loadMaps(mapNames);
		loadSoundFiles(soundFileNames);
		loadDialogFiles(dialogNames);
		
		Logger.debug("AssetManager", "Assets loaded");
		Logger.debug("AssetManager", "------------------");
	}
	
	public static void loadDataFiles() throws IOException {
		Gson gson = new Gson();
		
		// Load credits state data
		Logger.debug("AssetManager", "Loading data file: credits.data");
		Reader reader = new FileReader(DATA_FILE_PATH + "credits.data");
		creditsData = gson.fromJson(reader, CreditsData.class);
		reader.close();
		
		// Load game data
		Logger.debug("AssetManager", "Loading data file: game.data");
		reader = new FileReader(DATA_FILE_PATH + "game.data");
		gameData = gson.fromJson(reader, GameData.class);
		reader.close();
		
		// Load intro state data
		Logger.debug("AssetManager", "Loading data file: intro.data");
		reader = new FileReader(DATA_FILE_PATH + "intro.data");
		introData = gson.fromJson(reader, IntroData.class);
		reader.close();
		
		// Load outro state data
		Logger.debug("AssetManager", "Loading data file: outro.data");
		reader = new FileReader(DATA_FILE_PATH + "outro.data");
		outroData = gson.fromJson(reader, OutroData.class);
		reader.close();
		
		// Load main menu data
		Logger.debug("AssetManager", "Loading data file: mainmenu.data");
		reader = new FileReader(DATA_FILE_PATH + "mainmenu.data");
		mainmenuData = gson.fromJson(reader, MainMenuData.class);
		reader.close();
		
		// Load physics data
		Logger.debug("AssetManager", "Loading data file: physics.data");
		reader = new FileReader(DATA_FILE_PATH + "physics.data");
		physicsData = gson.fromJson(reader, PhysicsData.class);
		reader.close();
		
		// Load player data
		Logger.debug("AssetManager", "Loading data file: player.data");
		reader = new FileReader(DATA_FILE_PATH + "player.data");
		playerData = gson.fromJson(reader, PlayerData.class);
		reader.close();

	}
	
	private static void loadSoundFiles(List<String> soundFileNames) {
		for (String name : soundFileNames){
			if (sounds.containsKey(name)){
				Logger.message("AssetManager", "Sound with name: " + name + " already loaded");
				continue;
			}
			Sound s = new Sound(SOUND_FILE_PATH + name + ".wav");
			if (s != null)
				sounds.put(name, s);
		}
	}
	
	private static void loadDialogFiles(List<String> dialogNames) {
		for(String name : dialogNames) {
			if(dialogs.containsKey(name)) {
				Logger.message("AssetManager", "Dialog with name: " + name + "already loaded");
				continue;
			}
			Map<String, Dialog> d = loadDialogFile(name);
			if(d != null)
				dialogs = d;
			else
				Logger.message("AssetManager", "Failed to load dialog file with name: " + name);
		}
	}

	private static void loadModels(List<String> modelNames) {
		for(String name : modelNames) {
			if(models.containsKey(name)) {
				Logger.message("AssetManager", "Model with name: " + name + " already loaded");
				continue;
			}
			RawModel model = loadOBJModel(name);
			if(model != null)
				models.put(name, model);
			else
				Logger.message("AssetManager", "Failed to load model with name: " + name);
		}
		
		RawModel quad = loadQuad();
		if(quad != null)
			models.put("Quad", quad);
	}
	
	private static void loadTextures(List<String> textureNames) {
		for(String name : textureNames) {
			if(textures.containsKey(name)) {
				Logger.message("AssetManager", "Texture with name: " + name + " already loaded");
				continue;
			}
			ModelTexture tex = loadTexture(TEXTURE_PATH + name, Format.RGBA);
			if(tex != null)
				textures.put(name, tex);
			else
				Logger.message("AssetManager", "Failed to load texture with name: " + name);
		}
	}
	
	private static void loadAnimations(List<String> animationNames) throws IOException {
		for(String name : animationNames) {
			if(animationTextures.containsKey(name)) {
				Logger.message("AssetManager", "Animation with name: " + name + " already loaded");
				continue;
			}
			List<TextureRegion> frames = loadAnimation(name);
			if(frames != null)
				animationTextures.put(name, frames);
			else
				Logger.message("AssetManager", "Failed to load animation with name: " + name);
		}
	}
	
	private static void loadFonts(List<String> fontNames) {
		for(String name : fontNames) {
			loadFont(name);
		}
	}
	
	private static void loadMaps(List<String> mapNames) {
		for(String name : mapNames) {
			if(maps.containsKey(name)) {
				Logger.message("AssetManager", "Map with name: " + name + " already loaded");
				continue;
			}
			gamedev.lwjgl.game.map.Map map = loadMap(name);
			
			if(map != null)
				maps.put(name, map);
			else
				Logger.message("AssetManager", "Failed to load map with name: " + name);
		}
	}
	
	private static void loadFont(String filename) {
		Logger.debug("AssetManager", "Loading font: " + filename);
		FileReader fr = null;
		try {
			fr = new FileReader(new File(FONT_PATH + filename + "/" + filename + ".fnt"));
		} catch(FileNotFoundException e) {
			Logger.error("AssetManager", "No font file found with name: " + filename);
		}
		
		// Load font texture
		ModelTexture texture = loadTexture(FONT_PATH + filename + "/" + filename, Format.RGBA);
		
		BufferedReader br = new BufferedReader(fr);
		String line;
		int size = 0;
		String fontName = "";
		Map<Integer, Glyph> glyphs = new HashMap<Integer, Glyph>();
		try {
			while((line = br.readLine()) != null) {
				if(line.startsWith("char ")) {
					String[] values = line.split("\\s+");
					
					// Reading data
					int id = Integer.parseInt(values[1].split("=")[1]); 	// id
					float x = Integer.parseInt(values[2].split("=")[1]);	// x
					float y = Integer.parseInt(values[3].split("=")[1]);	// y
					int width = Integer.parseInt(values[4].split("=")[1]);	// width
					int height = Integer.parseInt(values[5].split("=")[1]);	// height

					int xoffset = Integer.parseInt(values[6].split("=")[1]);
					int yoffset = Integer.parseInt(values[7].split("=")[1]);
					int xadvance = Integer.parseInt(values[8].split("=")[1]);
					
					// Creating texture region out of data
					TextureRegion region = new TextureRegion(texture, x, y, width, height);
					
					Glyph glyph = new Glyph(region, id, width, height, xoffset, yoffset, xadvance);
					glyphs.put(id, glyph);
				} else if(line.startsWith("info")) {
					String[] data = line.split(" ");
					fontName = data[1].split("=")[1];
					size = Integer.parseInt(data[2].split("=")[1]);
				}
			}
		} catch (IOException e) {
			Logger.error("AssetManager", "Failed to read font file with name: " + filename);
			return;
		}
		
		Font font = new Font(fontName, size, texture, glyphs);
		fonts.put(filename, font);
	}
	
	private static Map<String, Dialog> loadDialogFile(String name) {
		Logger.debug("AssetManager", "Loading dialog file: " + name + ".json");
		try(Reader reader = new FileReader(DIALOG_PATH + name + ".json")) {
			Gson gson = new Gson();
			Type stringDialogMap = new TypeToken<Map<String, Dialog>>(){}.getType();
            Map<String, Dialog> map = gson.fromJson(reader, stringDialogMap);
            return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	
	private static gamedev.lwjgl.game.map.Map loadMap(String filename) {
		Logger.debug("AssetManager", "Loading map: " + filename);
		try(Reader reader = new FileReader(MAP_PATH + filename + "/" + filename + ".map")) {
			Gson gson = new Gson();
            MapData m = gson.fromJson(reader, MapData.class);
            
            List<Line> lines = new ArrayList<Line>();
            List<Water> waters = new ArrayList<Water>();
            List<ModelTexture> textures = new ArrayList<ModelTexture>();
            
            for(String name : m.textures)
            	textures.add(getTexture(name));
            
    		// Load collision map
    		for(String name : m.objs)
    			lines.addAll(loadLineSegments(filename + "/" + name));
    		
    		// Load waters
    		for(WaterData wd : m.waters)
    			waters.add(new Water(wd.x, wd.y, wd.width, wd.height));
    		
    		gamedev.lwjgl.game.map.Map map = null;
    		if(m.name.equals("demomap"))
    			map = new gamedev.lwjgl.game.map.Map1();
    		else if(m.name.equals("beginnings"))
    			map = new gamedev.lwjgl.game.map.Map2();
    		
    		if(map != null) {
    			map.setName(m.name);
    			map.setTextures(textures);
    			map.setCollisionMap(lines);
    			map.setWaters(waters);
    		}
    		return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<Line> loadLineSegments(String filename) {
		FileReader fr = null;
		String path = MAP_PATH + filename + ".obj";
		try {
			fr = new FileReader(new File(path));
		} catch(FileNotFoundException e) {
			Logger.error("AssetManager", "No collision map file found with name: " + path);
			return null;
		}
		
		List<Line> edges = new ArrayList<Line>();
		List<Vector2f> vertices = new ArrayList<Vector2f>();
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine()) != null) {
				String data[] = line.split(" ");
				if(data[0].equals("v")) {
					Vector2f vertex = new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
					vertices.add(vertex);
				} else if(data[0].equals("f") || data[0].equals("l")) {
					Line edge = new Line(vertices.get(Integer.parseInt(data[1]) - 1),
							vertices.get(Integer.parseInt(data[2]) - 1));
					edges.add(edge);
				}
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return edges;
	}
	
	private static RawModel loadOBJModel(String filename) {
		Logger.debug("AssetManager", "Loading model: " + filename + ".obj");
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
	
	private static ModelTexture loadTexture(String filename, Format format) {
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
			InputStream in = new FileInputStream(filename + ".png");
			PNGDecoder decoder = new PNGDecoder(in);
			
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();
			
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, format);
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
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tWidth, tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		return new ModelTexture(textureID, tWidth, tHeight);
	}
	
	private static List<TextureRegion> loadAnimation(String filename) throws IOException {
		Gson gson = new Gson();
		Reader reader = new FileReader(ANIMATION_PATH + filename + ".anim");
		ArrayList<TextureRegion> ts = new ArrayList<>();
		AnimationData aData = gson.fromJson(reader, AnimationData.class);
		ModelTexture texture = loadTexture(TEXTURE_PATH + aData.texture, Format.RGBA);
		int k = 0;
		bigloop:
		for (int i = 0; i*aData.frameheight < texture.getHeight(); i++){
			for (int j = 0; j*aData.framewidth < texture.getWidth(); j++){
				if (k++ > aData.framecount)
					break bigloop;
				ts.add(new TextureRegion(texture, j * aData.framewidth, i * aData.frameheight, aData.framewidth, aData.frameheight));
			}
		}
		reader.close();
		
		return ts;
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
	
	public static List<TextureRegion> getAnimationFrames(String filename) {
		return animationTextures.get(filename);
	}
	
	public static Font getFont(String name) {
		return fonts.get(name);
	}
	
	public static Sound getSound(String name) {
		return sounds.get(name);
	}
	
	public static Dialog getDialog(String name) {
		return dialogs.get(name);
	}
	
	public static gamedev.lwjgl.game.map.Map getMap(String name) {
		if(maps.containsKey(name))
			return maps.get(name);
		else
			return null;
	}
	
	public static CreditsData getCreditsData() {
		return creditsData;
	}
	
	public static GameData getGameData() {
		return gameData;
	}
	
	public static IntroData getIntroData() {
		return introData;
	}
	
	public static OutroData getOutroData() {
		return outroData;
	}
	
	public static MainMenuData getMainMenuData() {
		return mainmenuData;
	}
	
	public static PhysicsData getPhysicsData() {
		return physicsData;
	}
	
	public static PlayerData getPlayerData() {
		return playerData;
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

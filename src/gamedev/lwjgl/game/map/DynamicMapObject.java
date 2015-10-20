package gamedev.lwjgl.game.map;

import java.util.ArrayList;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.textures.ModelTexture;
import gamedev.lwjgl.engine.utils.AssetManager;

public class DynamicMapObject {
	
	private ModelTexture tex;
	private float x, y;
	private float width, height;
	private Vector2f speed;
	private ArrayList<Line> lines;
	
	public DynamicMapObject(float x, float y, float width, float height) {
		ModelTexture tex = AssetManager.getTexture("tree");
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tex = tex;
		
		Vector2f c1 = new Vector2f(x, y);
		Vector2f c2 = new Vector2f(x + width, y);
		Vector2f c3 = new Vector2f(x + width, y + height);
		Vector2f c4 = new Vector2f(x, y + height);
		
		lines = new ArrayList<Line>();
		lines.add(new Line(c1, c2));
		lines.add(new Line(c2, c3));
		lines.add(new Line(c3, c4));
		lines.add(new Line(c4, c1));
	}
	
	public void render(){
		
	}
	
	public void update(){
		
	}
	
	public Vector2f getSpeed(){
		return speed;
	}
	
	public ArrayList<Line> getLines(){
		return lines;
	}
	
}

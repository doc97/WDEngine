package gamedev.lwjgl.game.map;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.physics.Rectangle;

public class CollisionMap {
	private List<Rectangle> rects = new ArrayList<Rectangle>();
	
	public void addRectangle(Rectangle rect) {
		rects.add(rect);
	}
	
	public List<Rectangle> getRectangles() {
		return rects;
	}
}

package gamedev.lwjgl.game;

import java.util.ArrayList;
import java.util.List;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.Rectangle;

public class GamePhysics {
	
	private List<Rectangle> staticRects = new ArrayList<Rectangle>();
	private List<Rectangle> dynamicRects = new ArrayList<Rectangle>();
	private List<Circle> staticCircles = new ArrayList<Circle>();
	private List<Circle> dynamicCircles = new ArrayList<Circle>();
	
	public void addRect(Rectangle rect, boolean dynamic) {
		if(dynamic) dynamicRects.add(rect);
		else staticRects.add(rect);
	}
	
	public void addCircle(Circle c, boolean dynamic) {
		if(dynamic) dynamicCircles.add(c);
		else staticCircles.add(c);
	}
	
	public void update() {
		
	}
}

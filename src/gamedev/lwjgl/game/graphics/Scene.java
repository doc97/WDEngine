package gamedev.lwjgl.game.graphics;

import java.util.HashMap;
import java.util.Map;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.utils.Timer;

public class Scene {
	
	private Color color = new Color(1, 1, 1, 1);
	private Timer fadeIn = new Timer();
	private Timer fadeOut = new Timer();
	private Timer show = new Timer();
	private Map<String, SceneObject> objects = new HashMap<String, SceneObject>();
	private int fadeInTick;
	private int fadeOutTick;
	private int showTick;
	private boolean finished;
	
	public Scene(int fadeInTick, int showTick, int fadeOutTick) {
		this.fadeInTick = fadeInTick;
		this.showTick = showTick;
		this.fadeOutTick = fadeOutTick;
		fadeIn.set(fadeInTick);
		fadeOut.set(fadeOutTick);
		show.set(showTick);
	}
	
	public void start() {
		finished = false;
		fadeIn.setActive(true);
	}
	
	public void reload() {
		finished = false;
		fadeIn.set(fadeInTick);
		fadeOut.set(fadeOutTick);
		show.set(showTick);
	}
	
	public void stop() {
		finished = true;
	}
	
	public void update() {
		if(!finished) {
			fadeIn.update();
			fadeOut.update();
			show.update();
			
			for(String sKey : objects.keySet())
				objects.get(sKey).update();
			
			if(fadeIn.getPercentage() == 1) {
				fadeIn.setActive(false);
				show.setActive(true);
			}
			
			if(show.getPercentage() == 1) {
				show.setActive(false);
				fadeOut.setActive(true);
			}
			
			if(fadeOut.getPercentage() == 1) {
				fadeOut.setActive(false);
				stop();
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.setColor(color);
		for(String sKey : objects.keySet()) {
			objects.get(sKey).render(batch);
		}
	}
	
	public void addObject(String name, SceneObject object) {
		objects.put(name, object);
	}
	
	public void setColor(float r, float g, float b, float a) {
		color.setColor(r, g, b, a);
	}
	
	public SceneObject getObject(String name) {
		return objects.get(name);
	}
	public Map<String, SceneObject> getObjects() {
		return objects;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Timer getFadeInTimer() {
		return fadeIn;
	}
	
	public Timer getFadeOutTimer() {
		return fadeOut;
	}
	
	public Timer getShowTimer() {
		return show;
	}
	
	public boolean hasFinished() {
		return fadeOut.getPercentage() == 1;
	}
}

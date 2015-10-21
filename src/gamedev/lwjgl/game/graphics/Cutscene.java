package gamedev.lwjgl.game.graphics;

import java.util.ArrayList;

import gamedev.lwjgl.engine.render.SpriteBatch;

public class Cutscene {
	
	private ArrayList<Scene> scenes = new ArrayList<Scene>();
	private int currentScene = -1;
	private boolean finished;
	
	public void start() {
		if(scenes.size() > 0) {
			currentScene = 0;
			for(Scene s : scenes)
				s.reload();
			scenes.get(currentScene).start();
			finished = false;
		}
	}
	
	public void update() {
		if(!finished && currentScene >= 0) {
			scenes.get(currentScene).update();
			
			if(scenes.get(currentScene).hasFinished()) {
				currentScene++;
				if(currentScene < scenes.size())
					scenes.get(currentScene).start();
			}
			
			if(currentScene >= scenes.size())
				finished = true;
		}
	}
	
	public void render(SpriteBatch batch) {
		if(!finished) {
			Scene scene = scenes.get(currentScene);
			float value = 1;
			if(scene.getFadeInTimer().isActive())
				value = scene.getFadeInTimer().getPercentage();
			else if(scene.getFadeOutTimer().isActive())
				value = 1 - scene.getFadeOutTimer().getPercentage();
			
			scene.setColor(value, value, value, 1);
			scene.render(batch);
		}
	}
	
	public void addScene(Scene scene) {
		scenes.add(scene);
	}
	
	public int getCurrentScene() {
		return currentScene;
	}
	
	public boolean hasFinished() {
		return finished;
	}
}

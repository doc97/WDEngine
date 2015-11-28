package gamedev.lwjgl.game.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamedev.lwjgl.engine.render.SpriteBatch;

public class Cutscene {
	
	private Map<String, Scene> scenes = new HashMap<String, Scene>();
	private List<String> sceneNames = new ArrayList<String>();
	private int currentSceneIndex = -1;
	private String currentSceneName = "";
	private boolean finished;
	
	public void start() {
		if(scenes.size() > 0) {
			currentSceneIndex = 0;
			for(String s : scenes.keySet())
				scenes.get(s).reload();
			
			currentSceneName = sceneNames.get(currentSceneIndex);
			scenes.get(currentSceneName).start();
			finished = false;
		}
	}
	
	public void update() {
		if(!finished && currentSceneIndex >= 0) {
			scenes.get(currentSceneName).update();
			
			if(scenes.get(currentSceneName).hasFinished()) {
				currentSceneIndex++;
				if(currentSceneIndex < scenes.size()) {
					currentSceneName = sceneNames.get(currentSceneIndex);
					scenes.get(currentSceneName).start();
				}
			}
			
			if(currentSceneIndex >= scenes.size())
				finished = true;
		}
	}
	
	public void render(SpriteBatch batch) {
		if(!finished) {
			Scene scene = scenes.get(currentSceneName);
			float value = 1;
			if(scene.getFadeInTimer().isActive())
				value = scene.getFadeInTimer().getPercentage();
			else if(scene.getFadeOutTimer().isActive())
				value = 1 - scene.getFadeOutTimer().getPercentage();
			
			scene.setColor(value, value, value, 1);
			scene.render(batch);
		}
	}
	
	public void addScene(String name, Scene scene) {
		scenes.put(name, scene);
		sceneNames.add(name);
	}
	
	public void clear() {
		scenes.clear();
		sceneNames.clear();
	}
	
	public int getCurrentSceneIndex() {
		return currentSceneIndex;
	}
	
	public String getCurrentSceneName() {
		return currentSceneName;
	}
	
	public Scene getScene(String name) {
		return scenes.get(name);
	}
	
	public Map<String, Scene> getScenes() {
		return scenes;
	}
	
	public boolean hasFinished() {
		return finished;
	}
}

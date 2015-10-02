package gamedev.lwjgl.game.quests;

import java.util.ArrayList;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.ui.Timer;

public class Quest {
	
	private float progress;
	private Timer timer;
	private ArrayList<Item> items;
	private int initialItemCount;
	
	public Quest(){
		
	}
	
	public void addTimer(Timer timer){
		this.timer = timer;
	}
	
	public void addItems(ArrayList<Item> items){
		this.items = items;
		initialItemCount = items.size();
	}
	
	public boolean hasItems(){
		return items != null;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public void update(float delta){
		if (timer != null){
			if (!timer.update(delta)){
				//time is up do something
			}
		}
		if (items != null){
			if (items.size() == 0){
				//everything is collected do something
			}
			progress = 1.0f - ((float)items.size() / (float)initialItemCount);
		}
	}
	
	public void render(){
		if (timer != null)
			timer.render(Engine.INSTANCE.uiBatch);
	}
	
	public float getProgress(){
		return progress;
	}
	
}

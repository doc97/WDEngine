package gamedev.lwjgl.game.systems;

import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.quests.Quest;

import java.util.ArrayList;

public class QuestSystem {
	
	private ArrayList<Quest> quests;
	
	
	public QuestSystem(){
		quests = new ArrayList<Quest>();
		
	}
	
	public void addQuest(Quest quest){
		quests.add(quest);
	}
	
	public void update(){
		for (Quest q : quests){
			q.update();
		}
	}
	
	public ArrayList<Item> getAllItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		for (Quest q : quests){
			if (q.hasItems())
				items.addAll(q.getItems());
		}
		return items;
	}
	
	public void render(){
		for (Quest q : quests){
			q.render();
		}
	}
	
	public float getProgress(){
		float progress = 0;
		for (Quest q : quests){
			progress += q.getProgress();
		}
		progress /= quests.size();
		
		return progress;
	}
	
}
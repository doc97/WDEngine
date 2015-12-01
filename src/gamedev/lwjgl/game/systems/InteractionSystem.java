package gamedev.lwjgl.game.systems;

import java.util.ArrayList;
import java.util.Iterator;

import gamedev.lwjgl.engine.Cleanable;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.Interaction;

public class InteractionSystem implements Cleanable {

	private ArrayList<Interaction> interactions = new ArrayList<Interaction>();
	
	public void addInteraction(Interaction i) {
		i.init();
		interactions.add(i);
	}
	
	public void clear() {
		interactions.clear();
	}
	
	public void interact() {
		for(Interaction i : interactions)
			if(i.isInRange())
				i.interact();
	}
	
	public void update() {
		for(Iterator<Interaction> it = interactions.iterator(); it.hasNext();) {
			Interaction i = it.next();
			i.update();
			if(i.isFinished())
				it.remove();
		}
	}
	
	public void render(SpriteBatch batch) {
		for(Interaction i : interactions)
			i.render(batch);
	}
	
	public void cleanup() {
		interactions.clear();
	}
}
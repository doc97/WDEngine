package gamedev.lwjgl.game.graphics.effects;

import java.util.ArrayList;
import java.util.List;

public abstract class Pool<T> {
	
	private List<T> poolable = new ArrayList<T>();
	private boolean[] inUse;
	
	public Pool(int size) {
		for(int i = 0; i < size; i++)
			poolable.add(newObject());
			
		inUse = new boolean[size];
		for(int i = 0; i < size; i++)
			inUse[i] = false;
	}
	
	protected abstract T newObject();
	
	public T obtain() {
		for(int i = 0; i < inUse.length; i++) {
			if(!inUse[i]) {
				inUse[i] = true;
				return poolable.get(i);
			}
		}
		return null;
	}
	
	public void free(T t) {
		int i = poolable.indexOf(t);
		inUse[i] = false;
	}
}

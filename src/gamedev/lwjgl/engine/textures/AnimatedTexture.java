package gamedev.lwjgl.engine.textures;

import java.util.LinkedList;
import java.util.List;

public class AnimatedTexture {
	
	private LinkedList<TextureRegion> textures;
	private TextureRegion current;
	private int frameTicks;
	private int last;
	private boolean looping;
	private boolean finished;
	
	public AnimatedTexture(List<TextureRegion> texs, int frameTicks, boolean looping) {
		textures = new LinkedList<>();
		if(texs != null)
			textures.addAll(texs);
		this.frameTicks = frameTicks;
		
		if(!textures.isEmpty())
			this.current = textures.getFirst();
		last = 0;
		this.looping = looping;
	}
	
	public TextureRegion getCurrent() {
		return current;
	}
	
	public void update() {
		last++;
		if(textures.isEmpty() && !looping) {
			finished = true;
			return;
		}
		
		if (last > frameTicks) {
			current = textures.removeFirst();
			if(looping)
				textures.add(current);
			last = 0;
		}
	}
	
	public int getLength() {
		return textures.size() * frameTicks;
	}
	
	public boolean isFinished() {
		return finished;
	}
}

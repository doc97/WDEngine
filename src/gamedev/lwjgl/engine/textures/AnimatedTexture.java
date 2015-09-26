package gamedev.lwjgl.engine.textures;

import java.util.LinkedList;
import java.util.List;

public class AnimatedTexture {
	
	private LinkedList<TextureRegion> textures;
	private TextureRegion current;
	private float frameTime;
	private float last;
	private boolean looping;
	private boolean finished;
	
	public AnimatedTexture(List<TextureRegion> texs, float frameTime, boolean looping) {
		textures = new LinkedList<>();
		if(texs != null)
			textures.addAll(texs);
		this.frameTime = frameTime;
		
		if(!textures.isEmpty())
			this.current = textures.getFirst();
		last = 0;
		this.looping = looping;
	}
	
	public TextureRegion getCurrent() {
		return current;
	}
	
	public void update(float delta) {
		last += delta;
		if(textures.isEmpty() && !looping) {
			finished = true;
			return;
		}
		
		if (last > frameTime) {
			current = textures.removeFirst();
			if(looping)
				textures.add(current);
			last = 0;
		}
	}
	
	public float getLength() {
		return textures.size() * frameTime;
	}
	
	public boolean isFinished() {
		return finished;
	}
}

package gamedev.lwjgl.engine.textures;

import java.util.LinkedList;
import java.util.List;

public class AnimatedTexture {
	
	private LinkedList<TextureRegion> textures;
	private TextureRegion current;
	private float frameTime;
	private float last;
	
	public AnimatedTexture(List<TextureRegion> texs, float frameTime){
		textures = new LinkedList<>();
		textures.addAll(texs);
		this.frameTime = frameTime;
		this.current = textures.getFirst();
		last = 0;
	}
	
	public TextureRegion getCurrent(){
		return current;
	}
	
	public void update(float delta){
		last += delta;
		if (last > frameTime){
			current = textures.removeFirst();
			textures.add(current);
			last = 0;
		}
	}
	
}

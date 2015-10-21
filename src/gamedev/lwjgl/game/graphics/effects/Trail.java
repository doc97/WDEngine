package gamedev.lwjgl.game.graphics.effects;

import java.util.ArrayList;
import java.util.Iterator;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class Trail {

	private ArrayList<TrailPart> parts = new ArrayList<TrailPart>();
	
	public void addTrailPart(ModelTexture partTex, float x, float y, float width, float height) {
		parts.add(new TrailPart(partTex, x, y, width, height));
	}
	
	public void clear() {
		parts.clear();
	}
	
	public void update() {
		for(Iterator<TrailPart> it = parts.iterator(); it.hasNext();) {
			TrailPart tp = it.next();

			tp.update();
			if(tp.getAlpha() == 0)
				it.remove();
		}
	}
	
	public void render(SpriteBatch batch) {
		for(TrailPart tp : parts)
			tp.render(batch);
	}
	
	private class TrailPart {
		
		private ModelTexture texture;
		private float alpha;
		private float x, y;
		private float width, height; 
		
		public TrailPart(ModelTexture texture, float x, float y, float width, float height) {
			this.texture = texture;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			alpha = 1.0f;
		}
		
		public void update() {
			alpha -= 0.34f;
		}
		
		public void render(SpriteBatch batch) {
			Color c = batch.getColor();
			c.a = alpha;
			batch.setColor(c);
			batch.draw(texture, x, y, width, height);
			batch.setColor(Color.WHITE);
		}
		
		public float getAlpha() {
			return alpha;
		}
	}
}

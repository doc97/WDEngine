package gamedev.lwjgl.game.graphics;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class SceneTexture implements SceneObject {
	
	private ModelTexture texture;
	private float x, y;
	private float width, height;
	
	@Override
	public void update() {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		if(texture != null)
			batch.draw(texture, x, y, width, height);
	}
	
	public void setTexture(ModelTexture texture) {
		this.texture = texture;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDimension(float width, float height) {
		this.width = width;
		this.height = height;
	}
}

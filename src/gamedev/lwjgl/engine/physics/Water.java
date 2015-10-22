package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;

public class Water {
	
	public static Vector2f liftForce = new Vector2f(0, 1.1f);

	private Spring[] springs;
	private float tension = 0.025f;
	private float dampening = 0.05f;
	private float spread = 0.1f;
	private float optimalSpringDist = 10;
	private float x;
	private float width;
	
	public Water(float x, float y, float width, float height) {
		int amount = (int) Math.ceil(width / optimalSpringDist);
		springs = new Spring[amount];
		float springDist = (width / (springs.length - 1.0f));
		for(int i = 0; i < springs.length; i++) {
			springs[i] = new Spring(x + i * springDist, y, y + height);
		}
		this.x = x;
		this.width = springDist * springs.length;
	}
	
	public void update() {
		for(int i = 0; i < springs.length; i++)
			springs[i].update(tension, dampening);
		
		float[] leftDeltas = new float[springs.length];
		float[] rightDeltas = new float[springs.length];
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < springs.length; j++) {
				if(j > 0) {
					leftDeltas[j] = spread * (springs[j].getHeight() - springs[j - 1].getHeight());
					springs[j - 1].addSpeed(leftDeltas[j]);
				}
				if(j < springs.length - 1) {
					rightDeltas[j] = spread * (springs[j].getHeight() - springs[j + 1].getHeight());
					springs[j + 1].addSpeed(rightDeltas[j]);
				}
			}
			
			for(int j = 0; j < springs.length; j++) {
				if(j > 0)
					springs[j - 1].addHeight(leftDeltas[j]);
				if(j < springs.length - 1)
					springs[j + 1].addHeight(rightDeltas[j]);
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for(int i = 0; i < springs.length - 1; i++) {
			Spring s = springs[i];
			Spring s2 = springs[i + 1];
			
			Color dark = new Color(0.5f, 0.5f, 0.5f, 0.5f);
			Color light = new Color(1, 0, 1, 0.5f);
			Color[] colors = { dark, dark, light, light};

			batch.draw(null,
					s.getX(), s.getHeight(),
					s2.getX(), s2.getHeight(),
					s2.getX(), s2.getY(),
					s.getX(), s.getY(),
					null, colors, 0, 0, 0);
		}
	}
	
	public void splash(int index, float speed) {
		if(index >= 0 && index < springs.length)
			springs[index].setSpeed(speed);
	}
	
	public Spring getSpring(float x) {
		int index = (int) ((x - this.x) / (width / springs.length));
		if(index >= 0 && index < springs.length)
			return springs[index];
		else
			return null;
	}
	
	public Spring[] getSprings() {
		return springs;
	}
}

package gamedev.lwjgl.game.ui;

import gamedev.lwjgl.engine.font.Font;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;


public class Timer {
	
	private float timeLeft;
	private Font font;
	
	public Timer(float x, float y, float time){
		font = AssetManager.getFont("basic");
		timeLeft = time;
	}
	
	public boolean update(float delta){
		if (timeLeft > 0)
			timeLeft -= (delta / 60);
		if (timeLeft <= 0){
			return false;
		} else {
			return true;
		}
	}
	
	public void render(SpriteBatch batch){
		int timeleft = (int)timeLeft;
		font.drawString(batch, Integer.toString(timeleft), font.getOriginalSize(), batch.getCamera().getWidth() / 2 - 100, -batch.getCamera().getHeight() / 2);
	}
	
}

package gamedev.lwjgl.game.graphics.effects.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.graphics.effects.Pool;

public class ParticleSystem {
	
	public static final int POOL_SIZE = 100;
	private Pool<PlayerParticle> playerParticlePool;
	private List<PlayerParticle> playerParticles = new ArrayList<PlayerParticle>();
	
	public ParticleSystem() {
		playerParticlePool = new Pool<PlayerParticle>(POOL_SIZE) {
			@Override
			protected PlayerParticle newObject() {
				return new PlayerParticle();
			}
		};
	}
	
	public void createPlayerParticle(float x, float y, float dx, float dy, float size, float speed) {
		PlayerParticle newParticle = playerParticlePool.obtain();
		if(newParticle == null) return;
		
		newParticle.init(x, y, dx, dy, size, speed);
		playerParticles.add(newParticle);
	}
	
	public void update() {
		for(Iterator<PlayerParticle> it = playerParticles.iterator(); it.hasNext();) {
			PlayerParticle p = it.next();
			if(p.update()) {
				playerParticlePool.free(p);
				it.remove();
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for(PlayerParticle p : playerParticles) {
			p.render(batch);
		}
	}
}

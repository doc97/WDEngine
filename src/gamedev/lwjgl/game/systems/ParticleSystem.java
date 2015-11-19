package gamedev.lwjgl.game.systems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.graphics.effects.Pool;
import gamedev.lwjgl.game.graphics.effects.particles.MainMenuParticle;
import gamedev.lwjgl.game.graphics.effects.particles.Particle;
import gamedev.lwjgl.game.graphics.effects.particles.PlayerParticle;

public class ParticleSystem {
	
	public static final int POOL_SIZE = 100;
	private Pool<PlayerParticle> playerParticlePool;
	private Pool<MainMenuParticle> mainMenuParticlePool;
	private List<Particle> particles = new ArrayList<Particle>();
	
	public ParticleSystem() {
		playerParticlePool = new Pool<PlayerParticle>(POOL_SIZE) {
			@Override
			protected PlayerParticle newObject() {
				return new PlayerParticle();
			}
		};
		
		mainMenuParticlePool = new Pool<MainMenuParticle>(20) {
			@Override
			protected MainMenuParticle newObject() {
				return new MainMenuParticle();
			}
		};
	}
	
	public void createPlayerParticle(float x, float y, float dx, float dy, float size, float speed) {
		PlayerParticle newParticle = playerParticlePool.obtain();
		if(newParticle == null) return;
		
		newParticle.init(x, y, dx, dy, size, speed);
		particles.add(newParticle);
	}
	
	public void createMainMenuParticle(float x, float y, float dx, float dy, float size, float speed) {
		MainMenuParticle newParticle = mainMenuParticlePool.obtain();
		if(newParticle == null) return;
		
		newParticle.init(x, y, dx, dy, size, speed);
		particles.add(newParticle);
	}
	
	public void clear() {
		for(Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle p = it.next();
			free(p);
			it.remove();
		}
	}
	
	public void update() {
		for(Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle p = it.next();
			if(p.update()) {
				free(p);
				it.remove();
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for(Particle p : particles) {
			p.render(batch);
		}
	}
	
	private void free(Particle p) {
		if(p instanceof PlayerParticle)
			playerParticlePool.free((PlayerParticle) p);
		else if(p instanceof MainMenuParticle)
			mainMenuParticlePool.free((MainMenuParticle) p);
	}
}

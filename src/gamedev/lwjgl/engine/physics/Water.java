package gamedev.lwjgl.engine.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.particle.ParticleGroupDef;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.Color;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.Game;

public class Water {
	
	private ParticleGroupDef pgd;
	
	public Water(float x, float y, float width, float height) {
		pgd = new ParticleGroupDef();
		pgd.position.x = x;
		pgd.position.y = y;
		
		PolygonShape ps = new PolygonShape();
		ps.setAsBox(width, height);
		
		pgd.shape = ps;
		pgd.strength = 10f;
		
	}
	
	public void render(SpriteBatch batch) {
		Vec2[] positions = Game.INSTANCE.physics.getWaterParticles(this);
		batch.setColor(0.2f,0.2f,1,0.2f);
		for (int i = 0; i < positions.length; i++) {
			batch.draw(AssetManager.getTexture("water_particle"), positions[i].x - 6, positions[i].y - 6, 18, 18);
		}
		batch.setColor(Color.WHITE);
	}
	
	public ParticleGroupDef getParticleGroupDef() {
		return pgd;
	}
	
}

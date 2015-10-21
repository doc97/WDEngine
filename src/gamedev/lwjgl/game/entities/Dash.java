package gamedev.lwjgl.game.entities;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.graphics.effects.Trail;

public class Dash {

	private Timer dashTimer = new Timer();
	private Trail dashTrail = new Trail();
	private Player player;
	
	public Dash(Player player) {
		this.player = player;
	}
	
	public void update() {
		dashTrail.update();
		dashTimer.update();
		
		if(dashTimer.getPercentage() == 1)
			deactivate();
		
		if(dashTimer.isActive()) {
			float innerRadius = player.getCollisionShape().getInner().getRadius();
			Vector2f pos = player.getCollisionShape().getInner().getPosition();
			dashTrail.addTrailPart(player.getTexture(0),
					pos.x - innerRadius - player.speed.x / 2,
					pos.y - innerRadius - player.speed.y / 2,
					2 * innerRadius, 2 * innerRadius);
			dashTrail.addTrailPart(player.getTexture(0),
					pos.x - innerRadius,
					pos.y - innerRadius,
					2 * innerRadius, 2 * innerRadius);
			player.speed.set(Math.signum(player.speed.x) * 5 * player.maxSpeed, 0);
		}
	}
	
	public void render(SpriteBatch batch) {
		dashTrail.render(batch);
	}
	
	public void activate() {
		dashTimer.set(10);
		dashTimer.setActive(true);
	}
	
	public void deactivate() {
		dashTimer.setActive(false);
	}
	
	public boolean isActive() {
		return dashTimer.isActive();
	}
}

package gamedev.lwjgl.game.entities;

import org.jbox2d.common.Vec2;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.Timer;
import gamedev.lwjgl.game.Game;
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
			float innerRadius = player.getFixtureDef("base").getShape().getRadius() * Game.INSTANCE.physics.ppm;
			Vec2 pos = Game.INSTANCE.physics.currentEntityPosition(player);
			Vec2 speed = Game.INSTANCE.physics.currentEntitySpeed(player);
			dashTrail.addTrailPart(player.getTexture(0),
					pos.x - innerRadius - speed.x / 2,
					pos.y - innerRadius - speed.y / 2,
					2 * innerRadius, 2 * innerRadius);
			dashTrail.addTrailPart(player.getTexture(0),
					pos.x - innerRadius,
					pos.y - innerRadius,
					2 * innerRadius, 2 * innerRadius);
			if (speed.x < 0)
				Game.INSTANCE.physics.applyForceToMiddle(player, new Vec2(-50, 0));
			else
				Game.INSTANCE.physics.applyForceToMiddle(player, new Vec2(50, 0));
		}
	}
	
	public void render(SpriteBatch batch) {
		dashTrail.render(batch);
	}
	
	public void activate() {
		dashTimer.set(10);
		dashTimer.setActive(true);
		player.getBodyDef().setGravityScale(0);
		Game.INSTANCE.physics.setEntitySpeed(player, new Vec2(0, 0), false, true);
	}
	
	public void deactivate() {
		dashTimer.setActive(false);
		player.getBodyDef().setGravityScale(1);
	}
	
	public boolean isActive() {
		return dashTimer.isActive();
	}
}

package gamedev.lwjgl.game;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.Map;

public class GamePhysics {
	
	private float friction = 0.75f;
	private float airResistance = 0.97f;
	private final Vector2f gravitation = new Vector2f(0, -1.0f);
	
	public void update() {
		collisionDetection(Game.INSTANCE.container.getPlayer(), Game.INSTANCE.container.getMap());
		updatePositions();
		updateForces();
	}
	
	public void updateForces() {
		for(int i = 0; i < Game.INSTANCE.entities.getEntities().size(); i++) {
			Entity e = Game.INSTANCE.entities.getEntities().get(i);
			if(e.isDynamic()) {
				// Applying air resistance
				e.getSpeed().mul(airResistance);
			
				// Applying gravitation
				e.getSpeed().add(gravitation);
			}
		}
	}
	
	public void updatePositions() {
		for(int i = 0; i < Game.INSTANCE.entities.getEntities().size(); i++) {
			Entity e = Game.INSTANCE.entities.getEntities().get(i);
			if(e.isDynamic())
				e.addEntityPosition(e.getSpeed().x, e.getSpeed().y);
		}
	}

	private Vector2f calculateCollisionSpeed(Line line, Vector2f pos, Vector2f speed, float radius) {
		Vector2f end = new Vector2f(pos.x + speed.x, pos.y + speed.y);
		Vector2f pt = new Vector2f();
		Vector2f projUnit = new Vector2f();
		Vector2f projVec = new Vector2f();
		Vector2f closest = new Vector2f();
		Vector2f distance = new Vector2f();

		// Calculate closest point on line
		if(line.vector.lengthSquared() == 0) return null;
		Vector2f.sub(end, line.a, pt);
		line.vector.normalize(projUnit);
		
		float dot = pt.dot(projUnit);
		if(dot <= 0) {
			closest = line.a;
		} else if(dot * dot >= line.vector.lengthSquared()) {
			closest = line.b;
		} else {
			projVec.set(projUnit.x * dot, projUnit.y * dot);
			Vector2f.add(projVec, line.a, closest);
		}

		// Calculate offset
		Vector2f.sub(end, closest, distance);
		
		if(distance.length() <= radius) {
			float speedDot = speed.dot(projUnit);
			Vector2f speedProj = new Vector2f(projUnit.x * speedDot, projUnit.y * speedDot);
			return speedProj;
		}
		return null;
	}
	public void collisionDetection(Player player, Map map) {
		Circle circle = player.getCollisionShape();
		float radius = circle.getRadius();

		for(Line line : map.getCollisionMap()) {
			Vector2f speed = player.getSpeed();
			Vector2f newSpeed = calculateCollisionSpeed(line, circle.getPosition(), speed, radius);
			if(newSpeed != null) {
				if(newSpeed.lengthSquared() == 0) {
					player.setSpeed(0, 0);
					continue;
				}
				// Applying friction
				Vector2f frictionSpeed = new Vector2f();
				newSpeed.normalize(frictionSpeed);
				frictionSpeed.mul(-friction);
				player.setSpeed(newSpeed.x, newSpeed.y);
				
				if(player.getSpeed().lengthSquared() < frictionSpeed.lengthSquared())
					player.setSpeed(0, 0);
				else
					player.addSpeed(frictionSpeed.x, frictionSpeed.y);
			}
		}
	}
}

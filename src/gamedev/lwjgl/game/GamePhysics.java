package gamedev.lwjgl.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Player;

public class GamePhysics {
	
	private List<Line> lines = new ArrayList<Line>();
	
	private float friction = 0.5f;
	private float airResistance = 0.9f;
	private final Vector2f gravitation = new Vector2f(0, -1.0f);
	
	public GamePhysics() {
	}
	
	public void update(Player player) {
		collisionDetection(player);
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
		Vector2f proj = new Vector2f();
		Vector2f projVec = new Vector2f();
		Vector2f closest = new Vector2f();
		Vector2f distance = new Vector2f();

		if(line.vector.lengthSquared() == 0) return null;
		line.vector.normalize(projVec);
		
		float dot = end.dot(projVec);
		if(dot <= 0)
			proj = line.a;
		else if(dot * dot >= line.vector.lengthSquared())
			proj = line.b;
		else
			proj.set(projVec.x * dot, projVec.y * dot);
		
		Vector2f.add(proj, line.a, closest);
		Vector2f.sub(end, closest, distance);
		
		float distLength = distance.length();
		if(distLength <= radius) {
			float speedDot = speed.dot(projVec);
			Vector2f speedProj = new Vector2f(projVec.x * speedDot, projVec.y * speedDot);
			return speedProj;
		}
		return null;
	}
	public void collisionDetection(Player player) {
		Circle circle = player.getCollisionShape();
		float radius = circle.getRadius();
		
		Vector2f a = new Vector2f(0, 0);
		Vector2f b = new Vector2f(1000, 500);
		Line line = new Line(a, b);
		Vector2f speed = player.getSpeed();
		Vector2f newSpeed = calculateCollisionSpeed(line, circle.getPosition(), speed, radius);
		if(newSpeed != null) {
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

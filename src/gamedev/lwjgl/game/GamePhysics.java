package gamedev.lwjgl.game;

import java.util.ArrayList;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.Collision;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Spring;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.Map;
import gamedev.lwjgl.game.ui.Inventory;

public class GamePhysics {
	
	private float friction;
	private float airResistance;
	private float waterResistance;
	private Vector2f gravitation;

	
	public GamePhysics() {
		java.util.Map<String, String> data = AssetManager.getData("physics");
		friction = Float.parseFloat(data.get("friction"));
		airResistance = Float.parseFloat(data.get("airResistance"));
		waterResistance = Float.parseFloat(data.get("waterResistance"));
		gravitation = new Vector2f(0, -Float.parseFloat(data.get("gravitation")));
	}
	
	public void update() {
		for (Entity e : Game.INSTANCE.entities.getEntities()){
			if (e.isDynamic()){
				collisionDetection(e, Game.INSTANCE.container.getMap());
			}
		}
		Player pl = Game.INSTANCE.container.getPlayer();
		ArrayList<Item> toRemove = new ArrayList<Item>();
		
		for (Entity e : Game.INSTANCE.entities.getEntities()){
			if (e instanceof Item){
				if (isColliding(e, pl)){
					toRemove.add((Item) e);
					Game.INSTANCE.sounds.playSound(AssetManager.getSound("testSound"));
				}
			}
		}
		
		Inventory inv = pl.getInventory();
		for (Item i : toRemove){
			Game.INSTANCE.entities.removeEntity(i);
			inv.addItem(i);
		}
		
		
		updatePositions();
		updateForces();
	}
	
	public void updateForces() {
		for(int i = 0; i < Game.INSTANCE.entities.getEntities().size(); i++) {
			Entity e = Game.INSTANCE.entities.getEntities().get(i);
			if(e.isDynamic()) {
				// Applying gravitation
				e.getSpeed().add(gravitation);
				
				// Add water lift
				if(e.getWaterLift().lengthSquared() > 0) {
					e.getSpeed().add(e.getWaterLift());
					e.getSpeed().mul(waterResistance);
				} else {
					// Applying air resistance
					e.getSpeed().mul(airResistance);
				}
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
		Collision collision = calculateCollision(line, pos, speed, radius);
		
		if(collision != null) {
			Vector2f projUnit = collision.getProjectionUnit();
			float speedDot = speed.dot(projUnit);
			Vector2f speedProj = new Vector2f(projUnit.x * speedDot, projUnit.y * speedDot);
			return speedProj;
		}
		return null;
	}
	
	public Collision calculateCollision(Line line, Vector2f pos, Vector2f speed, float radius) {
		Vector2f end = new Vector2f(pos.x + speed.x, pos.y + speed.y);
		Vector2f pt = new Vector2f();
		Vector2f projUnit = new Vector2f();
		Vector2f projVec = new Vector2f();
		Vector2f closest = new Vector2f();
		Vector2f distance = new Vector2f();
		Collision collision = new Collision();
		
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
			collision.setContactPoint(closest);
			collision.setDistance(distance);
			collision.setProjectionUnit(projUnit);
			return collision;
		}
		return null;
	}
	
	public void calculateWaves(Entity entity, Water water) {
		Circle circle = entity.getCollisionShape();
		float radius = circle.getRadius();
		
		for(int j = 0; j < water.getSprings().length - 1; j++) {
			Spring s = water.getSprings()[j];
			Spring s2 = water.getSprings()[j + 1];
			Line line = new Line(new Vector2f(s.getX(), s.getHeight()), new Vector2f(s2.getX(), s2.getHeight()));
			Collision repulsion = calculateCollision(line, circle.getPosition(), entity.getSpeed(), radius);
			if(repulsion != null) {
				Vector2f x = new Vector2f(1, 0);
				Vector2f y = new Vector2f(0, 1);
				float xDot = entity.getSpeed().dot(x);
				float yDot = entity.getSpeed().dot(y);
				
				Spring frontSpring = null;
				Spring backSpring = null;
				
				if(xDot != 0) {
					frontSpring = water.getSpring(entity.getX() + entity.getCollisionShape().getRadius() * Math.signum(xDot));
					backSpring = water.getSpring(entity.getX() - entity.getCollisionShape().getRadius() * Math.signum(xDot));
				}

				float yFactor = 0;
				float xFactor = Math.abs(xDot);
				if(yDot < 0) {
					yFactor = y.y * yDot / 2.0f;
					xFactor /= 2.0f;
				} else if(yDot > 0) {
					yFactor = y.y * yDot / 4.0f;
					xFactor /= 4.0f;
				}
				
				if(frontSpring != null) {
					frontSpring.setSpeed(-(yFactor - xFactor));
				}
				if(backSpring != null) {
					backSpring.setSpeed(yFactor - xFactor);
				}
				
				if(backSpring == null && frontSpring == null) {
					s.setSpeed(-(yFactor + xFactor));
				}
			}
		}
	}
	
	public void collisionDetection(Entity entity, Map map) {
		Circle circle = entity.getCollisionShape();

		float radius = circle.getRadius();

		for(Line line : map.getCollisionMap()) {
			Vector2f speed = entity.getSpeed();
			Vector2f newSpeed = calculateCollisionSpeed(line, circle.getPosition(), speed, radius);
			if(newSpeed != null) {
				if(newSpeed.lengthSquared() == 0) {
					entity.setSpeed(0, 0);
					continue;
				}
				// Applying friction
				Vector2f frictionSpeed = new Vector2f();
				newSpeed.normalize(frictionSpeed);
				frictionSpeed.mul(-friction);
				entity.setSpeed(newSpeed.x, newSpeed.y);
				
				if(entity.getSpeed().lengthSquared() < frictionSpeed.lengthSquared())
					entity.setSpeed(0, 0);
				else
					entity.addSpeed(frictionSpeed.x, frictionSpeed.y);
			}
		}
		
		for(int i = 0; i < map.getWaters().size(); i++) {
			Water w = map.getWaters().get(i);
			Spring s = w.getSpring(entity.getX());
			
			calculateWaves(entity, w);

			if(s != null && entity.getY() >= s.getY() && entity.getY() <= s.getTargetHeight()) {
				float limit = s.getTargetHeight() + entity.getCollisionShape().getRadius() / 2;
				entity.setWaterLift(0.999f + 2 * (limit - entity.getY()) / limit);
			} else {
				if (s != null) {
					if (entity.getY() - entity.getCollisionShape().getRadius() >= s.getHeight()) {
						entity.isInWater(false);
					} else {
						entity.isInWater(true);
					}
				}
				entity.setWaterLift(0);
			}
		}
	}
	
	public boolean isColliding(Entity ent1, Entity ent2){
		Circle circ1 = ent1.getCollisionShape();
		Circle circ2 = ent2.getCollisionShape();
		
		Vector2f pos1 = circ1.getPosition() , pos2 = circ2.getPosition();
		
		float distance = pos1.distance(pos2);
		
		if (distance * distance < circ1.getRadius() * circ1.getRadius() + circ2.getRadius() * circ2.getRadius()){
			return true;
		}
		
		return false;
	}
	
}

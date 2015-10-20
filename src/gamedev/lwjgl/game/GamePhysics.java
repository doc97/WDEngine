package gamedev.lwjgl.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import gamedev.lwjgl.engine.physics.Circle;
import gamedev.lwjgl.engine.physics.Collision;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Spring;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.entities.ItemType;
import gamedev.lwjgl.game.entities.Player;
import gamedev.lwjgl.game.map.DynamicMapObject;
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
		Map m = Game.INSTANCE.container.getMap();
		for (DynamicMapObject dmo : m.getDynamicObjects()){
			collisionDetection(dmo, m);
		}
		
		for (Entity e : Game.INSTANCE.entities.getEntities()){
			if (e.isDynamic()){
				collisionDetection(e, m);
			}
		}
		
		Player pl = Game.INSTANCE.container.getPlayer();
		ArrayList<Item> toRemove = new ArrayList<Item>();
		
		for (Entity e : Game.INSTANCE.entities.getEntities()){
			if (e instanceof Item){
				if (isColliding(e, pl)){
					Game.INSTANCE.sounds.playSound(AssetManager.getSound("testSound"));
					toRemove.add((Item) e);
				}
			}
		}
		
		Inventory inv = pl.getInventory();
		for (Item i : toRemove){
			Game.INSTANCE.entities.removeEntity(i);
			switch(i.getType()) {
			case ENERGY :
				pl.getResources().addEnergy(30);
				break;
			case COIN :
				inv.addItem(i);
				break;
			}
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
	
	public Collision claculateCollison(Line l1, Line l2, Vector2f speed) {
		
		return null;
	}
	
	public void calculateWaves(Entity entity, Water water) {
		Circle circle = entity.getCollisionShape().getInner();
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
					frontSpring = water.getSpring(entity.getX() + entity.getCollisionShape().getInner().getRadius() * Math.signum(xDot));
					backSpring = water.getSpring(entity.getX() - entity.getCollisionShape().getInner().getRadius() * Math.signum(xDot));
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
	
	public void collisionDetection(DynamicMapObject dmo, Map map){
		ArrayList<Line> lines = dmo.getLines();
		for (Line line : map.getCollisionMap()){
			Vector2f speed = dmo.getSpeed();
			for (Line l : lines){
				Collision coll = claculateCollison(line, l, speed);
				
			}
		}
	}
	
	public void collisionDetection(Entity entity, Map map) {
		Circle circle = entity.getCollisionShape().getOuter();
		Circle circle2 = entity.getCollisionShape().getInner();
		float radius = circle.getRadius();
		
		entity.setOnGround(false);
		for(Line line : map.getCollisionMap()) {
			Vector2f speed = entity.getSpeed();
			Collision collision = calculateCollision(line, circle.getPosition(), speed, radius);
			
			if(collision != null) {
				Vector2f projUnit = collision.getProjectionUnit();
				float speedDot = speed.dot(projUnit);
				Vector2f speedProj = new Vector2f(projUnit.x * speedDot, projUnit.y * speedDot);
				
				Vector2f normal = new Vector2f();
				Vector2f.sub(collision.getContactPoint(), circle.getPosition(), normal);
				float normalAngle = (float) Math.atan2(normal.y, normal.x);
				float margin = (float) (Math.PI / 10);
				if((normalAngle > -Math.PI + margin && normalAngle < -margin) ||
						(normalAngle > Math.PI + margin && normalAngle < 2 * Math.PI - margin)) {
					entity.setOnGround(true);
				}
				
				if(speedProj.lengthSquared() == 0) {
					entity.setSpeed(0, 0);
					continue;
				}
				
				// Applying friction
				Vector2f frictionSpeed = new Vector2f();
				speedProj.normalize(frictionSpeed);
				frictionSpeed.mul(-friction);
				entity.setSpeed(speedProj.x, speedProj.y);
				
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

			if(s != null && circle2.getPosition().y >= s.getY() && circle2.getPosition().y <= s.getTargetHeight()) {
				float limit = s.getTargetHeight() + circle2.getRadius() / 2;
				entity.setWaterLift(0.999f + 2 * (limit - circle2.getPosition().y) / limit);
			} else {
				if (s != null) {
					if (circle2.getPosition().y - circle2.getRadius() >= s.getHeight()) {
						entity.setInWater(false);
					} else {
						entity.setInWater(true);
					}
				}
				entity.setWaterLift(0);
			}
		}
	}
	
	public boolean isColliding(Entity ent1, Entity ent2){
		Circle circ1 = ent1.getCollisionShape().getInner();
		Circle circ2 = ent2.getCollisionShape().getInner();
		
		Vector2f pos1 = circ1.getPosition() , pos2 = circ2.getPosition();
		
		float distanceSqrd = (pos1.x - pos2.x) * (pos1.x - pos2.x) + (pos1.y - pos2.y) * (pos1.y - pos2.y);
		
		float radSumSqrd = (circ1.getRadius() + circ2.getRadius()) * (circ1.getRadius() + circ2.getRadius());
		
		if (distanceSqrd < radSumSqrd){
			return true;
		}
		
		return false;
	}
	
}

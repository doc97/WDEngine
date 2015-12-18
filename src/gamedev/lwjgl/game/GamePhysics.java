package gamedev.lwjgl.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.particle.ParticleBodyContact;
import org.jbox2d.particle.ParticleGroup;
import org.jbox2d.particle.ParticleGroupDef;

import gamedev.lwjgl.engine.data.PhysicsData;
import gamedev.lwjgl.engine.physics.Line;
import gamedev.lwjgl.engine.physics.Water;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.entities.Entity;
import gamedev.lwjgl.game.entities.Item;
import gamedev.lwjgl.game.entities.ItemType;
import gamedev.lwjgl.game.map.Map;

public class GamePhysics implements ContactListener{
	
	private World world;
	private Body solidGround;
	private Body semiSolidGround;
	private HashMap<Entity, Body> bodies;
	private HashMap<Water, ParticleGroup> waters;
	private List<Body> bodiesToDestroy;
	public final float ppm = 32;
	
	public GamePhysics() {
		loadDatafiles();
	}
	
	public void loadDatafiles() {
		PhysicsData data = AssetManager.getPhysicsData();
		world = new World(new Vec2(data.gravitation.x, data.gravitation.y));
		bodies = new HashMap<Entity, Body>();
		waters = new HashMap<Water, ParticleGroup>();
		bodiesToDestroy = new ArrayList<Body>();
		world.setWarmStarting(true);
	}
	
	public void update() {
		world.step(0.04f, 1, 1);
		ParticleBodyContact[] contacts = world.getParticleBodyContacts();
		ArrayList<Entity> entities = new ArrayList<Entity>();
		entities.addAll(bodies.keySet());
		int[] contactAmount = new int[entities.size()];
		if(contacts != null) {
			for (int i = 0; i < contacts.length; i++) {
				Entity e = getEntity(contacts[i].body);
				if (e != null) {
					contactAmount[entities.indexOf(e)]++;
				}
			}
		}

		for (Entity e : entities) {
			if (contactAmount[entities.indexOf(e)] > 2) {
				e.setInWater(true);
			} else {
				e.setInWater(false);
			}
		}
		
		for (Body b : bodiesToDestroy) {
			world.destroyBody(b);
		}
		bodiesToDestroy.clear();
	}
	
	public void setMap(Map map) {
		
		// Solid ground
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position.set(0, 0);
		
		Body solidGround = world.createBody(bd);
		EdgeShape es = new EdgeShape();
		FixtureDef fd = new FixtureDef();
		fd.shape = es;
		fd.density = 0;
		fd.friction = 0.3f;
		
		for (Line line : map.getSolidLines()) {
			es.set(new Vec2(line.a.x / ppm, line.a.y / ppm), new Vec2(line.b.x / ppm, line.b.y / ppm));
			solidGround.createFixture(fd);
		}
		
		this.solidGround = solidGround;
		
		
		// Jump-through ground
		BodyDef bd2 = new BodyDef();
		bd2.type = BodyType.STATIC;
		bd2.position.set(0, 0);
		
		Body semiSolidGround = world.createBody(bd2);
		EdgeShape es2 = new EdgeShape();
		FixtureDef fd2 = new FixtureDef();
		fd2.shape = es2;
		fd2.density = 0;
		fd2.friction = 0.3f;
		
		for(Line line : map.getSemiSolidLines()) {
			es2.set(new Vec2(line.a.x / ppm, line.a.y / ppm), new Vec2(line.b.x / ppm, line.b.y / ppm));
			semiSolidGround.createFixture(fd2);
		}
		this.semiSolidGround = semiSolidGround;
		
		world.setParticleRadius(0.25f);
		world.setParticleDensity(0.2f);
		world.setParticleGravityScale(0.8f);
		
		for (Water water : map.getWaters()) {
			ParticleGroupDef pgd = new ParticleGroupDef();
			ParticleGroupDef waterDef = water.getParticleGroupDef();
			
			pgd.position.set(waterDef.position.x / 32, waterDef.position.y / 32);
			
			pgd.shape = waterDef.shape.clone();
			
			switch (pgd.shape.m_type) {
			case CHAIN:
				ChainShape chaShape = (ChainShape)pgd.shape;
				chaShape.m_radius /= ppm;
				for (int i = 0; i < chaShape.m_vertices.length; i++) {
					chaShape.m_vertices[i].x /= ppm;
					chaShape.m_vertices[i].y /= ppm;
				}
				break;
			case CIRCLE:
				CircleShape cirShape = (CircleShape)pgd.shape;
				cirShape.m_p.x /= ppm;
				cirShape.m_p.y /= ppm;
				cirShape.m_radius /= ppm;
				break;
			case EDGE:
				EdgeShape edgShape = (EdgeShape)pgd.shape;
				edgShape.m_radius /= ppm;
				break;
			case POLYGON:
				PolygonShape polShape = (PolygonShape)pgd.shape;
				for (int i = 0; i < polShape.m_normals.length; i++) {
					polShape.m_normals[i].x /= ppm;
					polShape.m_normals[i].y /= ppm;
				}
				for (int i = 0; i < polShape.m_vertices.length; i++) {
					polShape.m_vertices[i].x /= ppm;
					polShape.m_vertices[i].y /= ppm;
				}
				polShape.m_centroid.x /= ppm;
				polShape.m_centroid.y /= ppm;
				polShape.m_radius /= ppm;
				break;
			default:
				break;
			}
						
			ParticleGroup  pg = world.createParticleGroup(pgd);
			waters.put(water, pg);
			
		}
		
	}
	
	public void reset() {
		world.destroyBody(solidGround);
		world.destroyBody(semiSolidGround);
		for (Entity e : bodies.keySet()) {
			world.destroyBody(bodies.get(e));
		}
		for (Water w : waters.keySet()) {
			world.destroyParticlesInGroup(waters.get(w));
		}
		world.clearForces();
		waters.clear();
		bodies.clear();
	}
	
	public void addEntity(Entity entity) {
		BodyDef bd = entity.getBodyDef();
		bd.position.set(bd.position.x / ppm, bd.position.y / ppm);
		Body body = world.createBody(entity.getBodyDef());
		bodies.put(entity, body);
		world.setContactListener(this);
		for(String s : entity.getFixtureDefs().keySet()) {
			FixtureDef fd = entity.getFixtureDef(s);
			FixtureDef nfd = new FixtureDef();
			// Common variables
			nfd.userData = fd.userData;
			nfd.isSensor = fd.isSensor;
			switch (fd.shape.m_type) {
			case CHAIN:
				ChainShape chaShape = (ChainShape)fd.shape;
				ChainShape newShape = new ChainShape();
				nfd.shape = newShape;
				newShape.setRadius(chaShape.getRadius() / ppm);
				newShape.m_vertices = new Vec2[chaShape.m_vertices.length];
				for (int i = 0; i < chaShape.m_vertices.length; i++) {
					newShape.m_vertices[i].x = chaShape.m_vertices[i].x / ppm;
					newShape.m_vertices[i].y = chaShape.m_vertices[i].y / ppm;
				}
				break;
			case CIRCLE:
				CircleShape cirShape = (CircleShape)fd.shape;
				CircleShape cs = new CircleShape();
				nfd.shape = cs;
				cs.m_p.x = cirShape.m_p.x / ppm;
				cs.m_p.y = cirShape.m_p.y / ppm;
				cs.m_radius = cirShape.getRadius() / ppm;
				break;
			case EDGE:
				EdgeShape edgShape = (EdgeShape)fd.shape;
				EdgeShape nes = new EdgeShape();
				nfd.shape = nes;
				nes.m_radius = edgShape.m_radius / ppm;
				break;
			case POLYGON:
				PolygonShape polShape = (PolygonShape)fd.shape;
				PolygonShape nps = new PolygonShape();
				nfd.shape = nps;
				nps.set(polShape.m_vertices, polShape.m_count);
				for (int i = 0; i < nps.m_normals.length; i++) {
					nps.m_normals[i].x /= ppm;
					nps.m_normals[i].y /= ppm;
				}
				for (int i = 0; i < nps.m_vertices.length; i++) {
					nps.m_vertices[i].x /= ppm;
					nps.m_vertices[i].y /= ppm;
				}
				nps.m_centroid.x = polShape.m_centroid.x / ppm;
				nps.m_centroid.y = polShape.m_centroid.y / ppm;
				nps.m_radius = polShape.m_radius / ppm;
				break;
			default:
				break;
			}
			
			body.createFixture(nfd);
		}
	}
	
	public void removeEntity(Entity entity) {
		world.destroyBody(bodies.remove(entity));
	}
	
	public Vec2 currentEntityPosition(Entity entity) {
		Vec2 pos = bodies.get(entity).getWorldCenter();
		return new Vec2(pos.x * ppm, pos.y * ppm);
	}
	
	public Vec2 currentEntitySpeed(Entity entity) {
		Vec2 speed = bodies.get(entity).getLinearVelocity();
		return new Vec2(speed.x, speed.y);
	}
	
	public void applyForceToMiddle(Entity entity, Vec2 force) {
		bodies.get(entity).applyForceToCenter(force);
	}
	
	public void setEntityPosition(Entity entity, Vec2 pos) {
		pos.x /= ppm;
		pos.y /= ppm;
		bodies.get(entity).setTransform(pos, 0);
	}
	
	public void setEntitySpeed(Entity entity, Vec2 speed, boolean x, boolean y) {
		Body b = bodies.get(entity);
		if (x)
			b.setLinearVelocity(new Vec2(speed.x, b.m_linearVelocity.y));
		if (y)
			b.setLinearVelocity(new Vec2(b.m_linearVelocity.x, speed.y));
	}
	
	private Entity getEntity(Body body) {
		for (Entry<Entity, Body> entry : bodies.entrySet()) {
			if (body == entry.getValue()) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public Vec2[] getWaterParticles(Water water) {
		ParticleGroup pg = waters.get(water);
		Vec2[] positions = new Vec2[pg.getParticleCount()];
		for (int i = 0; i < positions.length; i++) {
			Vec2 pos = world.getParticlePositionBuffer()[pg.getBufferIndex() + i];
			positions[i] = new Vec2(pos.x * ppm, pos.y * ppm);
		}
		return positions;
	}
	
	@Override
	public void beginContact(Contact c) {
		Body b1 = c.m_fixtureA.getBody();
		Body b2 = c.m_fixtureB.getBody();
		if(beginCheckGround(c, b1, b2)) return;
		if(beginCheckItems(c, b1, b2)) return;
	}
	
	private boolean beginCheckGround(Contact c, Body b1, Body b2) {
		if (b1 == solidGround) {
			Entity e = getEntity(b2);
			if (e != null) {
				e.setOnGround(true);
				return true;
			}
		} else if (b2 == solidGround) {
			Entity e = getEntity(b1);
			if (e != null) {
				e.setOnGround(true);
				return true;
			}
		}
		return false;
	}
	
	private boolean beginCheckItems(Contact c, Body b1, Body b2) {
		Entity e1 = getEntity(b1);
		Entity e2 = getEntity(b2);
		if (e1 instanceof Item) {
			Item i = (Item) e1;
			if (e2 == Game.INSTANCE.container.getPlayer()) {
				if (i.getType() == ItemType.ENERGY) {
					c.setEnabled(false);
					Game.INSTANCE.resources.addEnergy(1);
					Game.INSTANCE.entities.removeEntity(e1);
					bodiesToDestroy.add(b1);
					return true;
				} else if(i.getType() == ItemType.ORB) {
					c.setEnabled(false);
					Game.INSTANCE.container.getPlayer().getInventory().addItem(ItemType.ORB);
					Game.INSTANCE.entities.removeEntity(e1);
					bodiesToDestroy.add(b1);
				}
			}
		} else if (e2 instanceof Item) {
			Item i = (Item) e2;
			if (e1 == Game.INSTANCE.container.getPlayer()) {
				if (i.getType() == ItemType.ENERGY) {
					c.setEnabled(false);
					Game.INSTANCE.resources.addEnergy(1);
					Game.INSTANCE.entities.removeEntity(e2);
					bodiesToDestroy.add(b2);
					return true;
				} else if(i.getType() == ItemType.ORB) {
					c.setEnabled(false);
					Game.INSTANCE.container.getPlayer().getInventory().addItem(ItemType.ORB);
					Game.INSTANCE.entities.removeEntity(e2);
					bodiesToDestroy.add(b2);
				}

			}
		}
		return false;
	}
	
	@Override
	public void endContact(Contact c) {
		Body b1 = c.m_fixtureA.getBody();
		Body b2 = c.m_fixtureB.getBody();
		if(endCheckGround(c, b1, b2)) return;
	}
	
	private boolean endCheckGround(Contact c, Body b1, Body b2) {
		if (b1 == solidGround) {
			Entity e = getEntity(b2);
			if (e != null) {
				e.setOnGround(false);
				return true;
			}
		} else if (b2 == solidGround) {
			Entity e = getEntity(b1);
			if (e != null) {
				e.setOnGround(false);
				return true;
			}
		} else if(b1 == semiSolidGround) {
			Entity e = getEntity(b1);
			if(e != null) {
				e.setOnGround(false);
				return true;
			}
		} else if(b2 == semiSolidGround) {
			Entity e = getEntity(b2);
			if(e != null) {
				e.setOnGround(false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void postSolve(Contact c, ContactImpulse i) {
		
	}

	@Override
	public void preSolve(Contact c, Manifold m) {
		Body b1 = c.m_fixtureA.getBody();
		Body b2 = c.m_fixtureB.getBody();

		if(b1 == semiSolidGround) {
			Entity e = getEntity(b2);
			if(e != null) {
				c.setEnabled(false);
				e.setOnGround(false);
				
				WorldManifold worldManifold = new WorldManifold();
				c.getWorldManifold(worldManifold);
				
				Vec2 vel1 = b1.getLinearVelocityFromWorldPoint(worldManifold.points[0]);
				Vec2 vel2 = b2.getLinearVelocityFromWorldPoint(worldManifold.points[0]);
				Vec2 impactVel = new Vec2(vel1.x - vel2.x, vel1.y - vel2.y);
				
				Vec2 center1 = worldManifold.points[0];
				Vec2 center2 = b2.getWorldCenter();
				float entY = center2.y - c.m_fixtureB.m_shape.m_radius * 4 / 5f;
				
				if(impactVel.y > 0 && entY > center1.y) {
					c.setEnabled(true);
					e.setOnGround(true);
				}
			}
		} else if(b2 == semiSolidGround) {
			Entity e = getEntity(b1);
			if(e != null) {
				c.setEnabled(false);
				e.setOnGround(false);

				WorldManifold worldManifold = new WorldManifold();
				c.getWorldManifold(worldManifold);
				
				Vec2 vel1 = b1.getLinearVelocityFromWorldPoint(worldManifold.points[0]);
				Vec2 vel2 = b2.getLinearVelocityFromWorldPoint(worldManifold.points[0]);
				Vec2 impactVel = new Vec2(vel2.x - vel1.x, vel2.y - vel1.y);

				Vec2 center1 = worldManifold.points[0];
				Vec2 center2 = b1.getWorldCenter();
				float entY = center2.y - c.m_fixtureA.m_shape.m_radius * 4 / 5f;

				
				if(impactVel.y > 0 && entY > center1.y) {
					c.setEnabled(true);
					e.setOnGround(true);
				}
			}
		}
	}
}

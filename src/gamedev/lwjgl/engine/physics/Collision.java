package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class Collision {
	private Vector2f contactPoint;
	private Vector2f projectionUnit;
	private Vector2f distance;
	
	public Vector2f getContactPoint() {
		return contactPoint;
	}
	public void setContactPoint(Vector2f contactPoint) {
		this.contactPoint = contactPoint;
	}
	public Vector2f getProjectionUnit() {
		return projectionUnit;
	}
	public void setProjectionUnit(Vector2f projectionUnit) {
		this.projectionUnit = projectionUnit;
	}
	public Vector2f getDistance() {
		return distance;
	}
	public void setDistance(Vector2f distance) {
		this.distance = distance;
	}
}

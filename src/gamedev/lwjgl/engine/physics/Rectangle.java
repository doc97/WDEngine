package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class Rectangle {
	private Vector2f[] points = new Vector2f[4];
	private Vector2f velocity = new Vector2f();

	public Rectangle(Vector2f[] points) {
		this.points = points;
	}
	
	public Rectangle(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3) {
		points[0] = p0;
		points[1] = p1;
		points[2] = p2;
		points[3] = p3;
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public void addPosition(float x, float y) {
		for(int i = 0; i < points.length; i++) {
			points[i].x += x;
			points[i].y += y;
		}
	}
	
	public void updatePositon(float delta) {
		Vector2f dvel = new Vector2f();
		velocity.mul(delta, dvel);
		for(int i = 0; i < points.length; i++)
			points[i].add(dvel);
	}
	
	public Vector2f[] getSides() {
		Vector2f[] sides = new Vector2f[4];
		for(int i = 0; i < sides.length; i++)
			sides[i] = new Vector2f();
		Vector2f.sub(points[0], points[1], sides[0]);
		Vector2f.sub(points[1], points[2], sides[1]);
		Vector2f.sub(points[2], points[3], sides[2]);
		Vector2f.sub(points[3], points[0], sides[3]);
		return sides;
	}
	
	public Vector2f[] getPoints() {
		return points;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
}

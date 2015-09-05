package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class PhysicsUtil {
	
	public static void main(String[] args) {
		Rectangle b1 = new Rectangle(
				new Vector2f(0, 0),
				new Vector2f(0, 4),
				new Vector2f(4, 4),
				new Vector2f(4, 0)
				);
		
		Rectangle b2 = new Rectangle(
				new Vector2f(2, 7),
				new Vector2f(5, 4),
				new Vector2f(8, 7),
				new Vector2f(5, 10)
				);
		
		boolean res = false;
		long times = 10;
		long startMs = System.nanoTime();
		for(int i = 0; i < times; i++)
			res = isCollidingSAT(b1, b2);
		long endMs = System.nanoTime();
		
		System.out.println("Average: " + (endMs - startMs) / (times * Math.pow(10, 3)) + " microsec");
		System.out.println(res);
	}
	
	public static boolean isCollidingSAT(Rectangle box0, Rectangle box1) {
		// Get box data
		Vector2f[] points0 = box0.getPoints();
		Vector2f[] points1 = box1.getPoints();
		Vector2f[] normals0 = box0.getSides();
		Vector2f[] normals1 = box1.getSides();
		
		if(!intersectAxis(points0, points1, normals0[0])) return false;
		if(!intersectAxis(points0, points1, normals0[1])) return false;
		if(!intersectAxis(points0, points1, normals1[0])) return false;
		if(!intersectAxis(points0, points1, normals1[1])) return false;
		
		return true;
	}
	
	private static boolean intersectAxis(Vector2f[] points0, Vector2f[] points1, Vector2f normal) {
		// Project every corner onto axis, 00 = first box's first corner, 02 = first box's 3rd corner etc
		float proj_dot_00 = getProjectionDot(points0[0], normal);
		float proj_dot_01 = getProjectionDot(points0[1], normal);
		float proj_dot_02 = getProjectionDot(points0[2], normal);
		float proj_dot_03 = getProjectionDot(points0[3], normal);
		float proj_dot_10 = getProjectionDot(points1[0], normal);
		float proj_dot_11 = getProjectionDot(points1[1], normal);
		float proj_dot_12 = getProjectionDot(points1[2], normal);
		float proj_dot_13 = getProjectionDot(points1[3], normal);
		
		float min_0 = Math.min(Math.min(proj_dot_00, proj_dot_01), Math.min(proj_dot_02, proj_dot_03));
		float max_0 = Math.max(Math.max(proj_dot_00, proj_dot_01), Math.max(proj_dot_02, proj_dot_03));
		float min_1 = Math.min(Math.min(proj_dot_10, proj_dot_11), Math.min(proj_dot_12, proj_dot_13));
		float max_1 = Math.max(Math.max(proj_dot_10, proj_dot_11), Math.max(proj_dot_12, proj_dot_13));
		
		return max_0 >= min_1 && min_0 <= max_1;
	}
	
	private static float getProjectionDot(Vector2f point, Vector2f normal) {
		float factor =
				(point.x * normal.x + point.y * normal.y) /
				(normal.x * normal.x + normal.y * normal.y);
		Vector2f proj_00 = new Vector2f(normal.x * factor, normal.y * factor);
		float dot = proj_00.x * normal.x + proj_00.y * normal.y;
		return dot;
	}
}

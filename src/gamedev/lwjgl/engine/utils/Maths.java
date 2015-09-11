package gamedev.lwjgl.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Maths {

	/**
	 * Creates a transformation matrix
	 * @param pos - The position vector
	 * @param rot - The rotation vector, angles in radians
	 * @param scale - The scale vector
	 * @return The final transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f pos, Vector3f rot, Vector3f scale) {
		Matrix4f mat = new Matrix4f();
		mat.identity();
		mat.translate(pos);
		mat.rotate(rot.x, new Vector3f(1, 0, 0));
		mat.rotate(rot.y, new Vector3f(0, 1, 0));
		mat.rotate(rot.z, new Vector3f(0, 0, 1));
		mat.scale(scale, mat);
		return mat;
	}
	
	/**
	 * Creates a projection matrix
	 * @param fov - Field of view in radians
	 * @param aspectRatio - width / height
	 * @param nearPlane
	 * @param farPlane
	 * @return The final projection matrix
	 */
	public static Matrix4f createProjectionMatrix(float fov, float aspectRatio, float nearPlane, float farPlane) {
		Matrix4f mat = new Matrix4f();
		float yscale = (float) ((1f / Math.tan(fov / 2f)) * aspectRatio);
		float xscale = yscale / aspectRatio;
		float frustumLength = farPlane - nearPlane;
		
		mat.m00 = xscale;
		mat.m11 = yscale;
		mat.m22 = -((farPlane + nearPlane) / frustumLength);
		mat.m23 = -1;
		mat.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		mat.m33 = 0;
		return mat;
	}
	
	public static Matrix4f createViewMatrix(Vector3f position, float verticalAngle, float horizontalAngle) {
		Matrix4f mat = new Matrix4f();
		mat.identity();
		mat.rotate(verticalAngle, new Vector3f(1, 0, 0));
		mat.rotate(horizontalAngle, new Vector3f(0, 1, 0));
		mat.translate(position.x, position.y, position.z);
		return mat;
	}
	
	public static void clampVector(Vector2f vec, float length) {
		if(vec.lengthSquared() > length * length)
		vec.normalize().mul(length);
	}
	
	public static float clamp(float min, float max, float value) {
		if(value <= min) return min;
		if(value >= max) return max;
		return value;
	}
	
	/**
	 * Returns the distance from a point to a line segment
	 * @param point - The point to which the distance is calculated
	 * @param coord1 - One end point of the line segment
	 * @param coord2 - Second end point of the line segment
	 * @return
	 */
	public static float distanceFromLineToPointSqrd(Vector2f point, Vector2f coord1, Vector2f coord2) {
		Vector2f line = new Vector2f(coord2.x - coord1.x, coord2.y - coord1.y);
		float dot = point.dot(line);
		float len_sq = line.lengthSquared();
		float param = -1;
		if (len_sq != 0) //in case of 0 length line
			param = dot / len_sq;

		Vector2f pos;

		if (param < 0) {
			pos = coord1;
		}
		else if (param > 1) {
			pos = coord2;
		}
		else {
			pos = new Vector2f(coord1.x + param * (coord2.x - coord1.x), coord1.y + param * (coord2.y - coord1.y));
		}

		float dx = point.x - pos.x;
		float dy = point.y - pos.y;
		return dx * dx + dy * dy;
	}
	
	/**
	 * Calculates if a given point is inside a rectangle
	 * @param point - The point that is checked
	 * @param v1 - first vertex of the rectangle
	 * @param v2 - second vertex of the rectangle
	 * @param v4 - the fourth vertex of the rectangle
	 * @return
	 */
	public static boolean pointInRectangle(Vector2f point, Vector2f v1, Vector2f v2, Vector2f v4) {
		Vector2f v1p = new Vector2f();
		Vector2f.sub(v1, point, v1p);
		
		Vector2f v1v2 = new Vector2f();
		Vector2f.sub(v1, v2, v1v2);
		
		Vector2f v1v4 = new Vector2f();
		Vector2f.sub(v1, v4, v1v4);
		
		float dotV1pV1v2 = v1p.dot(v1v2);		// AP dot AB
		float dotV1v2V1v2 = v1v2.dot(v1v2);		// AB dot AB
		float dotV1pV1v4 = v1p.dot(v1v4);		// AP dot AD
		float dotV1v4V1v4 = v1v4.dot(v1v4);		// AD dot AD
		
		// 0 <= AP dot AB <= AB dot AB and 0 <= AP dot AD <= AD dot AD
		return (dotV1pV1v2 >= 0 && dotV1pV1v2 <= dotV1v2V1v2) && (dotV1pV1v4 >= 0 && dotV1pV1v4 <= dotV1v4V1v4);
	}
	
	public static double getAngle(Vector2f line1, Vector2f line2) {
		float dot = line1.dot(line2);
		return Math.acos(dot);
	}
}
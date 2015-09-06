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
	
	public static Vector2f maxLength(Vector2f vec, float length) {
		if(vec.length() > length) {
			Vector2f dest = new Vector2f();
			dest.x = vec.x;
			dest.y = vec.y;
			dest.normalize();
			dest.mul(length);
			return dest;
		}
		return vec;
	}
}

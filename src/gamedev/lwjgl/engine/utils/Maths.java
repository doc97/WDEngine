package gamedev.lwjgl.engine.utils;

import org.joml.Matrix4f;
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
		mat.translate(pos, mat);
		mat.rotate(rot.x, new Vector3f(1, 0, 0), mat);
		mat.rotate(rot.y, new Vector3f(0, 1, 0), mat);
		mat.rotate(rot.z, new Vector3f(0, 0, 1), mat);
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
		mat.m32 = -((2 * farPlane * nearPlane) / frustumLength);
		mat.m33 = 0;
		return mat;
	}
}

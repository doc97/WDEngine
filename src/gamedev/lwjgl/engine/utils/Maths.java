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
		mat.translate(pos);
		mat.rotate(rot.x, new Vector3f(1, 0, 0));
		mat.rotate(rot.y, new Vector3f(0, 1, 0));
		mat.rotate(rot.z, new Vector3f(0, 0, 1));
		mat.scale(scale);
		return mat;
	}
}

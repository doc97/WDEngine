package gamedev.lwjgl.engine.utils;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Buffers {
	public static FloatBuffer fillMatrixBuffer(Matrix4f matrix) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		float[] arr = new float[16];
		matrix.get(arr, 0);
		
		buf.put(arr);
		buf.flip();
		return buf;
	}
}

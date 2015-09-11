package gamedev.lwjgl.engine.physics;

import org.joml.Vector2f;

public class Line {
	public Vector2f a, b;
	public Vector2f vector = new Vector2f();
	
	public Line(Vector2f a, Vector2f b) {
		this.a = a;
		this.b = b;
		Vector2f.sub(b, a, vector);
	}
}

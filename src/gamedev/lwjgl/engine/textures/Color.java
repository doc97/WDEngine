package gamedev.lwjgl.engine.textures;

public class Color {
	
	public static final Color WHITE = new Color(1, 1, 1, 1);
	public static final Color BLACK = new Color(0, 0, 0, 1);
	public static final Color TRANSPARENT = new Color(1, 1, 1, 0);
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color(float r, float g, float b, float a) {
		this.r = Math.min(Math.max(r, 0), 1);
		this.g = Math.min(Math.max(g, 0), 1);
		this.b = Math.min(Math.max(b, 0), 1);
		this.a = Math.min(Math.max(a, 0), 1);
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.r = Math.min(Math.max(r, 0), 1);
		this.g = Math.min(Math.max(g, 0), 1);
		this.b = Math.min(Math.max(b, 0), 1);
		this.a = Math.min(Math.max(a, 0), 1);
	}
	
	public void addColor(float r, float g, float b, float a) {
		this.r = Math.min(Math.max(this.r + r, 0), 1);
		this.g = Math.min(Math.max(this.g + g, 0), 1);
		this.b = Math.min(Math.max(this.b + b, 0), 1);
		this.a = Math.min(Math.max(this.a + a, 0), 1);
	}
}

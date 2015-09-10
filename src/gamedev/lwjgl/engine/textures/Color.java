package gamedev.lwjgl.engine.textures;

public enum Color {
	
	RED(1,0,0,1),
	GREEN(0,1,0,1),
	BLUE(0,0,1,1),
	BLACK(0,0,0,1),
	WHITE(1,1,1,1),
	GRAY(0.5f,0.5f,0.5f,1),
	CUSTOM(0,0,0,0);
	
	public float red;
	public float green;
	public float blue;
	public float alpha;
	
	Color(float r, float g, float b, float a){
		red = Math.min(Math.max(r, 0), 1);
		green = Math.min(Math.max(g, 0), 1);
		blue = Math.min(Math.max(b, 0), 1);
		alpha = Math.min(Math.max(a, 0), 1);
	}
	
	public Color setColor(float r, float g, float b, float a){
		red = Math.min(Math.max(r, 0), 1);
		green = Math.min(Math.max(g, 0), 1);
		blue = Math.min(Math.max(b, 0), 1);
		alpha = Math.min(Math.max(a, 0), 1);
		return this;
	}
}

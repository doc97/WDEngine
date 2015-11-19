package gamedev.lwjgl.engine.shaders;

public class SelectionShader extends Shader {

	private static final String SHADER_FILE = "assets/shaders/brightnesselectionshader.ss";
	
	private float threshold;
	public int location_threshold;
	
	public SelectionShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {
		
	}

	@Override
	protected void getAllUniformLocations() {
		location_threshold = getUniformLocation("threshold");
	}

	@Override
	public void load() {
		loadFloat(location_threshold, threshold);
	}
	
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
}

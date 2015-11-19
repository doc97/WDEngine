package gamedev.lwjgl.engine.shaders;

public class HBlurShader extends Shader {

	private static final String SHADER_FILE = "assets/shaders/hblurshader.ss";
	
	private float blurFactor;
	public int location_blurFactor;
	
	public HBlurShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {
		
	}

	@Override
	protected void getAllUniformLocations() {
		location_blurFactor = getUniformLocation("blurFactor");
	}
	
	@Override
	public void load() {
		loadFloat(location_blurFactor, blurFactor);
	}
	
	public void setBlurFactor(float factor) {
		blurFactor = factor;
	}

}

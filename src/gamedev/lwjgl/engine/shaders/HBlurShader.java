package gamedev.lwjgl.engine.shaders;

public class HBlurShader extends Shader {

	private static final String SHADER_FILE = "assets/shaders/hblurshader.ss";
	
	public HBlurShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {
		
	}

	@Override
	protected void getAllUniformLocations() {
		
	}
}

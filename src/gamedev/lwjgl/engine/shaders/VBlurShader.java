package gamedev.lwjgl.engine.shaders;

public class VBlurShader extends Shader {

	private static final String SHADER_FILE = "assets/shaders/vblurshader.ss";
	
	public VBlurShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {

	}

	@Override
	protected void getAllUniformLocations() {

	}
}

package gamedev.lwjgl.shaders;

public class StaticShader extends Shader {

	private static final String SHADER_FILE = "shaders/staticshader.ss";
	
	public StaticShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texcoords");
	}
}

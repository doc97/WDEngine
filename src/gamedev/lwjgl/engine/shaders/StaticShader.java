package gamedev.lwjgl.engine.shaders;

import org.joml.Matrix4f;

public class StaticShader extends Shader {

	private static final String SHADER_FILE = "shaders/staticshader.ss";
	
	private int location_mvp;
	
	public StaticShader() {
		super(SHADER_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texcoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvp = super.getUniformLocation("MVP");
	}
	
	public void loadMVP(Matrix4f mat) {
		super.loadMatrix(location_mvp, mat);
	}
}

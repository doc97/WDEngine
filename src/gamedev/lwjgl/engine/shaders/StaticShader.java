package gamedev.lwjgl.engine.shaders;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

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
		super.bindAttribute(2, "color");
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvp = super.getUniformLocation("MVP");
	}
	
	public void loadTexture(int textureUnit) {
		super.loadInt(GL_TEXTURE0 + textureUnit, textureUnit);
	}
	
	public void loadMVP(Matrix4f model, Matrix4f view, Matrix4f projection) {
		Matrix4f result = new Matrix4f();
		projection.mul(view.mul(model), result);
		super.loadMatrix(location_mvp, result);
	}
}

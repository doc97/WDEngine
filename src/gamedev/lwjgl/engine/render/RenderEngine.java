package gamedev.lwjgl.engine.render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GLContext;

import gamedev.lwjgl.engine.Entity;
import gamedev.lwjgl.engine.GlobalSystem;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;
import gamedev.lwjgl.engine.shaders.StaticShader;

public class RenderEngine {
	private GlobalSystem gSys;
	private DisplayManager displayManager;
	
	public RenderEngine(GlobalSystem gSys) {
		this.gSys = gSys;
		displayManager = new DisplayManager();
	}
	
	public boolean init(int width, int height) {
		if(displayManager.createDisplay(width, height)) {
			GLContext.createFromCurrent();
			glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
			return true;
		}
		return false;
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		for(Entity entity : gSys.getEntitySystem().getEntities()) {
			TexturedModel texModel = entity.getModel();
			RawModel rawModel = texModel.getRawModel();
			begin(rawModel);
			loadUniforms(entity);
			glDrawElements(GL_TRIANGLES, texModel.getRawModel().getIndexCount(), GL_UNSIGNED_INT, 0);
			unbindTextures();
			end(rawModel);
		}
		
		displayManager.updateDisplay();
	}
	
	private void begin(RawModel model) {
		model.getShader().start();
		glBindVertexArray(model.getVAO());
		enableVertexAttributes();
	}
	
	private void end(RawModel model) {
		disableVertexAttributes();
		glBindVertexArray(0);
		model.getShader().stop();
	}
	
	private void loadUniforms(Entity entity) {
		// Textures
		StaticShader shader = (StaticShader) entity.getModel().getRawModel().getShader();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());
		shader.loadTexture(0);
		
		// Model * View * Projection
		shader.loadMVP(entity.getTransformationMatrix(), gSys.getCamera().getViewMatrix(), gSys.getCamera().getProjectionMatrix());
	}
	
	private void unbindTextures() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private void enableVertexAttributes() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}
	
	private void disableVertexAttributes() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
	}
	
	public DisplayManager getDisplayManager() {
		return displayManager;
	}
	
	public void cleanup() {
		
	}
}
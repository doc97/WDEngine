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

import gamedev.lwjgl.engine.GlobalSystem;
import gamedev.lwjgl.engine.models.RawModel;
import gamedev.lwjgl.engine.models.TexturedModel;

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
		
		for(TexturedModel model : gSys.getModelSystem().getModels()) {
			RawModel rawModel = model.getRawModel();
			begin(rawModel);
			bindTextures(model);
			glDrawElements(GL_TRIANGLES, model.getRawModel().getIndexCount(), GL_UNSIGNED_INT, 0);
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
	
	private void bindTextures(TexturedModel model) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getTextureID());
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
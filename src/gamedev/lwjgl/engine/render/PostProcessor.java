package gamedev.lwjgl.engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;

import gamedev.lwjgl.engine.textures.ModelTexture;

public class PostProcessor {
	private int fbo;
	private ModelTexture texture;
	private int width;
	private int height;
	
	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Create frame buffer object
		fbo = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		
		// Create texture attachment
		int texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texID, 0);
		
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		    System.err.println("Framebuffer configuration error");
		
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		texture = new ModelTexture(texID, width, height);
		// Flip texture
		float[] uvs = { 0,  1, 1, 0 };
		texture.setUVS(uvs);
	}
	
	public void bindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	}
	
	public void unbindFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void bindTexture() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
	}
	
	public void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		
		// Delete old texture
		glDeleteTextures(texture.getTextureID());
		
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		
		// Create texture attachment
		int texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texID, 0);
		
		texture = new ModelTexture(texID, width, height);
		// Flip texture
		float[] uvs = { 0,  1, 1, 0 };
		texture.setUVS(uvs);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
	
	public int getFBO() {
		return fbo;
	}
}

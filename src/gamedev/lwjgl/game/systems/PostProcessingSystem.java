package gamedev.lwjgl.game.systems;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.Logger;
import gamedev.lwjgl.engine.render.PostProcessor;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class PostProcessingSystem {
	public PostProcessor screenCapture = new PostProcessor();
	private List<PostProcessor> usedProcessors = new ArrayList<PostProcessor>();
	private List<PostProcessor> unusedProcessors = new ArrayList<PostProcessor>();
	
	public PostProcessor newProcessor() {
		PostProcessor pp = unusedProcessors.get(0);
		if(pp == null) return null;
		usedProcessors.add(pp);
		unusedProcessors.remove(pp);
		return pp;
	}
	
	public void init() {
		for(int i = 0; i < 10; i++) {
			PostProcessor pp = new PostProcessor();
			pp.init(Engine.INSTANCE.display.getWindowWidth(), Engine.INSTANCE.display.getWindowHeight());
			unusedProcessors.add(pp);
		}
		screenCapture.init(Engine.INSTANCE.display.getWindowWidth(), Engine.INSTANCE.display.getWindowHeight());
	}
	
	public void freeProcessor(PostProcessor pp) {
		unusedProcessors.remove(pp);
		usedProcessors.add(pp);
	}
	
	public void reset() {
		for(Iterator<PostProcessor> it = usedProcessors.iterator(); it.hasNext();) {
			PostProcessor p = it.next();
			p.clear();
			if(p.getWidth() != Engine.INSTANCE.display.getWindowWidth() || p.getHeight() != Engine.INSTANCE.display.getWindowHeight()) {
				p.resize(Engine.INSTANCE.display.getWindowWidth(), Engine.INSTANCE.display.getWindowHeight());
			}
			unusedProcessors.add(p);
			it.remove();
		}
		
		screenCapture.clear();
		if(screenCapture.getWidth() != Engine.INSTANCE.display.getWindowWidth() || screenCapture.getHeight() != Engine.INSTANCE.display.getWindowHeight()) {
			screenCapture.resize(Engine.INSTANCE.display.getWindowWidth(), Engine.INSTANCE.display.getWindowHeight());
		}
	}
	
	public void bindProcessor(PostProcessor pp) {
		pp.bindFBO();
		pp.bindTexture();
	}
	
	public void bindDefault() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public ModelTexture downSample(ModelTexture tex, int downScale) {
		PostProcessor p = newProcessor();
		if(p == null) return null;
		p.resize(Engine.INSTANCE.display.getWindowWidth() / downScale, Engine.INSTANCE.display.getWindowHeight() / downScale);
		
		bindProcessor(p);
		Engine.INSTANCE.batch.setScale(1.0f / downScale);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		Engine.INSTANCE.batch.setScale(1.0f);
		return p.getTexture();
	}
	
	public ModelTexture horizontalBlur(ModelTexture tex, float blurFactor) {
		PostProcessor p = newProcessor();
		if(p == null) return null;
		bindProcessor(p);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		SpriteBatch.hBlurShader.setBlurFactor(blurFactor);
		Engine.INSTANCE.batch.setShader(SpriteBatch.hBlurShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		return p.getTexture();
	}
	
	public ModelTexture verticalBlur(ModelTexture tex, float blurFactor) {
		PostProcessor p = newProcessor();
		if(p == null) {
			Logger.message("PostProcessingSystem", "Cannot get new processor");
			return null;
		}
		bindProcessor(p);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		SpriteBatch.vBlurShader.setBlurFactor(blurFactor);
		Engine.INSTANCE.batch.setShader(SpriteBatch.vBlurShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		return p.getTexture();
	}
	
	public ModelTexture selectFromBrightness(ModelTexture texture, float threshold) {
		PostProcessor p = newProcessor();
		if(p == null) return null;
		bindProcessor(p);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		SpriteBatch.selectShader.setThreshold(threshold);
		Engine.INSTANCE.batch.setShader(SpriteBatch.selectShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(texture, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		return p.getTexture();
	}
	
	public ModelTexture additiveBlend(ModelTexture base, ModelTexture bloomTexture) {
		PostProcessor p = newProcessor();
		if(p == null) return null;
		bindProcessor(p);
		// Additive blending
		glBlendFunc(GL_ONE, GL_ONE);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(base, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.draw(bloomTexture, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();

		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		return p.getTexture();
	}
	
	public ModelTexture blur(ModelTexture initial, int downScale) {
		ModelTexture lowres = downSample(initial, downScale);
		ModelTexture hBlur = horizontalBlur(lowres, 0.5f);
		ModelTexture vBlur = verticalBlur(hBlur, 0.5f);
		return vBlur;
	}
	
	public void drawToScreen(ModelTexture tex) {
		bindDefault();
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
	}
}

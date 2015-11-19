package gamedev.lwjgl.game.systems;

import gamedev.lwjgl.engine.Engine;
import gamedev.lwjgl.engine.render.PostProcessor;
import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.textures.ModelTexture;

public class PostProcessingSystem {
	public PostProcessor screenCapture = new PostProcessor();
	private PostProcessor bloomProcessor = new PostProcessor();
	private PostProcessor hBlurProcessor = new PostProcessor();
	private PostProcessor vBlurProcessor = new PostProcessor();
	private PostProcessor lowResProcessor = new PostProcessor();
	
	public void init() {
		// TODO change to display size (pixels)
		screenCapture.init(1280, 720);
		bloomProcessor.init(1280, 720);
		lowResProcessor.init(1280 / 2, 720 / 2);
		hBlurProcessor.init(1280, 720);
		vBlurProcessor.init(1280, 720);
	}
	
	public ModelTexture downSample(ModelTexture tex, int downScale) {
		lowResProcessor.resize(1280 / downScale, 720 / downScale);
		lowResProcessor.bindFBO();
		lowResProcessor.bindTexture();
		Engine.INSTANCE.batch.setScale(1.0f / downScale);
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		lowResProcessor.unbindFBO();
		lowResProcessor.unbindTexture();
		Engine.INSTANCE.batch.setScale(1.0f);
		return lowResProcessor.getTexture();
	}
	
	public ModelTexture horizontalBlur(ModelTexture tex, float blurFactor) {
		hBlurProcessor.bindFBO();
		hBlurProcessor.bindTexture();
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		SpriteBatch.hBlurShader.setBlurFactor(blurFactor);
		Engine.INSTANCE.batch.setShader(SpriteBatch.hBlurShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		hBlurProcessor.unbindFBO();
		hBlurProcessor.unbindTexture();
		return hBlurProcessor.getTexture();
	}
	
	public ModelTexture verticalBlur(ModelTexture tex, float blurFactor) {
		vBlurProcessor.bindFBO();
		vBlurProcessor.bindTexture();
		
		Engine.INSTANCE.camera.setPosition(
				Engine.INSTANCE.camera.getWidth() / 2,
				Engine.INSTANCE.camera.getHeight() / 2
		);
		
		SpriteBatch.vBlurShader.setBlurFactor(blurFactor);
		Engine.INSTANCE.batch.setShader(SpriteBatch.vBlurShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
		
		vBlurProcessor.unbindFBO();
		vBlurProcessor.unbindTexture();
		return vBlurProcessor.getTexture();
	}
	
	public void drawToScreen(ModelTexture tex) {
		Engine.INSTANCE.batch.setShader(SpriteBatch.staticShader);
		Engine.INSTANCE.batch.begin();
		Engine.INSTANCE.batch.draw(tex, 0, 0, Engine.INSTANCE.camera.getWidth(), Engine.INSTANCE.camera.getHeight());
		Engine.INSTANCE.batch.end();
	}
}

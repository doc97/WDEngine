package gamedev.lwjgl.engine.input;

public interface InputListener {
	
	public void update();
	public boolean keyPressed(int key);
	public boolean keyRepeat(int key);
	public boolean keyReleased(int key);
	public boolean mousePressed(int button);
	public boolean mouseReleased(int button);
}

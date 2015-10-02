package gamedev.lwjgl.game.states;

public abstract class State {
	
	public abstract void enter();
	public abstract void update();
	public abstract void render();
	public abstract void exit();
	
}
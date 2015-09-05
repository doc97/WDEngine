package gamedev.lwjgl.game.states;

public abstract class State {
	
	public abstract void enter();
	public abstract void render(float dt);
	public abstract void exit();
	
}
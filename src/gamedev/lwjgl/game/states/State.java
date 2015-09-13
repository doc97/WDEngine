package gamedev.lwjgl.game.states;

public abstract class State {
	
	public abstract void enter();
	public abstract void loop(float dt);
	public abstract void exit();
	
}
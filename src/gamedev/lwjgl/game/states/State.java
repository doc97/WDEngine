package gamedev.lwjgl.game.states;

public abstract class State {

	public abstract void loadData();
	public abstract void enter();
	public abstract void exit();
	public abstract void update();
	public abstract void render();
	
}
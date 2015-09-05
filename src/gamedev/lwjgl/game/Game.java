package gamedev.lwjgl.game;

import gamedev.lwjgl.game.entities.EntitySystem;
import gamedev.lwjgl.game.states.StateSystem;

public enum Game {
	INSTANCE;
	
	public final StateSystem states = new StateSystem();
	public final EntitySystem entities = new EntitySystem();
	
	public void init(GameLauncher launcher) {
		states.create(launcher);
	}
}

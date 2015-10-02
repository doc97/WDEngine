package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.sound.SoundSystem;
import gamedev.lwjgl.game.entities.EntitySystem;
import gamedev.lwjgl.game.quests.QuestSystem;
import gamedev.lwjgl.game.states.StateSystem;

public enum Game {
	INSTANCE;
	
	public final StateSystem states = new StateSystem();
	public final EntitySystem entities = new EntitySystem();
	public final SoundSystem sounds = new SoundSystem();
	public final GameContainer container = new GameContainer();
	public final GamePhysics physics = new GamePhysics();
	public final QuestSystem quests = new QuestSystem();
	
	public void init(GameLauncher launcher) {
		container.init();
		states.create(launcher);
	}
}

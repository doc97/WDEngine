package gamedev.lwjgl.game;

import gamedev.lwjgl.engine.sound.SoundSystem;
import gamedev.lwjgl.game.systems.EntitySystem;
import gamedev.lwjgl.game.systems.InteractionSystem;
import gamedev.lwjgl.game.systems.LevelSystem;
import gamedev.lwjgl.game.systems.ParticleSystem;
import gamedev.lwjgl.game.systems.QuestSystem;
import gamedev.lwjgl.game.systems.ResourceSystem;
import gamedev.lwjgl.game.systems.StateSystem;

public enum Game {
	INSTANCE;
	
	public final StateSystem states = new StateSystem();
	public final EntitySystem entities = new EntitySystem();
	public final ParticleSystem particles = new ParticleSystem();
	public final SoundSystem sounds = new SoundSystem();
	public final GameContainer container = new GameContainer();
	public final GamePhysics physics = new GamePhysics();
	public final QuestSystem quests = new QuestSystem();
	public final InteractionSystem interactions = new InteractionSystem();
	public final LevelSystem levels = new LevelSystem();
	public final ResourceSystem resources = new ResourceSystem();
	
	public void init(GameLauncher launcher) {
		container.init();
		levels.init();
		resources.init();
		states.create(launcher);
	}
}

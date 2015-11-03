package gamedev.lwjgl.game.systems;

import java.util.EnumMap;
import java.util.Map;

import gamedev.lwjgl.game.GameLauncher;
import gamedev.lwjgl.game.states.CreditsState;
import gamedev.lwjgl.game.states.GameState;
import gamedev.lwjgl.game.states.IntroState;
import gamedev.lwjgl.game.states.MainMenuState;
import gamedev.lwjgl.game.states.State;

public class StateSystem {
	public enum States { MAINMENUSTATE, INTROSTATE, GAMESTATE, OPTIONSTATE, CREDITSSTATE };
	
	private Map<States, State> states = new EnumMap<States, State>(States.class);
	private GameLauncher launcher;
	
	public void create(GameLauncher launcher) {
		this.launcher = launcher;
		states.put(States.MAINMENUSTATE, new MainMenuState());
		states.put(States.INTROSTATE, new IntroState());
		states.put(States.GAMESTATE, new GameState());
		states.put(States.CREDITSSTATE, new CreditsState());
	}
	
	public void enterState(States s)
	{
		State state = states.get(s);
		if(state != null)
			launcher.setState(state);
		else
			throw new RuntimeException("No state assigned to: " + s.toString());
	}
}
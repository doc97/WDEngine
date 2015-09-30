package gamedev.lwjgl.game.states;

import java.util.EnumMap;
import java.util.Map;

import gamedev.lwjgl.engine.utils.MainMenuState;
import gamedev.lwjgl.game.GameLauncher;

public class StateSystem {
	public enum States { MAINMENUSTATE, INTROSTATE, GAMESTATE, OPTIONSTATE };
	
	private Map<States, State> states = new EnumMap<States, State>(States.class);
	private GameLauncher launcher;
	
	public void create(GameLauncher launcher) {
		this.launcher = launcher;
		states.put(States.MAINMENUSTATE, new MainMenuState());
		states.put(States.INTROSTATE, new IntroState());
		states.put(States.GAMESTATE, new GameState());
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

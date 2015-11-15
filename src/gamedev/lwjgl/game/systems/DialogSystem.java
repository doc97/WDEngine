package gamedev.lwjgl.game.systems;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.engine.utils.AssetManager;
import gamedev.lwjgl.game.text.Dialog;

public class DialogSystem {
	private Dialog currentDialog = new Dialog();
	private String currentKey = "";
	
	public void setCurrentDialog(String name) {
		if(currentDialog != null)
			currentDialog.reset();
		currentKey = name;
		currentDialog = AssetManager.getDialog(name);
	}
	
	public String getCurrentDialogKey() {
		return currentKey;
	}
	
	public Dialog getCurrentDialog() {
		return currentDialog;
	}
	
	public void render(SpriteBatch batch) {
		if(currentDialog != null) {
			currentDialog.render(batch);
		}
	}
}

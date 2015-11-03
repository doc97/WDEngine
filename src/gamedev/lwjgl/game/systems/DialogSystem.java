package gamedev.lwjgl.game.systems;

import java.util.ArrayList;

import gamedev.lwjgl.engine.render.SpriteBatch;
import gamedev.lwjgl.game.text.Dialog;

public class DialogSystem {
	private ArrayList<Dialog> dialogs = new ArrayList<Dialog>();
	private Dialog currentDialog;
	
	public void addDialog(Dialog dialog) {
		dialogs.add(dialog);
	}
	
	public void nextDialog() {
		if(getCurrentDialogIndex() < dialogs.size() - 1)
			dialogs.get(dialogs.indexOf(currentDialog) + 1);
	}
	
	public void setCurrentDialog(int index) {
		currentDialog = dialogs.get(index);
	}
	
	public int getCurrentDialogIndex() {
		return dialogs.indexOf(currentDialog);
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

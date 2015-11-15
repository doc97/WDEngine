package gamedev.lwjgl.engine.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import gamedev.lwjgl.game.text.Dialog;

public class JSONTool {
	
	public static void main(String[] args) {
		Map<String, Dialog> dialogs = new HashMap<String, Dialog>();
		Dialog d = new Dialog();
		d.addText("hello");
		Dialog d1 = new Dialog();
		d1.addText("bye");
		dialogs.put("Initial dialog", d);
		dialogs.put("Second dialog", d1);
		
		/*
		try {
			JSONTool.saveDialog("assets/dialogs/testdialog.json", dialogs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		try {
			Map<String, Dialog> map = JSONTool.readDialog("assets/dialogs/testdialog.json");
			System.out.println(map.get("Initial dialog").getCurrentTextIndex());
			System.out.println(map.get("Second dialog").getCurrentTextIndex());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveDialog(String path, Map<String, Dialog> dialogs) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Type stringDialogMap = new TypeToken<Map<String, Dialog>>(){}.getType();
		String s = gson.toJson(dialogs, stringDialogMap);
		Files.write(Paths.get(path), s.getBytes());
	}
	
	public static Map<String, Dialog> readDialog(String path) throws IOException {
		try(Reader reader = new FileReader(path)) {
			Gson gson = new Gson();
			Type stringDialogMap = new TypeToken<Map<String, Dialog>>(){}.getType();
            Map<String, Dialog> map = gson.fromJson(reader, stringDialogMap);
            reader.close();
            return map;
		}
	}
}

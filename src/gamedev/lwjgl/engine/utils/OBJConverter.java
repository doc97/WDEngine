package gamedev.lwjgl.engine.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import gamedev.lwjgl.engine.Logger;

public class OBJConverter {
	
	private enum ConvertType {
		SCALE, TRANSLATE, TRIM;
	}
	
	public static void main(String[] args) {
		String filename = "assets/maps/level1/outline";
		String source = OBJConverter.getSource(filename + ".obj");
		source = OBJConverter.convert(ConvertType.TRIM, source, 0.01f);
		//source = OBJConverter.convert(ConvertType.SCALE, source, 0.1f);
		OBJConverter.write(filename + "_converted.obj", source);
		
		Logger.message("OBJ Converter", "Done.");
	}
	
	private static String getSource(String filename) {
		String source = "";
		try {
			File f = new File(filename);
			FileReader reader = new FileReader(filename);
			char[] arr = new char[(int) f.length()];
			reader.read(arr);
			reader.close();
			source = String.valueOf(arr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}
	
	private static void write(String filename, String src) {
		try {
			PrintWriter writer = new PrintWriter(filename);
			writer.print(src);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static String convert(ConvertType type, String content, float value) {
		ArrayList<String> parts = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		String floatPattern = "-*\\d+\\.\\d+";
		String[] lines = content.split("\n");
		for(String line : lines) {
			String params[] = line.split(" ");
			for(int i = 0; i < params.length; i++) {
				String param = params[i].trim();
				if(param.matches(floatPattern)) {
					float fValue = Float.parseFloat(param);
					if(type == ConvertType.SCALE) {
						param = String.valueOf(fValue * value);
					} else if(type == ConvertType.TRANSLATE) {
						param = String.valueOf(fValue + value);
					} else if(type == ConvertType.TRIM) {
						if(fValue * fValue <= value * value)
							param = "0.0";
					}
				}
				
				parts.add(param);
				
				if(i < params.length - 1)
					parts.add(" ");
			}
			parts.add("\n");
		}
		
		for(String s : parts)
			builder.append(s);
		
		return builder.toString();
	}
}

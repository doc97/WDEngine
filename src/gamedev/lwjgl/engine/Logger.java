package gamedev.lwjgl.engine;

public class Logger {
	private static boolean debug = false;
	
	public static void message(String system, String message) {
		System.out.println("[" + system + "]: " + message);
	}
	
	public static void error(String system, String message) {
		System.err.println("[" + system + "]: " + message);
	}
	
	public static void setDebug(boolean debug) {
		Logger.debug = debug;
	}

	public static boolean isDebug() {
		return debug;
	}
}

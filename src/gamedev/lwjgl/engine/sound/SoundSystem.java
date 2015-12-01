package gamedev.lwjgl.engine.sound;

public class SoundSystem {
	
	public void playSound(Sound sound) {
		sound.stop();
		sound.play();
	}
	
	public void stopSound(Sound sound) {
		sound.stop();
	}
	
	public void loopSound(Sound sound) {
		sound.loop();
	}
	
	public void loopSound(Sound sound, int times) {
		sound.loop(times);
	}
	
	public boolean isSoundPlaying(Sound sound) {
		return sound.isPlaying();
	}
	
	public void setVolume(Sound sound, float level) {
		if (level < 0 || level > 100)
			return;
		boolean b = false;
		if (sound.isPlaying())
			b = true;
		sound.pause();
		sound.setVolume(level);
		if (b)
			sound.continuu();
	}
	
	public void setBalance(Sound sound, float balance) {
		if (balance < -1 || balance > 1)
			return;
		boolean b = false;
		if (sound.isPlaying())
			b = true;
		sound.pause();
		sound.setBalance(balance);
		if (b)
			sound.continuu();
	}
}
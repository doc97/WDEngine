package gamedev.lwjgl.engine.sound;

public class SoundSystem {
	
	public void playSound(Sound sound){
		sound.stop();
		sound.play();
	}
	
	public void stopSound(Sound sound){
		sound.stop();
	}
	
	public void loopSound(Sound sound){
		sound.loop();
	}
	
	public void loopSound(Sound sound, int times){
		sound.loop(times);
	}
	
	public boolean isSoundPlaying(Sound sound){
		return sound.isPlaying();
	}
}

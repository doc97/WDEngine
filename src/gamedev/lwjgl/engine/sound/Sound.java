package gamedev.lwjgl.engine.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements LineListener{
	
	private Clip clip;
	
	public Sound(String fileName){
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.addLineListener(this);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	void play(){
		clip.start();
	}
	
	void stop(){
		clip.stop();
		clip.setFramePosition(0);
	}
	
	void loop(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	void loop(int times){
		clip.loop(times);
	}
	
	boolean isPlaying(){
		return clip.isActive();
	}

	@Override
	public void update(LineEvent e) {
		if (e.getType() == LineEvent.Type.STOP){
			clip.setFramePosition(0);
		}
	}
	
	
}

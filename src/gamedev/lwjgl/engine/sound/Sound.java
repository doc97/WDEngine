package gamedev.lwjgl.engine.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements LineListener{
	
	public static final int LEFT = -1;
	public static final int CENTER = 0;
	public static final int RIGHT = 1;
	
	private int pause = 0;
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
	
	void setBalance(float balance){
		FloatControl c = (FloatControl) clip.getControl(Type.BALANCE);
		c.setValue(balance);
	}
	
	void setVolume(float percent){
		FloatControl c = (FloatControl) clip.getControl(Type.MASTER_GAIN);
		float v = c.getMinimum() + (Math.abs(c.getMaximum()) + Math.abs(c.getMinimum())) * (percent / 100.0f);
		c.setValue(v);
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
	
	void pause(){
		pause = clip.getFramePosition();
		clip.stop();
	}
	
	void continuu(){
		clip.start();
		clip.setFramePosition(pause);
	}
	
	@Override
	public void update(LineEvent e) {
		if (e.getType() == LineEvent.Type.STOP){
			clip.setFramePosition(0);
		}
	}
	
	
}

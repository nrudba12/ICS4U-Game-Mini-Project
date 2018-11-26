package modifiedconnectfour;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundEffect {
	TICK("tick.wav"),
	RESET("reset.wav"),
	GAMEOVER("gameover.wav");
	
	private Clip clip;
	
	SoundEffect(String soundFileName) {
		try {
			URL url = getClass().getClassLoader().getResource(soundFileName);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void play() {
		clip.setFramePosition(0); //resets clip to play from beginning
		clip.start();
	}
}
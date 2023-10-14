package com.jogo.main;

import java.io.*;
import javax.sound.sampled.*;


public class Sound {
	
	
	
	
	public static class Clips{
		private Clip[] clips;
		private int p;
		private int count;
		
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
			if(buffer == null) return;
			
			clips = new Clip[count];
			
			this.count = count;
			
			for (int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
				
			}
		}
			
		public void play() {
			
			if(clips == null) {
				return;
			}
			
			clips[p].stop();
			clips[p].setFramePosition(0);
			
			clips[p].start();
			p++;
			if(p>=count) p = 0;
		}
		
		public void stop() {
			
			if(clips == null) {
				return;
			}
			
			clips[p].stop();
			clips[p].setFramePosition(0);

		}
			
		public void loop() {
			if(clips == null) return;
			clips[p].loop(300);
			
		}
		
		public void setVolume(float gainAmount) {
			FloatControl volume = (FloatControl) clips[p].getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(gainAmount);
			
		}
		
		
	}
	
	public static Clips music = load("/Song.wav", 1);
	public static Clips eat = load("/eat.wav", 1);
	public static Clips drink = load("/eat.wav", 1);
	public static Clips game_over = load("/gameOver.wav", 1);
	public static Clips menu_swap = load("/menuSwap.wav", 1);
	public static Clips level_cleared = load("/levelCleared.wav", 1);
	public static Clips step = load("/step.wav", 1);
	public static Clips hit = load("/hit.wav", 1);
	
	
	
	private static Clips load(String name, int count) {

		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0 , read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data, count);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	

}

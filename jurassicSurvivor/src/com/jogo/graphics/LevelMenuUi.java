package com.jogo.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.jogo.main.Game;
import com.jogo.main.Sound;

public class LevelMenuUi {

	public String[] options;
	
	public int currentOption = 0;
	private int maxOption;
	
	InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	Font pixelart;
	
	public boolean up = false;
	public boolean down = false;
	public boolean enter = false;
	
	private boolean loaded = false;
	
	private static int maxLevel = 0;
	
	public LevelMenuUi() {
		
		try {
			pixelart = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		options = new String[Game.levels.size()+1];
		
		loadResults();
		
		for(int i = 0; i < Game.levels.size(); i++) {
			options[i] = Game.levels.get(i).name;
		}
		 
		options[Game.levels.size()]="VOLTAR";
		
		maxOption = options.length;

	}
	
	

	private void loadResults() {
		File file = new File("save.txt");
		if(file.exists()) {
			String saver = loadGame(10);
			
			applySave(saver);
		}
		
	}



	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,255));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE*2 , Game.HEIGHT*Game.SCALE*2);
		
		g2.setColor(Color.white);

		
		g.setFont(pixelart.deriveFont(20f));
		
		for(int i = 0; i < maxOption; i++) {
			
			g2.setColor(Color.white);
			if(i == currentOption) {
				
				g2.setColor(Color.red);
			}else if(options[i] == "VOLTAR"){
				
				g2.setColor(Color.white);
			}else if(i> maxLevel) {
				
				g2.setColor(Color.gray);
			}
			
			
			g.drawString(options[i], 180 , 200 + (i * 30)  );
			
			
		}
		
	}
	
	
	public void tick() {
		
		if(!loaded) {
			loadResults();
		}
		
		if(up) {
			Sound.menu_swap.stop();
			Sound.menu_swap.play();
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption -1;
			}else if(currentOption > maxLevel) {
				currentOption = maxLevel;
			}
			
		}
		
		if(down) {
			Sound.menu_swap.stop();
			Sound.menu_swap.play();
			down = false;
			currentOption++;
			
			if(currentOption > maxOption -1){
				currentOption = 0;
			}else if(currentOption > maxLevel) {
				currentOption = maxOption-1;
			}
			
		}
		
		if(enter) {
			enter = false;
			Sound.eat.play();
			if(options[currentOption]!= "VOLTAR") {
				Game.currentLevel = currentOption;
				loaded = false;
				Game.restart();
				
			
			}else {
				Game.gameState = "MENU";
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			try {
				System.out.println(spl2[1]);
				maxLevel = Integer.parseInt(spl2[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			String singleLine = null;
			try {
				BufferedReader saveReader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine  = saveReader.readLine()) != null) {
						String[] transcode = singleLine.split(":");
						char[] val = transcode[1].toCharArray();
						transcode[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							transcode[1]+=val[i];
						}
						line+=transcode[0];
						line+=":";
						line+=transcode[1];
						line+="/";
						
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return line;
	}
	
	public void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter saveWrite = null;
		
		try {
			saveWrite = new BufferedWriter(new FileWriter("save.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			
			try {
				saveWrite.write(current);
				if(i < val1.length -1) {
					saveWrite.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				saveWrite.flush();
				saveWrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
	}
	
}

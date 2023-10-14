package com.jogo.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import com.jogo.main.Game;
import com.jogo.main.Sound;

public class MenuUI {

	public int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	public int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public String[] options = {"CONTINUAR", "NOVO JOGO", "SAIR"};
	
	
	
	
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
	public Font pixelart;
	
	public int currentOption = 0;
	private int maxOption = options.length;
	
	private boolean up = false;
	private boolean down = false;
	private boolean enter = false;

	
	private boolean resetData = false;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	private Image menuBackground;
	
	
	
	public MenuUI() {
		
		try {
			pixelart = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			menuBackground = ImageIO.read(getClass().getResource("/menu.png"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		Sound.menu_swap.setVolume(-10);
	}
	
	
	public void render(Graphics g) {
		
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,255));
		g2.fillRect(0, 0, width , height);
		
		g2.setColor(Color.white);
		g.drawImage(menuBackground, 0, 0, width , height, null);
		
		
		
		g.setFont(pixelart.deriveFont(45f));
		
		
		for(int i = 0; i < maxOption; i++) {
			
			if(i == currentOption) {
				g.setFont(pixelart.deriveFont(45f));
				g2.setColor(Color.red);
			}else {
				g.setFont(pixelart.deriveFont(40f));
				g2.setColor(Color.white);
			}
			
			g.drawString(options[i], 150 , 300 + (i * 60)  );
			
		}
		
		if(resetData) {
			g2.setColor(new Color(255,0,0,255));
			g2.fillRect(500, 260, 700 , 220);
			
			g.setFont(new Font("arial", Font.BOLD, 40));
			g2.setColor(Color.black);
			
			g.drawString("O seu progresso será apagado", 530, 320);
			g.drawString("Aperte ENTER para confirmar", 530, 380);
		}
		g2.setColor(Color.white);
		g.setFont(pixelart.deriveFont(25f));
		g.drawString("Ver. Aplha 0.1", 930, 230);
		
		g.setFont(pixelart.deriveFont(25f));
		g.drawString("Por    Rodrigo Frank    e    Sonsei Blue", 500, 680);
		
	}
	
	public void tick() {
		if(up) {
			
			Sound.menu_swap.stop();
			Sound.menu_swap.play();
			up = false;
			resetData = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption -1;
			}
			
		}
		
		if(down) {
			Sound.menu_swap.stop();
			Sound.menu_swap.play();
			resetData = false;
			down = false;
			currentOption++;
			if(currentOption >= maxOption) {
				currentOption = 0;
			}
			
		}
		
		if(enter) {
			enter = false;
			Sound.eat.play();

			if(options[currentOption]== "CONTINUAR") {
				
				
				Game.gameState = "LEVEL_MENU";


			}else if(options[currentOption]== "NOVO JOGO") {
				
				if(!resetData) {
					resetData = true;
				}else {
					String[] opt1 = {"level"};
					int[] opt2 = {1};
					saveGame(opt1, opt2, 10);
					Game.currentLevel = 0;
					Game.restart();
				}
				
				
			
			}if(options[currentOption]== "SAIR") {
				System.exit(1);
			}
		}
	}
	
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
			
			
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
	
	
	
	
	public void setUp() {
		up = true;
	}
	
	public void setDown() {
		down = true;
	}
	
	public void setEnter() {
		enter = true;
	}
}

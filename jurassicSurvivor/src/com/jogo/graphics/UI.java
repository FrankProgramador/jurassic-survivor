package com.jogo.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;

public class UI {
	
	public int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	public int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int x = 5;
	private int y = 5;
	private int framesGameOver = 0;
	private BufferedImage[] hunger;
	private BufferedImage[] hungerSick;
	private BufferedImage[] thirst;
	
	private int frames = 0;
	
	private int framesSick = 0;
	
	public UI() {
		
		hunger = new BufferedImage[8];
		hungerSick = new BufferedImage[8];
		thirst = new BufferedImage[8];
		int maxHunger = 8;
		int countHunger = 7;
		int maxThirst = 8;
		int countThirsh = 7;
		
		for(int i = 0; i < maxHunger; i++) {
			hunger[i] = Game.hunger.getSprite(16*countHunger, 0, 16, 16);
			countHunger--;
		}
		
		countHunger = 7;
		for(int i = 0; i < maxHunger; i++) {
			hungerSick[i] = Game.hunger.getSprite(16*countHunger, 16, 16, 16);
			countHunger--;
		}
		
		for(int i = 0; i < maxThirst; i++) {
			thirst[i] = Game.thirst.getSprite(16*countThirsh, 0, 16, 16);
			countThirsh--;
		}
		
	
	}
	
	public void render(Graphics g) {

		g.setColor(Color.red);
		g.fillRect(x , y, 400 , 50);
		g.setColor(Color.green);
		g.fillRect(x, y, (int)((Game.player.getLife()/Game.player.getMaxLife())*400), 50);
		
		g.setColor(Color.red);
		g.fillRect(x , y + 60, 400 , 10);
		g.setColor(Color.yellow);
		g.fillRect(x, y + 60, (int)((Game.player.stamina/100)*400), 10);
		
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 32));
		g.drawString((int)Game.player.getLife() + "/" + (int)Game.player.getMaxLife(), 140, 40);
		
		if(Game.player.isSick) {
			framesSick++;
			if(framesSick <= 20) {
				g.drawImage(hungerSick[Game.player.getHunger()], width - 150, height - 150, 16*10 , 16*10, null);
			}else if(framesSick <=40) {
				g.drawImage(hunger[Game.player.getHunger()], width - 150, height - 150, 16*10 , 16*10, null);
				if(framesSick == 40) {
					framesSick = 0;
				}
				
			}
			
		}else {
			g.drawImage(hunger[Game.player.getHunger()], width - 150, height - 150, 16*10 , 16*10, null);
		}

		g.drawImage(thirst[Game.player.getThirst()], width - 200, height - 200, 16*10 , 16*10, null);
		
		
		
		// System.out.println(Game.player.getHunger());
		
	}
	
	public void levelMessage(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		
		g2.fillRect(0, 0, width , height);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 64));
		g.drawString(Game.levels.get(Game.currentLevel).name, 40, 100);
		
		g.setFont(new Font("arial", Font.BOLD, 32));
		
		if(Game.levels.get(Game.currentLevel).introMessage != null) {
			String message[] = Game.levels.get(Game.currentLevel).introMessage.split("\n");
			
			for (int i = 0; i < message.length; i++) {
					g.drawString(message[i], 40, 200 + (i*40));
					
			}
		}
			
		
		if(frames < 25) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString(">>> Pressione ENTER para começar <<<",  width/7 , 500);
			frames++;
		}else if(frames < 50){
			frames++;
		}else {
			frames = 0;
		}
	}
	
	public void clearLevelMessage(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		
		g2.fillRect(0, 0, width , height);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 64));
		g.drawString("Parabéns, você passou de fase", 40, 100);

		
		if(frames < 25) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString(">>> Pressione ENTER para continuar <<<",  width/7 , 500);
			frames++;
		}else if(frames < 50){
			frames++;
		}else {
			frames = 0;
		}
	}
	
	
	public void gameOverUI(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, width , height);
		g2.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 90));
		g.drawString("GAME OVER!", width/2 - width/5  , 200);
		
		
		if(framesGameOver < 25) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString(">>> Pressione ENTER para reiniciar <<<",  width/7 , 380);
			framesGameOver++;
		}else if(framesGameOver < 50){
			framesGameOver++;
		}else {
			framesGameOver = 0;
		}
	}
	
	public void congratzUI(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, width , height);
		g2.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 90));
		g.drawString("PARABÉNS", width/2 - width/5  , 200);
		
		g.setFont(new Font("arial", Font.BOLD, 50));
		g.drawString("Você concluiu a versão alpha!", width/2 - width/5  , 280);
		
		
		if(framesGameOver < 25) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString(">>> Pressione ENTER para voltar ao menu <<<",  width/7 , 380);
			framesGameOver++;
		}else if(framesGameOver < 50){
			framesGameOver++;
		}else {
			framesGameOver = 0;
		}
	}
	

	

}

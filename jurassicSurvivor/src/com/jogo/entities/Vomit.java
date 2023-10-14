package com.jogo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.world.Camera;

public class Vomit extends Entity{
	
	private BufferedImage[] vomit;
	private int index;
	private int frames;
	
	public Vomit(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		
		vomit = new BufferedImage[3];
		vomit[0] = Game.spritesheet.getSprite(0, 16*8 , 16, 16);
		vomit[1] = Game.spritesheet.getSprite(16, 16*8 , 16, 16);
		vomit[2] = Game.spritesheet.getSprite(32, 16*8 , 16, 16);
	}


	
	
	public void tick() {
		frames++;
		if(frames >5 && index < 2) {
			frames = 0;
			index++;
		}
		
		if(frames>120) {
			Game.entities.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(vomit[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

}

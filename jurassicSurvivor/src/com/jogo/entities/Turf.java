package com.jogo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.world.Camera;

public class Turf extends Entity{

	public static BufferedImage turf = Game.grass.getSprite(0, 32, 16, 16);
	
	public Turf(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		depth = 5;
		this.mask_height = 2;
		this.mask_y = 7;
		this.mask_width= 3; 
		this.mask_x = 6;
	}
	
	public void render(Graphics g) {
		
		//super.debugRender(g);
		g.drawImage(turf, this.getX() - Camera.x, this.getY() - Camera.y, null);

	}

}

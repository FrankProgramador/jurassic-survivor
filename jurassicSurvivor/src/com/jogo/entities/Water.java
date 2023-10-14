package com.jogo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.world.Camera;

public class Water extends Entity{

	private BufferedImage water;
	
	public Water(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		
		depth = 4;
		
		water = Game.grass.getSprite(0, 16, 16, 16);
		mask_x = 3;
		mask_y = 7;
		mask_width = 10;
		mask_height = 4;
	}

	
	
	
	
	public void render(Graphics g) {
		
		
		g.drawImage(water, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		// g.setColor(Color.red);
		// g.fillRect(this.getX()+this.getMask_x() - Camera.x, this.getY() + this.getMask_y() - Camera.y, this.getMask_width(), this.getMask_height());
	}
	
}

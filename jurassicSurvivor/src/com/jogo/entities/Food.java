package com.jogo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.main.Sound;
import com.jogo.world.Camera;

public class Food extends Entity {

	private BufferedImage[] food;
	public int life = 3;
	
	private int regen = 500;
	
	public Food(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		depth = 5;
		
		food = new BufferedImage[4];
		food[0] = Game.grass.getSprite(16*4, 16, 16, 16);
		food[1] = Game.grass.getSprite(16*3, 16, 16, 16);
		food[2] = Game.grass.getSprite(16*2, 16, 16, 16);
		food[3] = Game.grass.getSprite(16*1, 16, 16, 16);
		
		mask_x = 3;
		mask_y = 10;
		mask_width = 8;
		mask_height = 4;
	}
	
	
	
	public void render(Graphics g) {
		 mask_x = 3;
			mask_y = 11;
			mask_width = 6;
			mask_height = 4;
		g.drawImage(food[life], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
	}
	
	public void tick() {
		if(life<3) {
			regen--;
			if(regen == 0) {
				life++;
				regen = 500;
			}
		}
		
		

		
	}
	

	
	public boolean canBeEated() {
		if(life>0) {
			return true;
		}else {
			return false;
		}
	}
	
	

}

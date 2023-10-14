package com.jogo.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.world.Camera;

public class FoodPoison extends Food {

	private BufferedImage[] food;
	private int life = 3;
	private int leafLife = 20;
	
	private int regen = 500;
	
	public FoodPoison(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		depth = 5;
		
		food = new BufferedImage[4];
		food[0] = Game.grass.getSprite(16*4, 32, 16, 16);
		food[1] = Game.grass.getSprite(16*3, 32, 16, 16);
		food[2] = Game.grass.getSprite(16*2, 32, 16, 16);
		food[3] = Game.grass.getSprite(16*1, 32, 16, 16);
		
		mask_x = 2;
		mask_y = 11;
		mask_width = 8;
		mask_height = 6;
	}
	
	
	
	public void render(Graphics g) {
		
		 super.debugRender(g);
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
	
	public void eaten() {
		if(leafLife > 0) {
			leafLife--;
			if(leafLife == 0) {
				leafLife = 20;
				if(life > 0) {
					life--;
					Game.player.isSick = true;
					Game.player.setHunger();
				}
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

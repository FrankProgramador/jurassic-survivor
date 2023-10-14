package com.jogo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;
import com.jogo.world.Camera;

public class PlayerAtack extends Entity{

	private int life = 5;
	
	public PlayerAtack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	
	public void tick() {
		life--;
		if(life<=0) {
			Game.entities.remove(this);
			return;
		}
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Enemy) {
				if(isColliding(this, e)) {
					
					((Enemy) e).life--;
					Game.entities.remove(this);
					return;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect((int)x - Camera.x, (int)y-Camera.y, width, height);
	}
	
	
	

}

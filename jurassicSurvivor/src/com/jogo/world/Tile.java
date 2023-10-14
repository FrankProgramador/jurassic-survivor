package com.jogo.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.grass.getSprite(16 * 1, 0, 16, 16);
	public static BufferedImage TILE_WALL = Game.enviroments.getSprite(16*5, 0, 16, 16);
			
	private BufferedImage sprite;
	protected int x, y;
	
	
	

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
	
	
}

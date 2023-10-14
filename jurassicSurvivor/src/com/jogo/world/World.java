package com.jogo.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jogo.entities.Enemy;
import com.jogo.entities.Entity;
import com.jogo.entities.ExitArea;
import com.jogo.entities.Food;
import com.jogo.entities.FoodPoison;
import com.jogo.entities.Turf;
import com.jogo.entities.Water;
import com.jogo.graphics.Spritesheet;
import com.jogo.main.Game;


public class World {
	
	public static Tile[] tiles;
	

	public static int WIDTH, HEIGHT;
	public final static int TILE_SIZE = 16;
	
	private BufferedImage[] grass;
	public Spritesheet grassSprite = new Spritesheet("/grass.png");
	
	public World(String path) {
		
		if(path != "RANDOMIZE") {
			
		
			try {
				BufferedImage map = ImageIO.read(getClass().getResource(path));
				
				int[] pixels = new int[map.getWidth()*map.getHeight()];
				
				tiles = new Tile[map.getWidth()*map.getHeight()];
				WIDTH = map.getWidth();
				HEIGHT = map.getHeight();
				map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
				
				grass = new BufferedImage[12];
				
				for(int i = 0; i < 11; i++) {
					grass[i] = grassSprite.getSprite(16 * i, 0, 16, 16);
				}
				
				
				
				for(int xx = 0; xx < map.getWidth(); xx++) {
					for(int yy = 0; yy < map.getHeight(); yy++) {
						int pixelAtual = pixels[xx + (yy*map.getWidth())];
						tiles[xx + (yy*map.getWidth())] = new FloorTile(xx*16, yy*16, grass[Game.rand.nextInt(11)]);
						

						if(pixelAtual == 0xFFFFFFFF) {

							tiles[xx + (yy*map.getWidth())] = new WallTile(xx*16, yy*16, null);

						}else if(pixelAtual == 0xFF0000ff) {	
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
						}else if(pixelAtual == 0xFFFF0000) {
							// Enemy
							Entity en = new Enemy(xx*16, yy*16, 16, 16, null);	
							
							Game.entities.add(en);
						}else if(pixelAtual == 0xFFFF6600) {
							// Enemy
							Enemy en = new Enemy(xx*16, yy*16, 16, 16, null);
							en.patrolDir = 1;
							Game.entities.add(en);
						}else if(pixelAtual == 0xFFFF6666) {
							// Enemy
							Enemy en = new Enemy(xx*16, yy*16, 16, 16, null);
							en.patrolDir = 2;
							Game.entities.add(en);
						}else if(pixelAtual == 0xFF00FF00) {
							// planta
							Entity en = new Food(xx*16, yy*16, 16, 16, null);	
							Game.entities.add(en);
						}else if(pixelAtual == 0xFFFF00FF) {
							// planta venenosa
							Entity en = new FoodPoison(xx*16, yy*16, 16, 16, null);	
							Game.entities.add(en);
						}else if(pixelAtual == 0xFF00FFFF) {
							// �gua
							Entity en = new Water(xx*16, yy*16, 16, 16, null);	
							Game.entities.add(en);
						}else if(pixelAtual == 0xFFFFFF00) {
							// �gua
							Entity en = new ExitArea(xx*16, yy*16, 16, 16, null);	
							Game.entities.add(en);
						}
						else if(pixelAtual == 0xFF40BB00) {
							// �gua
							Entity en = new Turf(xx*16, yy*16, 16, 16, null);
							Entity en2 = new Turf(xx*16, (yy*16)-3, 16, 16, null);
							Entity en3 = new Turf(xx*16, (yy*16) - 6, 16, 16, null);
							Game.entities.add(en);
							Game.entities.add(en2);
							Game.entities.add(en3);
							if(pixels[xx+1 + (yy*map.getWidth())] !=  0xFFFFFFFF) {
								Entity en4 = new Turf((xx*16)+4, (yy*16), 16, 16, null);
								Game.entities.add(en4);
							}
							if(pixels[xx-1 + (yy*map.getWidth())] !=  0xFFFFFFFF) {
								Entity en5 = new Turf((xx*16)-4, (yy*16), 16, 16, null);
								Game.entities.add(en5);
							}
							
						}
					}	
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			Game.player.setX(0);
			Game.player.setY(0);
			
			WIDTH = 100;
			HEIGHT = 100;
			
			tiles = new Tile[WIDTH * HEIGHT];
			
			for(int xx = 0; xx < WIDTH; xx++) {
				for(int yy = 0; yy < HEIGHT; yy++) {
					tiles[xx+yy*WIDTH] = new Tile(xx*16, yy*16, Tile.TILE_WALL);
				}
			}
			
			int dir = 0;
			int xx = 0;
			int yy = 0;
			
			for (int i= 0; i < 200; i++){
				
				if(dir==0) //esquerda
				{
					if(xx < WIDTH) {
						xx++;
					}
					
				}else if(dir == 1) { //direita
					if(xx > 0){
						xx--;
					}
								
				}else if(dir == 2){ //baixo
					if(yy < HEIGHT) {
						yy++;
					}		
				
				}else if(dir == 3){ //cima
			
					if(yy > 0) {
						yy--;
					}
				}
				
				if(Game.rand.nextInt(100) < 30) {
					dir = Game.rand.nextInt(4);
				}
				System.out.println("X / Y :" + xx + " / " + yy);
				tiles[xx+yy*WIDTH] = new Tile(xx*16, yy*16, Tile.TILE_FLOOR);
				
			}
		}
		
	}
	

	

	
	
	public static boolean isFree(int xNext, int yNext) {

		
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE-1)/ TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE-1)/ TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE-1)/ TILE_SIZE;
		int y4 = (yNext + TILE_SIZE-1)/ TILE_SIZE;
		

		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile)  ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	

	
	public static boolean isFree(Entity e, int xNext, int yNext) {
				
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE-1)/ TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE-1)/ TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE-1)/ TILE_SIZE;
		int y4 = (yNext + TILE_SIZE-1)/ TILE_SIZE;
		

		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile)  ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4) +1 ;
		int yfinal = ystart + (Game.HEIGHT >> 4) +1;
		
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx <0 || yy <00 || xx >= WIDTH || yy >= HEIGHT || xx > (Camera.x/16) + 10 || yy > (Camera.y/16) + 8) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}

	public static void renderMiniMap(Graphics g) {
		
		for(int i = 0; i < Game.miniMapPixels.length; i++) {
			Game.miniMapPixels[i] = 0;
		}
		
		for(int xx = 0; xx < World.WIDTH; xx++) {
			for(int yy = 0; yy < World.HEIGHT; yy++) {
				if(World.tiles[xx + (yy*WIDTH)] instanceof WallTile) {
					Game.miniMapPixels[xx + (yy*WIDTH)] = 0xff505050; 
				}
			}
		}

		g.drawImage(Game.miniMap, 550, 10, World.WIDTH*5, World.HEIGHT*5, null);
		
	}

	public static Tile getTile(int position) {
		Tile tile = null;
		if(position >= 0 && position < tiles.length) {
			tile = tiles[position];
		}
		return tile;
	}
	
	
}

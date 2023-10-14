package com.jogo.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jogo.main.Game;

public class WallTile extends Tile {

	
	private static BufferedImage TILE_WALL_1 = Game.grass.getSprite(16*5, 16, 16, 16);
	private static BufferedImage TILE_WALL_MID = Game.grass.getSprite(16*10, 16*7, 16, 16);
	
	
	private static BufferedImage WALL_TL_2_4 = Game.grass.getSprite(16*14, 16*0 , 8, 8);
	private static BufferedImage WALL_TL_24 = Game.grass.getSprite(16*7, 16*2 , 8, 8);
	private static BufferedImage WALL_TL_1_24 = Game.grass.getSprite(16*7, 16*4 , 8, 8);
	private static BufferedImage WALL_TL_0_124 = Game.grass.getSprite(16*10, 16*7 , 8, 8);
	private static BufferedImage WALL_TL_4_12 = Game.grass.getSprite(16*7, 16*3 , 8, 8);
	
	
	private static BufferedImage WALL_TR_2_6 = Game.grass.getSprite((16*14)+8, 16*0 , 8, 8);
	private static BufferedImage WALL_TR_26 = Game.grass.getSprite(16*7+8, 16*2 , 8, 8);
	private static BufferedImage WALL_TR_3_26 = Game.grass.getSprite(16*7+8, 16*4 , 8, 8);
	private static BufferedImage WALL_TR_0_236 = Game.grass.getSprite(16*10+8, 16*7 , 8, 8);
	private static BufferedImage WALL_TL_6_23 = Game.grass.getSprite(16*7+8, 16*3 , 8, 8);
	

	
	private static BufferedImage WALL_DR_8_6 = Game.grass.getSprite((16*8)+8, 16*4+8 , 8, 8);
	private static BufferedImage WALL_DR_68 = Game.grass.getSprite((16*7)+8, 16*5+8 , 8, 8);
	private static BufferedImage WALL_DR_0_689 = Game.grass.getSprite((16*10)+8, 16*7+8 , 8, 8);
	private static BufferedImage WALL_DR_9_68 = Game.grass.getSprite((16*7)+8, 16*4+8 , 8, 8);
	
	private static BufferedImage WALL_DR_6_8 = Game.grass.getSprite((16*7)+8, 16*3+8 , 8, 8);
	
	private static BufferedImage WALL_DL_8_4 = Game.grass.getSprite((16*8), 16*4+8 , 8, 8);
	private static BufferedImage WALL_DL_48 = Game.grass.getSprite((16*7), 16*5+8 , 8, 8);
	private static BufferedImage WALL_DL_0_478 = Game.grass.getSprite((16*10), 16*7+8 , 8, 8);
	private static BufferedImage WALL_DL_7_48 = Game.grass.getSprite((16*7), 16*4+8 , 8, 8);
	
	private static BufferedImage WALL_DL_4_8 = Game.grass.getSprite((16*7), 16*3+8 , 8, 8);
	

	
	private boolean pos1 = false;
	private boolean pos2 = false;
	private boolean pos3 = false;
	private boolean pos4 = false;
	private boolean pos6 = false;
	private boolean pos7 = false;
	private boolean pos8 = false;
	private boolean pos9 = false;
	
	public WallTile(int x, int y, BufferedImage sprite) {
		super(x, y, sprite);
		
	}
	
	

	public void validatePositions() {
		
		int xx = x/16;
		int yy = y/16;
				
		
		if(xx - 1 >= 0 && yy -1 >= 0 && World.tiles[xx-1 + ((yy-1)*World.WIDTH)] instanceof WallTile) {
			pos1 = true;
		}
		
		if(yy - 1 >= 0 && World.tiles[xx + ((yy-1)*World.WIDTH)] instanceof WallTile) {
			pos2 = true;
		}
	
		if(xx +1 < World.WIDTH && yy -1 >= 0 && World.tiles[xx+1 + ((yy-1)*World.WIDTH)] instanceof WallTile) {
			pos3 = true;
		}
		
		if(xx -1 >= 0 && World.tiles[xx-1 + (yy*World.WIDTH)] instanceof WallTile) {
			pos4 = true;
		}
		
		if(xx +1 < World.WIDTH && World.tiles[xx+1 + ((yy)*World.WIDTH)] instanceof WallTile) {
			pos6 = true;
		}
		
		if(xx -1 >= 0 && yy + 1 < World.HEIGHT && World.tiles[xx-1 + ((yy+1)*World.WIDTH)] instanceof WallTile) {
			pos7 = true;
		}
		
		if(yy + 1 < World.HEIGHT && World.tiles[xx + ((yy+1)*World.WIDTH)] instanceof WallTile) {
			pos8 = true;
		}
		
		if(xx +1 < World.WIDTH && yy + 1 < World.HEIGHT && World.tiles[xx+1 + ((yy+1)*World.WIDTH)] instanceof WallTile) {
			pos9 = true;
		}
	}
	
	public void render(Graphics g) {
		validatePositions();
		if(!pos1 && !pos2 && !pos3 && !pos4 && !pos6 && !pos7 && !pos8 && !pos9) {
			g.drawImage(WallTile.TILE_WALL_1, x - Camera.x, y - Camera.y, null);
		}else {
			g.drawImage(WallTile.TILE_WALL_MID, x - Camera.x, y - Camera.y, null);
			
			
			if(!pos2 && pos4) {
				g.drawImage(WallTile.WALL_TL_2_4, x - Camera.x, y - Camera.y, null);
			}else if(!pos2 && !pos4) {
					g.drawImage(WallTile.WALL_TL_24, x - Camera.x, y - Camera.y, null);

			}else if(!pos1 && pos2 && pos4) {
				g.drawImage(WallTile.WALL_TL_1_24, x - Camera.x, y - Camera.y, null);

			}else if(pos1 && pos2 && pos4) {
				g.drawImage(WallTile.WALL_TL_0_124, x - Camera.x, y - Camera.y, null);

			}else if(pos2 && !pos4) {
				g.drawImage(WallTile.WALL_TL_4_12, x - Camera.x, y - Camera.y, null);

			}
			
			
			
			if(!pos2 && pos6) {
				g.drawImage(WallTile.WALL_TR_2_6, x - Camera.x + 8, y - Camera.y, null);
			}else if(!pos2 && !pos6) {
				g.drawImage(WallTile.WALL_TR_26, x - Camera.x + 8, y - Camera.y, null);
			}else if(!pos3 && pos2 && pos6) {
				g.drawImage(WallTile.WALL_TR_3_26, x - Camera.x + 8, y - Camera.y, null);
			}else if(pos3 && pos2 && pos6) {
				g.drawImage(WallTile.WALL_TR_0_236, x - Camera.x + 8, y - Camera.y, null);
			}else if(pos2 && !pos6) {
				g.drawImage(WallTile.WALL_TL_6_23, x - Camera.x + 8, y - Camera.y, null);
			}
			
		
			if(!pos8 && pos6) {
				g.drawImage(WallTile.WALL_DR_8_6, x - Camera.x + 8, y - Camera.y + 8, null);
			}else if(!pos8 && !pos6) {
				g.drawImage(WallTile.WALL_DR_68, x - Camera.x + 8, y - Camera.y + 8, null);
			}else if(pos9 && pos6 && pos8) {
				g.drawImage(WallTile.WALL_DR_0_689, x - Camera.x + 8, y - Camera.y + 8, null);
			}else if(!pos9 && pos6 && pos8) {
				g.drawImage(WallTile.WALL_DR_9_68, x - Camera.x + 8, y - Camera.y + 8, null);
			}else if(!pos6 && pos8) {
				g.drawImage(WallTile.WALL_DR_6_8, x - Camera.x + 8, y - Camera.y + 8, null);
			}
			
			
			
			if(!pos8 && pos4) {
				g.drawImage(WallTile.WALL_DL_8_4, x - Camera.x, y - Camera.y + 8, null);
			}else if(!pos8 && !pos4) {
				g.drawImage(WallTile.WALL_DL_48, x - Camera.x, y - Camera.y + 8, null);
			}else if(pos4 && pos7 && pos8) {
				g.drawImage(WallTile.WALL_DL_0_478, x - Camera.x, y - Camera.y + 8, null);
			}else if(!pos7 && pos4 && pos8) {
				g.drawImage(WallTile.WALL_DL_7_48, x - Camera.x, y - Camera.y + 8, null);
			}else if(!pos4 && pos8) {
				g.drawImage(WallTile.WALL_DL_4_8, x - Camera.x, y - Camera.y + 8, null);
			}
			
			
			
			
		}

	}
	

}

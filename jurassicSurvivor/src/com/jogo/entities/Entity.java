package com.jogo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.jogo.main.Game;
import com.jogo.world.Camera;
import com.jogo.world.Node;
import com.jogo.world.Vector2i;
import com.jogo.world.WallTile;
import com.jogo.world.World;


public class Entity {

	
	protected List<Node> path;
	
	protected boolean debugMode = false;
	
	protected boolean collision = false;
	
	protected double x;
	protected double y;
	protected double initial_x;
	protected double initial_y;
	
	protected int width;
	protected int height;
	
	protected int mask_x = 0;
	protected int mask_y = 0;
	protected int mask_width = 16;
	protected int mask_height = 16;
	
	public int depth = 0;
	
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.setX(x);
		this.setY(y);
		this.initial_x = x;
		this.initial_y = y;
		
		this.setHeight(height);
		this.setWidth(width);
		
		this.setMask_height(height);
		this.setMask_width(width);
		
		this.sprite = sprite;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle mask1 = new Rectangle(e1.mask_x + e1.getX(), e1.mask_y + e1.getY(), e1.mask_width, e1.mask_height);
		Rectangle mask2 = new Rectangle( e2.mask_x + e2.getX(), e2.mask_y + e2.getY(), e2.mask_width, e2.mask_height);

		return mask1.intersects(mask2);
	}
	
	public static boolean isColliding(Rectangle r1, Entity e2) {
		
		Rectangle mask2 = new Rectangle( e2.mask_x + e2.getX(), e2.mask_y + e2.getY(), e2.mask_width, e2.mask_height);

		return r1.intersects(mask2);
	}
	
	
	
	public static boolean willCollide(Entity e1, Entity e2, double speedX, double speedY) {
		Rectangle mask1 = new Rectangle(e1.mask_x + e1.getX() + (int)speedX , e1.mask_y + e1.getY() + (int)speedY, e1.mask_width, e1.mask_height);
		Rectangle mask2 = new Rectangle( e2.mask_x + e2.getX(), e2.mask_y + e2.getY(), e2.mask_width, e2.mask_height);

		return mask1.intersects(mask2);
	}

	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		

	}
	
	
	// método para arrumar a máscara com ajuda visual
	protected void debugRender(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(this.getX()+this.getMask_x() - Camera.x, this.getY() + this.getMask_y() - Camera.y, this.getMask_width(), this.getMask_height());
	}

	public double calculateDistance(
		    double x1, 
		    double y1, 
		    double x2, 
		    double y2) 
	{
		        
		    double ac = Math.abs(y2 - y1);
		    double cb = Math.abs(x2 - x1);
		        
		    return Math.hypot(ac, cb);
	}
	
	
	/*
	 * canMove verifica a próxima posição a ser explorada
	 * levando em consideração a máscara de colisão
	 * 
	 * A velocidade está em double para detectar melhor a colisão e evitar bugs
	 * 
	 * Caso a velocidade seja negativa, utilizar velocidade * -1
	 */
	protected boolean canMove(double xSpeed, double ySpeed) {
		
		
		int xx = (int)(x + xSpeed + this.mask_x)/16;
		
		int yy = (int)(y + ySpeed +  this.mask_height)/16;

		int x1 = xx; 
		int y1 = yy ;
		
		int x2 = (int)(x + xSpeed + this.mask_x + this.mask_width)/16;
		int y2 = yy;
		
		int x3 = xx;
		int y3 = (int)(y + ySpeed + this.mask_y + this.mask_height)/16;
		
		int x4 = (int)(x + xSpeed + this.mask_x + this.mask_width)/16;
		int y4 = (int)(y + ySpeed + this.mask_y + this.mask_height)/16;
		

		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e != this) {
				if(willCollide(this, e, xSpeed*2, ySpeed*2)) {
					if(e.collision) {
						return false;
					}
				}
			}
			
		}
		
		return !((World.tiles[x1 + (y1*World.WIDTH)] instanceof WallTile)  ||
				(World.tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(World.tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(World.tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size()-1).tile;
//				
				// System.out.println("target x = " + target.x + "/ Targety = " + target.y);
				
				if(x < target.x * 16 ) {
					x++;
				}else if(x > target.x * 16) {
					x--;
				}
				if(y < target.y * 16 ) {
					y++;
				}else if(y > target.y * 16) {
					y--;
				}
				
				if(x == target.x *16 && y== target.y * 16) {
					path.remove(path.size()-1);
				}
			}
		}
	}
	
	
	
	public void tick() {
		
	}
	
	public int getX() {
		return (int)x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public int getMask_x() {
		return mask_x;
	}


	public void setMask_x(int mask_x) {
		this.mask_x = mask_x;
	}


	public int getMask_y() {
		return mask_y;
	}


	public void setMask_y(int mask_y) {
		this.mask_y = mask_y;
	}


	public int getMask_width() {
		return mask_width;
	}


	public void setMask_width(int mask_width) {
		this.mask_width = mask_width;
	}


	public int getMask_height() {
		return mask_height;
	}


	public void setMask_height(int mask_height) {
		this.mask_height = mask_height;
	}
	
	
}

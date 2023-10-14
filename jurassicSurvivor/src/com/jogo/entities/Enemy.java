package com.jogo.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import com.jogo.main.Game;
import com.jogo.main.Sound;
import com.jogo.world.AStar;
import com.jogo.world.Camera;
import com.jogo.world.Node;
import com.jogo.world.Vector2i;
import com.jogo.world.World;

@SuppressWarnings("unused")
public class Enemy extends Entity{

	// Misto
	private double speed = 1;
	public int life = 100;
	private double lookingDistance;
	private int damage = 30;

	public int patrolDir = 0;
	public boolean isUp = true;
	
	
	// Controle de estados
	private boolean isAtacking;
	private boolean atack;
	private boolean moving = false;
	private boolean isRight = false;
	private boolean seekingPlayer = false;

	// controle de ataque
	private int atackDelay = 0;
	private int atackTime = 120;

	
	// imagens
	private static BufferedImage[] idleEnemy;
	private static BufferedImage[] walkingEnemy;
	private static BufferedImage[] atackingEnemy;
	
	// Controle de animação
	private int atackFrames;
	private int walkingFrames = 0;
	private int idleFrames;
	private int attackingFrames;
	
	
	private int animationDelay = 0;
	
	
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		// Definindo a máscara
		setMask_x(3);
		setMask_width(8);
		setMask_y(8);
		setMask_height(7);
		
		isRight = false;
		
		depth = 5;
		collision = true;
		idleEnemy = new BufferedImage[3];
		
		
		for(int i = 0; i <3; i++) {
			idleEnemy[i] = Game.velociraptor.getSprite(16 * i, 0, 16, 16);
		}
		walkingEnemy = new BufferedImage[3];
		for(int i = 0; i <3; i++) {
			walkingEnemy[i] = Game.velociraptor.getSprite(16 * i, 16, 16, 16);
		}
		atackingEnemy = new BufferedImage[3];
		for(int i = 0; i <3; i++) {
			atackingEnemy[i] = Game.velociraptor.getSprite(16 * i, 32, 16, 16);
		}
		
	
	}
	
	
	
	public void tick() {

		moving = false;

		if(life<=0) {
			Game.entities.remove(this);
			return;
		}

		int xx = (int)(Game.player.getX() - x);
		int yy = (int)(Game.player.getY() - y);
		
		if(!seekingPlayer) {
			speed = 0.5;
		}else{
			speed = 0.8;
		}
		
		
		
		if(patrolDir == 0) {
			
		}else if(patrolDir == 1) {
			
			if(!seekingPlayer) {
				if(isRight) {
					if(canMove(speed,0)) {
						moving = true;
						x+= speed;
					}else {
						isRight = false;
					}
				}else {
					if(canMove(speed*-1,0)) {
						moving = true;
						x-=speed;
					}else {
						isRight = true;
					}
				}
			}
		}else if(patrolDir == 2) {
			
			if(!seekingPlayer) {
				if(isUp) {
					if(canMove(0,speed*-1)) {
						moving = true;
						y-= speed;
					}else {
						isUp = false;
					}
				}else {
					if(canMove(0,speed)) {
						moving = true;
						y+=speed;
					}else {
						isUp = true;
					}
				}
			}
		}
		
		
		 
		// definindo como o inimigo irá perceber o jogador
		
	
		
		if(!seekingPlayer) {
			if(Game.player.isHidden) {
				lookingDistance = 15;
			}else {
				if(isRight) {
					if(Game.player.getX() +8 > getX() ) {
						
						if(canSee()) {
							lookingDistance = 75;
						}else {
							lookingDistance = 15;
						}
						
						
					}else {
						if(canSee()) {
							lookingDistance = 40;
						}else {
							lookingDistance = 15;
						}
					}
				}else{
					if(Game.player.getX()+8 < getX()) {
						
						if(canSee()) {
							lookingDistance = 75;
						}else {
							lookingDistance = 15;
						}
					}else {
						if(canSee()) {
							lookingDistance = 40;
						}else {
							lookingDistance = 15;
						}
					}
				}
				
			}
			
		}else {
			lookingDistance = 120;
		}
		
		if(this.calculateDistance(x, y, Game.player.x, Game.player.y) > 100 && seekingPlayer) {
			seekingPlayer = false;
			Game.player.atackingPostition.remove(this);
			
		}
		
		
		
		if(this.calculateDistance(x, y, Game.player.x, Game.player.y) < lookingDistance){
			seekingPlayer = true;
			
		}
		
		
		// Agora que o inimigo viu o jogador, tentamos chegar até uma área próxima a ele
		// Movimento do inimigo	

		
		if(seekingPlayer) {
			
			
			
			if(path == null || path.size() == 0 || Game.rand.nextInt(100)>90){

				Vector2i start = new Vector2i((int)x/16, (int)y/16);
				Vector2i end  = new Vector2i((int)(Game.player.getX())/16, (int)(Game.player.getY())/16);
								
				path = AStar.findPath(Game.world, start, end);

			}
			
			if(this.calculateDistance(x, y, Game.player.x, Game.player.y) > 20) {
				followPath(path, speed);
			
			}else {
				atackFormation();
			}	
		
			
		
			
			
		}
			

		
		// Animação
		
		
		// caso esteja atacando
		if(isAtacking) {
			animationDelay++;
			if(animationDelay > 7) {
				animationDelay = 0;
				if(atackFrames < 2) {
					atackFrames++;
				}else {
					// Se a animação de ataque vai até o fim
					// o Jogador recebe dano
					isAtacking = false;
					Sound.hit.play();
					Game.player.receiveDamage(damage);
					atackFrames= 0;
				}
			}
		}else if(!moving){
			animationDelay++;
			if(animationDelay > 7) {
				animationDelay = 0;
				if(idleFrames < 2) {
					idleFrames++;
				}else {
					idleFrames = 0;
				}
			}
		}else if(moving){
			animationDelay++;
			if(animationDelay > 7) {
				animationDelay = 0;
				if(walkingFrames < 2) {
					walkingFrames++;
				}else {
					walkingFrames = 0;
					double distance = calculateDistance(x, y, Game.player.x, Game.player.y);
					if(distance>140) {
						
								
					}else if(distance>110) {

						Sound.step.setVolume(-25);
						Sound.step.play();
						
					}else if(distance>80) {

						Sound.step.setVolume(-22);
						Sound.step.play();
						
					}else if(distance>65) {

						Sound.step.setVolume(-20);
						Sound.step.play();
						
					}else if(distance>40) {

						Sound.step.setVolume(-18);
						Sound.step.play();
						
					}else {
						Sound.step.setVolume(-15);
						Sound.step.play();
					}
					
					
				}
			}
		}
		
		
		
	}
	
	
	

	private boolean canAtack() {
		
			if(getY() < Game.player.getY()+3 && 
					getY() > Game.player.getY()-3) {
				if(Game.player.getX() - 8 > this.getX() && 
						this.getX() >= Game.player.getX() - 16) {
					// System.out.println("Pode atacar");
					isRight = true;
					return true;
				}
			
				if(this.getX() <= Game.player.getX() + 16 && 
						Game.player.getX() + 9 <= this.getX()) {
					// System.out.println("Pode atacar");
					isRight = false;
					return true;
					
				}
		}
			
		
		return false;

	}
	
	
	
	public void followPath(List<Node> path, double speed) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size()-1).tile;
				
				if(getX() < target.x * 16 && canMove(speed, 0)) {
					
					if((getX() + speed) >= target.x *16) {
						x = target.x *16;
					}else {
						x+=speed;
					}
					
					isRight = true;
					moving = true;
					
				}else if(getX() > target.x * 16 && canMove(speed*-1,0)) {
					if((getX() - speed) <= target.x *16) {
						x = target.x * 16;
					}else {
						x-=speed;
					}
					isRight = false;
					moving = true;
					
				}
				if(getY() < target.y * 16 && canMove(0, speed)) {
					if((getY() + speed) >= target.y *16) {
						y = target.y *16;
					}else {
						y+=speed;
					}
					moving = true;
					
				}else if(getY() > target.y * 16 && canMove(0, speed*-1)) {
					if((getY() - speed) <= target.y *16) {
						y = target.y * 16;
					}else {
						y-=speed;
					}
					moving = true;
					
				}
				
				if(getX() == target.x *16 && getY() == target.y * 16) {
					path.remove(path.size()-1);
				}
			}
				
		}
	}
	
	
	public void atackFormation() {
		
		
		
		if(calculateDistance(x, y, Game.player.x, Game.player.y) < 40) {
			if(!Game.player.atackingPostition.contains(this)) {
				Game.player.atackingPostition.add(this);
				path = null;
			}
		}else {
			Game.player.atackingPostition.remove(this);
		}
		
		int pos = Game.player.atackingPostition.indexOf(this);
		
		int target_x = (int)Game.player.x;
		int target_y = (int)Game.player.y;
		
		if(pos == 0) {
			target_x = (int)Game.player.x + 13;			
			target_y = (int)Game.player.y;
		}else if(pos == 1) {
			target_x = (int)Game.player.x - 13;			
			target_y = (int)Game.player.y;
		}else if(pos == 2) {
			target_x = (int)Game.player.x;			
			target_y = (int)Game.player.y +12;
		}else if(pos == 3) {
			target_x = (int)Game.player.x;			
			target_y = (int)Game.player.y - 12;
		}else if(pos == 4) {
				target_x = (int)Game.player.x + 20;	
				target_x = (int)Game.player.x - 20;	
		}else if(pos == 5) {
			target_x = (int)Game.player.x + 20;	
			target_x = (int)Game.player.x + 20;
		}else if(pos == 6) {
			target_x = (int)Game.player.x - 20;	
			target_x = (int)Game.player.x + 20;	

		}else if(pos == 7) {
			target_x = (int)Game.player.x - 20;	
			target_x = (int)Game.player.x - 20;	

		}
	
		if(atackDelay < atackTime) {
			this.atackDelay++;
		}
		
		
		if(canAtack()) {		
			if(atackDelay == atackTime) {
				atackDelay = 0;
				isAtacking = true;
				
			}
			moving = false;
		}
		
		
		if(!isAtacking && !canAtack()){
			double player_x = Game.player.x;
			if(getX() < target_x && canMove(speed, 0)) {	
				if(x < player_x -16) {
					isRight = true;
				}
				moving = true;
				if(!(x+speed > target_x)) {
					x+=speed;	
				}
					
				if(getY() < target_y && canMove(0, speed)) {
					moving = true;
					if(!(y + speed > target_y)) {
						y+=speed;
					}
					
				}else if(getY() > target_y && canMove(0, speed*-1)) {
					moving = true;
					if(!(y - speed < target_y)) {
						
						y-=speed;
					}
				}
			}else if(getX() < target_x && !canMove(speed, 0) && getY() <= target_y && canMove(0, speed) ) {	
				moving = true;
				if(!(y + speed > target_y)) {
					
					y+=speed;
				}			
			}else if(getX() < target_x && !canMove(speed, 0) && getY() > target_y && canMove(0, speed *-1)) {
				moving = true;
				if(!(y - speed < target_y)) {
					
					y-=speed;
				}
				
			}else if(getX() > target_x && canMove(speed*-1,0)) {
				if(x < player_x +16) {
					isRight = false;
				}
				moving = true;
				if(!(x-speed < target_x)) {
					x-=speed;	
				}
				if(getY() < target_y && canMove(0, speed)) {
					moving = true;
					if(!(y + speed > target_y)) {
						y+=speed;
					}
				}else if(getY() > target_y && canMove(0, speed*-1)) {
					moving = true;
					if(!(y - speed < target_y)) {
						y-=speed;
					}
				}
			}else if(getX() > target_x && !canMove(speed *-1, 0) && getY() >= target_y && canMove(0, speed*-1)) {
				moving = true;
				if(!(y - speed < target_y)) {
					y-=speed;
				}			
			}else if(getX() > target_x && !canMove(speed *-1, 0) && getY() < target_y && canMove(0, speed)) {
				moving = true;
				if(!(y + speed > target_y)) {
					y+=speed;
				}		
			}else if(getY() < target_y && canMove(0, speed)) {
				moving = true;
				if(!(y + speed > target_y)) {
					y+=speed;
				}
			}else if(getY() > target_y && canMove(0, speed*-1)) {
				moving = true;
				if(!(y - speed < target_y)) {
					y-=speed;
				}
			}
		}
	}
	

	public void render(Graphics g) {
		
		//super.debugRender(g);
		
		boolean showDistance = false;
		

		if(showDistance) {
			g.setColor(Color.red);
			g.drawOval(getX() +8- Camera.x -(int)lookingDistance, getY()+8 - Camera.y- (int)lookingDistance, (int)lookingDistance*2, (int)lookingDistance*2);
		}
	
		
		if(moving) {
			if(isRight) {
				g.drawImage(walkingEnemy[walkingFrames], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else {
				g.drawImage(walkingEnemy[walkingFrames], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
			}
		}else {
			if(isAtacking) {
				if(isRight) {
					g.drawImage(atackingEnemy[atackFrames], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(atackingEnemy[atackFrames], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
				}
			}else {
				if(isRight) {
					g.drawImage(idleEnemy[idleFrames], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}else {
					g.drawImage(idleEnemy[idleFrames], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
				}
			}
			
		}
		
	}
	
	public boolean canSee() {
		
		boolean cansee = false;
		double px = Game.player.x + 8;
		double py = Game.player.y + 8;
		
		double distance = calculateDistance(x+8, y+8, px,py);
		
		double dx = px - x;
		double dy = py - y;
		
	
		for(int i = 0; i < distance; i+= 5) {
			
			double stepsX = (dx/distance) * i;
			double stepsY = (dy/distance) * i;
			
			if(canMove(stepsX,stepsY)) {
				
				cansee =  true;
			}else {
				// System.out.println("Não Está vendo");
				return false;
			}
					
			
		}
		// System.out.println("Está vendo");
		return cansee;
	}
	
	
	public void setDamage(int i) {
		life -= i;
	}
	

}

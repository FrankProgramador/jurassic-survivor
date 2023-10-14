package com.jogo.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jogo.main.Game;
import com.jogo.main.Sound;
import com.jogo.world.Camera;
import com.jogo.world.World;

@SuppressWarnings("unused")
public class Player extends Entity{
	
	public boolean right, left, up, down;
	public boolean isHidden = false;
	
	
	public boolean isRunning = false;
	public int runUpCount = 0;
	public int runDownCount = 0;
	public int runLeftCount = 0;
	public int runRightCount = 0;
	public boolean canRun = false;
	public double stamina = 100;
	
	public boolean runRight = false;
	public boolean runLeft = false;
	public boolean runUp = false;
	public boolean runDown = false;
	
	private double maxSpeed = 1.4;
	private double normalSpeed = 0.7;
	private double slowedSpeed = 0.3;
	
	public double speed = 1;
	public boolean isRight = true;

	private int frames = 0;
	private int maxFrames = 10;
	
	private int index;
	private int maxIndex = 3;
	
	public Rectangle actionMask;
	public ArrayList<Enemy> atackingPostition;
	
	private boolean moving = false;
	private BufferedImage[] playerWalking;
	private BufferedImage[] playerRunning;
	private BufferedImage[] playerEating;
	private BufferedImage[] playerDrinking;
	private BufferedImage[] playerIddle;
	private BufferedImage[] playerAtacking;
	
	public boolean isEating = false;
	private boolean isDrinking = false;
	
	public boolean eat = false;
	public int eatIndex = 0;
	public int eatFrames = 0;
	
	public int waterConsumed = 0;
	public int foodConsumed = 0;
	
	public boolean canDrink = false;
	public boolean canEat = false;
	private int indexDrinking;
	
	public boolean atack = false;
	public boolean isAtacking = false;

	
	private int atackFrames;
	private int atackIndex;
	
	private int atackDelay = 0;
	
	private boolean isHit = false;
	private BufferedImage[] damagedPlayer;
	private int indexHit = 0;
	
	public boolean isSick = false;
	public int sickFrames = 0;
	public int pukeFrames = 0;
	public int pukeIndex = 5;
	public boolean puke = false;
	
	
	// Variáveis do jogador
	private double life;
	private double maxLife;
	private int hunger = 7;
	private int maxHunger = 7;
	private int hungerDelay = 400;
	private int hungerCount = 0;
	
	private int thirst = 7;
	private int maxThirst = 7;
	private int thirstDelay = 700;
	private int thirstCount = 0;

	public int actionMask_x = 3;
	public int actionMask_y = 7;
	public int actionMask_width = 10;
	public int actionMask_height = 4;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		
		
		
		atackingPostition = new ArrayList<Enemy>();
		actionMask = new Rectangle(actionMask_x, actionMask_y, actionMask_width, actionMask_height);
		depth = 5;
		collision = true;
		
		this.setMask_x(2);
		this.setMask_y(8);
		this.setMask_height(7);
		this.setMask_width(10);

		setLife(100);
		maxLife = 100;
		
		playerWalking = new BufferedImage[4];
		
		for(int i = 0; i <4; i++) {
			playerWalking[i] = Game.spritesheet.getSprite(16 * i, 16, 16, 16);
		}
		
		playerRunning = new BufferedImage[4];
		
		for(int i = 0; i <4; i++) {
			playerRunning[i] = Game.spritesheet.getSprite(16 * i, 0, 16, 16);
		}
		
		playerAtacking = new BufferedImage[4];
		
		for(int i = 0; i <4; i++) {
			playerAtacking[i] = Game.spritesheet.getSprite(16 * i, 16*2, 16, 16);
		}
		
		playerEating = new BufferedImage[4];
		
		for(int i = 0; i <4; i++) {
			playerEating[i] = Game.spritesheet.getSprite(16 * i, 16*6, 16, 16);
		}
		
		playerIddle = new BufferedImage[4];
		
		for(int i = 0; i <4; i++) {
			playerIddle[i] = Game.spritesheet.getSprite(16 * i, 16*5, 16, 16);
		}
		
		playerDrinking = new BufferedImage[8];
		
		for(int i = 0; i <6; i++) {
			playerDrinking[i] = Game.spritesheet.getSprite(16 * i, 16*7, 16, 16);
		}
		
		
		
		damagedPlayer = new BufferedImage[2];
		damagedPlayer[0] = Game.spritesheet.getSprite(16 * 2, 16 * 3, 16, 16);
		damagedPlayer[1] = Game.spritesheet.getSprite(16 * 3, 16 * 3, 16, 16);
		
		
		
	}
	
	
	public void tick() {
		
		boolean testMode = true;
		
		canEat = false;
		canDrink = false;
		isHidden = false;
	
		
		if(!isSick && hunger > 5 && thirst > 5 && life < 100) {
			if(Game.rand.nextInt(100) > 98) {
				life++;
			}
		}
		
		if(isSick) {
			sickFrames++;
			if(sickFrames > 400) {
				sickFrames= 0;
				hunger--;
				thirst--;
				life -= 10;
				// fazer vomitar
				puke = true;
			}
			if(hunger <=0 ) {
				hunger = 0;
				isSick = false;
				
			}
		}
		
		if(puke) {
			pukeFrames++;
			if(pukeFrames>10) {
				pukeIndex--;
				pukeFrames = 0;
			}
			
			if(pukeIndex == 2) {
			
				if(isRight) {
					Vomit vomit = new Vomit(getX() + 8, getY() + 3, 16, 16, null);
					Game.entities.add(vomit);
				}else {
					Vomit vomit = new Vomit(getX() - 6, getY() + 3, 16, 16, null);
					Game.entities.add(vomit);
				}
				
			}
			if(pukeIndex == 0) {
				pukeIndex = 5;
				puke = false;
				
			}
		}
		
		int runDelay = 7;
		
		
		if(stamina > 10) {
			if(runRight && !left && !up && !down) {
				runRightCount++;
				if(runRightCount < runDelay) {
					canRun = true;	
				}else {				
					canRun = false;
					runRight = false;
				}
			}else if(runLeft && !right && !up && !down) {
				runLeftCount++;
				if(runLeftCount < runDelay) {
					canRun = true;	
				}else {
					canRun = false;
					runLeft = false;
				}
			}else if(runUp && !left && !right && !down) {
				runUpCount++;
				if(runUpCount < runDelay) {
					canRun = true;	
				}else {
					canRun = false;
					runUp = false;
				}
			}else if(runDown && !left && !right && !up) {
				runDownCount++;
				if(runDownCount < runDelay) {
					canRun = true;			
				}else {
					canRun = false;
					runDown = false;
				}
			}
		}
		
		
		
		
		if(isRunning) {
			speed = maxSpeed;
			if(stamina <= 0) {
				isRunning = false;
			}

		}else {
			speed = normalSpeed;
		}
		
		if(isHidden) {
			speed = slowedSpeed;
		}
		
		checkHunger();
		checkThirst();
		
		if(isRunning) {
			if(thirst < 3) {
				stamina -= 0.2;
			}
			if(hunger <3) {
				stamina -= 0.2;
			}
			stamina -= 0.5;
			
		}else if(stamina < 100) {
			stamina += 0.1;
			
			if(thirst < 3) {
				stamina -= 0.05;
			}
			if(hunger <3) {
				stamina -= 0.05;
			}
		}
	
		
		
		if(!right || !left || !up || !down || isAtacking) {
			moving = false;
		}
		
		
		if(!isEating && !isDrinking && !isAtacking && !puke) {
			
			
			
			if((right || left || up || down)) {
				if(isRunning) {
					speed = maxSpeed;
					thirstCount+=4;
					hungerCount++;
					
				}
			}else {
				isRunning = false;
				speed = normalSpeed;
				
			}
			
			if(right && canMove(speed, 0)) {
				moving = true;
				isRight = true;
				
				x+=speed;
				
			}else if(left && canMove(speed*-1, 0)) {
				moving = true;
				isRight = false;
				
				x-=speed;
			}
			
			if(up && canMove(0, speed*-1)) {
				moving = true;
				y-=speed;
			}else if(down && canMove(0, speed)) {
				moving = true;
				y+=speed;
			}
			
		}
		
		if(eat) {
			
				
			eat = false;
			isEating = true;
		}
		
		// animações
		
		if(moving) {			
			frames++;
			if(frames>=maxFrames) {
				frames = 0;
				index++;
			
				if(index > maxIndex) {
					index = 0;
					Sound.step.setVolume(-15);
					Sound.step.play();
				}
			}
		}
		if(isRunning) {			
			frames++;
			if(frames>=maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex/2) {

					Sound.step.setVolume(-15);
					Sound.step.play();
				}
				if(index > maxIndex) {
					index = 0;
					
				}
			}
		}
	
		if(!moving && !isDrinking && !isEating ) {			
			frames++;
			if(frames>=maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
					
				}
			}
		}
		
		
		if(isEating) {	
			
			eatFrames++;
			
			if(eatFrames>=maxFrames) {
				eatFrames = 0;
				eatIndex++;
				if(eatIndex > maxIndex) {
					eatIndex = 0;
					isEating = false;
					hunger++;
					foodConsumed++;
					Sound.eat.play();
					for(int i = 0; i<Game.entities.size(); i++) {
						Entity e = Game.entities.get(i);
						if(e instanceof Food) {
							if(isColliding(actionMask, e)){
								((Food) e).life--;
								
								
							}
						}
					}
				}
			}
		}
		
		if(isDrinking) {
			frames++;
			if(frames>=maxFrames) {
				frames = 0;
				indexDrinking++;
				
				if(indexDrinking > 5) {
					indexDrinking = 0;
					Sound.drink.play();
					thirst++;
					waterConsumed++;
					if(thirst > maxThirst) {
						thirst = maxThirst;
					}
				}
			}
		}
		

		
		
		
		
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/4), 0, World.WIDTH*16 - Game.WIDTH/2);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/4), 0, World.HEIGHT*16 - Game.HEIGHT/2);
//		Camera.x =this.getX() - (Game.WIDTH / 4);
//		Camera.y = this.getY() - (Game.HEIGHT/ 4);


		onCollision();
		
		if(this.getLife() <= 0) {
			Game.gameState = "GAME_OVER";
			Sound.game_over.setVolume(-20);
			Sound.game_over.play();
		}
		if(life > 100) {
			life = 100;
		}
		
		if(hunger > 7) {
			hunger = 7;
		}
		
		if(thirst > 7) {
			thirst = 7;
		}
		

		
	}
	
	
	
	private void checkHunger() {
		hungerCount++;
		
		if(hungerCount > hungerDelay) {
			hungerCount = 0;
			if(hunger>0) {
				hunger--;
			}else {
				life-=20;
			}
		}
	}
	
	private void checkThirst() {
		thirstCount++;
		
		if(thirstCount > thirstDelay) {
			thirstCount = 0;
			if(thirst>0) {
				thirst--;
			}else {
				life-=20;
			}
		}
	}
	
	public void render(Graphics g) {
		
		
		if(!puke) {
		if(isHit) {
			g.drawImage(damagedPlayer[indexHit], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		if(isRunning && isRight) {
			g.drawImage(playerRunning[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(isRunning && !isRight) {
			g.drawImage(playerRunning[index], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		
		
		if(!isAtacking && !moving && !isHit && !isEating && !isDrinking && isRight) {
			g.drawImage(playerIddle[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			
		}
		
		if(!isAtacking && !moving && !isHit && !isEating && !isDrinking && !isRight) {
			g.drawImage(playerIddle[index], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		
		
		if(isRight && !isHit && !isEating && !isDrinking && moving && !isRunning) {
			g.drawImage(playerWalking[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

		}
		if(!isRight && !isHit && !isEating && !isDrinking && moving && !isRunning) {

			g.drawImage(playerWalking[index], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);

		}
		
		if(isEating && isRight) {
			g.drawImage(playerEating[eatIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		if(isEating && !isRight) {
			g.drawImage(playerEating[eatIndex], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		
		if(isDrinking && isRight) {
			g.drawImage(playerDrinking[indexDrinking], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		if(isDrinking && !isRight) {
			g.drawImage(playerDrinking[indexDrinking], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		
		if(isAtacking && isRight) {
			g.drawImage(playerAtacking[2], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(isAtacking && !isRight) {
			g.drawImage(playerAtacking[2], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		}
		if(puke && isRight) {
			g.drawImage(playerDrinking[pukeIndex], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		if(puke && !isRight) {
			g.drawImage(playerDrinking[pukeIndex], this.getX() - Camera.x + 16, this.getY() - Camera.y, -16, 16, null);
		}
		
		actionMask.x= (int) (this.x );
		actionMask.y = (int) (this.y+ 13) ;
		actionMask.width = 3;
		actionMask.height = 4;
		
		if(isRight) {
			actionMask.x = (int) (this.x + 13);
		}      
		actionMask_x= (int) (this.x );
		actionMask_y = (int) (this.y+ 13) ;
		actionMask_width = 3;
		actionMask_height = 4;
		
		if(isRight) {
			actionMask_x = (int) (this.x + 13);
		}
		// g.drawRect(actionMask.x - Camera.x, actionMask.y- Camera.y, actionMask.width, actionMask.height);
		// super.debugRender(g);
		
//		if(isHidden) {
//			g.drawString("isHidden", getX() - Camera.x, getY() - Camera.y);
//		}
		 
	}
	
	
	public void onCollision() {
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Food) {
				if(isColliding(actionMask, e)){
					
					if(((Food) e).canBeEated()) {
						canEat = true;
					}else {
						canEat = false;
					}

					
				
				}
			}else if(e instanceof Water) {
				if(isColliding(actionMask, e)){
					canDrink = true;
				}
			}else if(e instanceof Turf) {
				if(isColliding(this, e)){
					if(y > e.y - 3) {
						isHidden = true;
					}
						
					
					
				}
			}
		}
	}
	
	public int getHunger() {
		return hunger;
	}
	
	
	public int getThirst() {
		return thirst;
	}

	public double getLife() {
		return life;
	}


	public void setLife(int life) {
		this.life = life;
	}
	
	public void receiveDamage(int damage) {
		this.life -= damage;
	}
	
	public void setDamage(int damage) {
		this.life = life - damage;
		if(life<0) {
			life = 0;
		}
	}
	
	public void setHunger() {
		if(hunger < maxHunger) {
			hunger++;
			foodConsumed++;
			if(hunger>maxHunger) {
				hunger = maxHunger;
			}
		}
	}


	
	

	public double getMaxLife() {
		return maxLife;
	}


	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}


	public void hit() {
		isHit = true;
	}
	
	public void isEating() {
		isEating = true;
	}
	
	public void isNotEating() {
		isEating = false;
	}
	
	public void isDrinking() {
		isDrinking = true;
	}
	
	public void isNotDrinking() {
		isDrinking = false;
	}
}

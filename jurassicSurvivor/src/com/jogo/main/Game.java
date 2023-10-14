package com.jogo.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jogo.entities.Entity;
import com.jogo.entities.ExitArea;
import com.jogo.entities.Player;
import com.jogo.graphics.LevelMenuUi;
import com.jogo.graphics.MenuUI;
import com.jogo.graphics.Spritesheet;
import com.jogo.graphics.UI;

import com.jogo.world.Level;
import com.jogo.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	public static JFrame frame;
	
	private Thread thread;
	private Boolean isRunning = true;
	
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	public static int currentLevel = 1;

	
	private BufferedImage image;
	public static Random rand;
	public static List<Entity> entities;


	public static Spritesheet spritesheet;
	public static Spritesheet grass = new Spritesheet("/grass.png");
	public static Spritesheet enviroments= new Spritesheet("/enviromens.png");
	public static Spritesheet hunger;
	public static Spritesheet thirst;
	public static Spritesheet velociraptor;
	

	public static Player player;	
	private static UI ui;
	private MenuUI menu;
	
	public static String gameState = "MENU";
	
	public static World world;
	
	public static boolean gameInProgress = false;
	public static boolean saveGame = false;
	
	public static int timeOfDay = 0;

	public static int[] pixels;
	private int[] lightMapPixels;
	
	public static BufferedImage miniMap;
	public static int[] miniMapPixels;
	
	private Image jeraGames;

	
	private int sceneFrames = 0;
	
	public static List<Level> levels;
	
	public LevelMenuUi levelMenuUI;
	
	
	public static Comparator<Entity> depthSorter = new Comparator<Entity>() {
			
		@Override
		public int compare(Entity e0, Entity e1) {
			if(e0.depth < e1.depth) {
				return -1;
			}else if(e0.depth > e1.depth) {
				return +1;
			}else if(e0.getY() < e1.getY()) {
				return -1;
			}else if(e0.getY() > e1.getY()) {
				return +1;
			}
			
			return 0;
		}
		
	};
		
	
	public Game() {
		
		addKeyListener(this);

		// this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); // Janela
		setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize())); // fullScreen
		InitFrame();
		
		
		
		
		// inicializando objetos
		rand = new Random();
		image = new BufferedImage(WIDTH ,HEIGHT ,BufferedImage.TYPE_INT_RGB);
		
		try {
			jeraGames = ImageIO.read(getClass().getResource("/JeraGames.png"));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		levels = new ArrayList<Level>();
		
		Level level1 = new Level("/level1.png", "Fase 01");
		
		level1.introMessage = "Use as setas para se mover"
				 + "\nSe esconda nos arbustos para conseguir avan�ar."
				 + "\nUse dois toques em uma dire��o para correr"
				 + "\nN�o se esque�a de beber e comer, usando a barra de espa�o";
		
		Level level2 = new Level("/level2.png", "Fase 02");
		
		level2.introMessage = "Voc� pode morrer de fome e de sede"
				 + "\nSe um predador for te atacar, corra!"
				 + "\nOs arbustos s�o importantes para conseguir avan�ar";
		
		
		Level level3 = new Level("/level3.png", "Fase 03");
		
		level3.introMessage = "Essa regi�o � um labirinto repleto de perigos,"
				 + "\ndesvie os inimigos e n�o esque�a de comer e beber pra"
				 + "\ncontinuar vivo!";
		
		Level level4 = new Level("/level4.png", "Fase 04");
		
		level4.introMessage = "N�o existe comida nem bebida nesta regi�o,"
				 + "\nVoc� deve se apressar, mas sem se descuidar.";
		
		Level level5 = new Level("/level5.png", "Fase 05");
		
		level5.introMessage = "Algumas frutas podem te deixar doente"
				 + "\nEm caso de v�mitos, beba muita �gua e "
				 + "\nespere o est�mago esvaziar pra voltar a comer";
		
		Level level6 = new Level("/level6.png", "Fase 06");
		
		level6.introMessage = "Os inimigos sempre v�o tentar te cercar"
				 + "\nAs plantas crescem novamente depois de um tempo"
				 + "\nAs fases n�o tem limite de tempo";
		
		Level level7 = new Level("/level7.png", "Fase 07");
		
		level7.introMessage = "Pra correr voc� gasta stamina"
				 + "\nSe estiver sem fome e sem sede a stamina e"
				 + "\na sua vida se recuperam aos poucos";

		Level level8 = new Level("/level8.png", "Fase 08");
		
		level8.introMessage = "Aqui j� esplicamos o jogo"
				 + "\nAgrade�o por jogar at� aqui"
				 + "\nManda um feedback";
		
		Level level9 = new Level("/level9.png", "Fase 09");
		
		level9.introMessage = "Essa � uma vers�o alpha e voc� foi longe"
				 + "\nSe tiver alguma ideia n�o esque�a de me enviar"
				 + "\nBoa sorte";
		
		Level level10 = new Level("/level10.png", "Fase 10");
		
		level10.introMessage = "Aqui � a �ltima fase, voc� � um her�i"
				 + "\nVoc� � um sobrevivente nato!"
				 + "\nVoc� consegue!";
	
		levels.add(level1);
		levels.add(level2);
		levels.add(level3);
		levels.add(level4);
		levels.add(level5);
		levels.add(level6);
		levels.add(level7);
		levels.add(level8);
		levels.add(level9);
		levels.add(level10);
		

		
		
		levelMenuUI = new LevelMenuUi();
		entities = new ArrayList<Entity>();

		
		
		// * Definindo sprites
		spritesheet = new Spritesheet("/psitacosaurusSheet.png");
		grass = new Spritesheet("/grass.png");
		enviroments = new Spritesheet("/enviromens.png");
		hunger = new Spritesheet("/hunger.png");
		thirst = new Spritesheet("/thirst.png");
		velociraptor = new Spritesheet("/velociraptor.png");
		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));			
		entities.add(player);
		
		currentLevel = 9;

		
		ui = new UI();
		menu = new MenuUI();
		
		world = new World(levels.get(currentLevel).world);

		miniMap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		miniMapPixels = ((DataBufferInt)miniMap.getRaster().getDataBuffer()).getData();
		
	}
	
	public void InitFrame() {
		
		frame = new JFrame("Jurassic Survivor");
		frame.add(this);
		
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		
		//�cone
		try {
			Image icon = ImageIO.read((getClass().getResource("/icon.png")));
			frame.setIconImage(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Cursor
		/*
		 * Toolkit toolKit = Toolkit.getDefaultToolkit(); Image cursor =
		 * toolKit.getImage(getClass().getResource("/cursor01.png")); Cursor c =
		 * toolKit.createCustomCursor(cursor, new Point(0,0), "cursor");
		 * frame.setCursor(c);
		 */		
		
		/*
		 * Image cursorImage = Toolkit.getDefaultToolkit().getImage("xparent.gif");
		 * Cursor blankCursor =
		 * Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point( 0, 0),
		 * "" ); setCursor( blankCursor );
		 */
		
	
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		isRunning=false;
		try {
			thread.join();
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
	

		levelController();

		if(gameState == "NORMAL") {
			
			Sound.music.setVolume(-30);
			Sound.music.loop();
			
			
			for(int i = 0; i< entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
	
		}else if(gameState == "GAME_OVER"){
			
			
		}else if(gameState == "MENU"){
			menu.tick();
		}else if(gameState == "LEVEL_MENU"){
			levelMenuUI.tick();
		}else if(gameState == "LEVEL_MESSAGE"){
			menu.tick();
		}
		
		
	}
	
	public void render() {
		
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		
		// *** Rendenizando o jogo **		
		
		world.render(g);
		
		
		
		Collections.sort(entities, depthSorter);
		for(int i = 0; i< entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		// applyLight();
		// dia e noite
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0,timeOfDay/4));
				g2.fillRect(0, 0, Game.WIDTH*Game.SCALE , Game.HEIGHT*Game.SCALE);
		
		
		g = bs.getDrawGraphics();
		
		
		
		// g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width*SCALE, Toolkit.getDefaultToolkit().getScreenSize().height*SCALE, null);
		
		ui.render(g);
		
		if(gameState == "INTRO") {
			sceneFrames++;
			if(sceneFrames < 240) {
				g.drawImage(jeraGames, 0,0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
			}else {
				sceneFrames = 0;
				gameState = "MENU";
			}

		}
		
		// World.renderMiniMap(g);
		
		if(gameState == "GAME_OVER") {
			ui.gameOverUI(g);
		}
		
		if(gameState == "CONGRATZ") {
			ui.congratzUI(g);
		}
		
		if(gameState == "LEVEL_MESSAGE") {
			ui.levelMessage(g);
		}
		
		if(gameState == "MENU") {
			menu.render(g);
		}
		
		if(gameState == "LEVEL_MENU") {
			levelMenuUI.render(g);
		}
		
		
		g.dispose();
		bs.show();
	}
	
	
	

	public void applyLight() {
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightMapPixels[xx + (yy*Game.WIDTH)] != 0xff000000) {
					pixels[xx + (yy*Game.WIDTH)] = 0;
				}
			}
		}
	}

	
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0.0;
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1 ) {
				tick();
				render();
				delta --;
			}
			
		
		}

		stop();
	}
	
	

	
	
	
	public static void restart() {
		

		entities = new ArrayList<Entity>();

		
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));			
		entities.add(player);
		
		try {
			ui = new UI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		world = new World(levels.get(currentLevel).world);

		gameState = "LEVEL_MESSAGE";
	}
	
	private void levelController() {
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e instanceof ExitArea) {
				if(Entity.isColliding(player, e)) {
					
					levels.get(currentLevel).cleared = true;
					if(currentLevel == levels.size()-1) {
						gameState = "CONGRATZ";
					}else {			
						currentLevel++;
						String[] opt1 = {"level"};
						int[] opt2 = {Game.currentLevel};
						menu.saveGame(opt1, opt2, 10);
						Sound.level_cleared.play();
						restart();
					}
											
					
				}
			}
		}
		
		
	}
	

	
	
	
	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

		
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(gameState == "NORMAL") {
			
			if(e.getKeyCode() == KeyEvent.VK_U) {
				if(player.isSick) {
					player.isSick = false;
				}else {
					player.isSick = true;
				}
			}
			

			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
				player.right = true;	
				
				if(player.canRun) {
					player.isRunning = true;
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
				if(player.canRun) {
					player.isRunning = true;
				}
				
			}
			

			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
				if(player.canRun) {
					player.isRunning = true;
				}
				
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
				if(player.canRun) {
					player.isRunning = true;
				}
				
			}

			
			if(e.getKeyCode() == KeyEvent.VK_SPACE && player.canEat) {
				if(!player.isEating) {
					player.eat = true;
				}
			}
			
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE && player.canDrink) {
				player.isDrinking();
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState = "MENU";
			}
			
			
			
		}else if(gameState == "GAME_OVER") {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				Sound.eat.play();
				restart();
			}
		}else if(gameState == "LEVEL_MESSAGE") {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				Sound.eat.play();
				gameState = "NORMAL";
			}
		}else if(gameState == "MENU") {
			if(e.getKeyCode() == KeyEvent.VK_UP) {

				menu.setUp();
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {

				menu.setDown();
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				
				menu.setEnter();
			}
			
		}else if(gameState == "LEVEL_MENU") {
			if(e.getKeyCode() == KeyEvent.VK_UP) {

				levelMenuUI.up = true;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN) {

				levelMenuUI.down = true;
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER) {

				levelMenuUI.enter = true;
			}
			
		}else if(gameState == "INTRO") {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				Sound.eat.play();
				gameState = "MENU";
			}
			
		}
		
		 if(gameState == "CONGRATZ") {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					Sound.eat.play();
					currentLevel = 0;
					restart();
					gameState = "MENU";
				}
		 }
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		

		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			
			player.right = false;
			
			if(!player.isRunning&& !player.left && !player.up && !player.down) {
				
				player.canRun = false;
				player.runRightCount = 0;
				player.runRight = true;
			}
			
			
			
				
			

		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
			
			if(!player.isRunning && !player.right && !player.up && !player.down) {
				
				player.canRun = false;
				player.runLeftCount = 0;
				player.runLeft = true;
			}
		}
		
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			
			if(!player.isRunning && !player.right && !player.left && !player.down) {
				player.canRun = false;
				player.runUpCount = 0;
				player.runUp = true;
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			
			if(!player.isRunning && !player.right && !player.up && !player.left) {
				
				player.canRun = false;
				player.runDownCount = 0;
				player.runDown = true;
			}
		}
		
//		if(e.getKeyCode() == KeyEvent.VK_Z) {
//			player.isAtacking = false;
//			
//		}

		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.isNotDrinking();
		}
		
		
	}

}

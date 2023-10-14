package com.jogo.world;



public class Level {


	public String introMessage;
	public String name;
	public boolean cleared;
	public String world;
	
	public Level(String levelPath, String name) {
		
		world = levelPath;
		this.name = name;
	}
	
}

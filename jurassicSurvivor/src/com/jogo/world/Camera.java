package com.jogo.world;

public class Camera {

	public static int x;
	public static int y;
	public static double speed = 0.5;
	
	public static int clamp(int posAtual, int posMin, int posMax) {
		
		if(posAtual < posMin) {
			posAtual = posMin;
		}

		if(posAtual > posMax) {
			posAtual = posMax;
		}
		return posAtual;
	}
	
	
}

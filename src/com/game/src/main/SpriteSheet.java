package com.game.src.main;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage sprS;
	
	public SpriteSheet(BufferedImage image){
		this.sprS = image;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height){
		BufferedImage img = sprS.getSubimage((col*32)-32, (row*32)-32, width, height);
		return img;
	}
	
}

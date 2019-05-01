package com.game.src.main;

import java.awt.image.BufferedImage;

public class Sprites {
	
//	public BufferedImage player, beam, alien;
	public BufferedImage[] player = new BufferedImage[4];
	public BufferedImage[] beam = new BufferedImage[3];
	public BufferedImage[] alien = new BufferedImage[3];
	public BufferedImage rock;
	
	private SpriteSheet ss;
	
	public Sprites(Game game){
		ss = new SpriteSheet(game.getSpriteSheet());
		
		getSprites();
	}
	
	private void getSprites(){
		rock = ss.grabImage(4, 1, 32, 32);
		
		player[0] = ss.grabImage(1,1,32,32);
		player[1] = ss.grabImage(1,2,32,32);
		player[2] = ss.grabImage(1,3,32,32);
		player[3] = ss.grabImage(1,4,32,32);
		
		beam[0] = ss.grabImage(2, 1, 32, 32);
		beam[1] = ss.grabImage(2, 2, 32, 32);
		beam[2] = ss.grabImage(2, 3, 32, 32);
		
		alien[0] = ss.grabImage(3, 1, 32, 32);
		alien[1] = ss.grabImage(3, 2, 32, 32);
		alien[2] = ss.grabImage(3, 3, 32, 32);
	}
	
}

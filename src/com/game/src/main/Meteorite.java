package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

public class Meteorite extends GameObject implements EntityB {
	
	Random rdm = new Random();
	private int speed = rdm.nextInt(3)+1;
	private Sprites spr;
	private Game game;
	private Controller contr;
	
	public Meteorite(double x, double y, Sprites spr, Controller contr, Game game){
		super(x,y);
		this.spr = spr;
		this.game = game;
		this. contr = contr;
	}
	
	//reminder: tick methods always needed if something is moving 
	public void tick(){
		y+=speed;
		
		if(y>Game.HEIGHT*Game.SCALE){
			speed = rdm.nextInt(3)+1;
			x=rdm.nextInt(640);
			y=-5;
		}
		
		for(int i=0; i<game.ea.size(); i++){
			EntityA tempEnt = game.ea.get(i);
			
			if(GamePhysics.collision(this, tempEnt)){
				contr.removeEntity(this);
				game.setRock_dead(game.getRock_dead()+1);
				game.setSCORE(game.getSCORE()+25);
			}
		}
	}
	
	public void render(Graphics g){
		g.drawImage(spr.rock, (int)x, (int)y, null); //offset is to move image if its out of place a little - like if you put in 10 = x-10
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public double getY(){
		return y;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
}

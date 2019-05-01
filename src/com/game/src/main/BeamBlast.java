package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.game.src.main.classes.EntityA;


public class BeamBlast extends GameObject implements EntityA {

	private Sprites spr;
	private Game game;
	ObjAnimation a;
	
	public BeamBlast(double x, double y, Sprites spr, Game game){
		super(x,y);
		this.spr = spr;
		this.game = game;
		a = new ObjAnimation(3, spr.beam[0], spr.beam[1], spr.beam[2]);
	}
	
	public void tick(){
		y-=6;
		
		a.runObjAnimation();
	}
	
	public void render(Graphics g){
		a.drawObjAnimation(g, x, y, 0);
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}

	public double getY(){
		return y;
	}
	
	public double getX(){
		return x;
	}
}

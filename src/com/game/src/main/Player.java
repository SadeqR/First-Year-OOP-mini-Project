package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

public class Player extends GameObject implements EntityA {

		private double velX = 0;
		private double velY= 0;
		ObjAnimation a;
		private Sprites spr;
		Game game;
		Controller contr;
		
		public Player (double x, double y, Sprites spr, Game game, Controller contr){
			super(x,y);
			this.spr = spr;
			this.game = game;
			this.contr = contr;
			a = new ObjAnimation(3, spr.player[0], spr.player[1], spr.player[2], spr.player[3]);
		}
		
		
		public void tick(){
			x+=velX;
			y+=velY; 
			
			//boundaries for spaceship in x axis
			if(x<=0){
				x=0;
			}
			
			if(x>=640-25){
				x=640-25;
			}
			
			//boundaries for spaceship in y axis
			if(y<=0){
				y=0;
			}
			
			if(y>=480-32){
				y=480-32;
			}
			
			for(int i=0; i < game.eb.size(); i++){
				EntityB tempE = game.eb.get(i);
				
				if(GamePhysics.collision(this, tempE)){
					contr.removeEntity(tempE);
					Game.HP -= 127; 
					game.setEnemy_dead(game.getEnemy_dead()+1);
					game.setEnemy_dead(game.getEnemy_dead()+1);
				}
			}
			
				a.runObjAnimation();
			
		}
		
		public void render(Graphics g){
			if (Game.moving) {
			a.drawObjAnimation(g, x, y, 0);
			} else{
			g.drawImage(spr.player[0], (int)x, (int)y, null);
			}
		}
		
		public Rectangle getBounds(){
			return new Rectangle((int)x, (int)y, 32, 32);
		}
		
		public double getX(){
			return x;
		}
		
		public double getY(){
			return y;
		}
		
		public void setX(double x){
			this.x = x;
		}
		
		public void setY(double y){
			this.y = y;
		}
		
		public void setVX(double vX){
			this.velX = vX;
		}
		
		public void setVY(double vY){
			this.velY = vY;
		}
}

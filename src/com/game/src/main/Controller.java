package com.game.src.main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;


public class Controller {
	
	private ArrayList<EntityA> ea = new ArrayList<>();
	private ArrayList<EntityB> eb = new ArrayList<>();
	
	EntityA enta;
	EntityB entb;
	
	private Sprites spr;
	Random rdm = new Random();
	private Game game;
	
	public Controller(Sprites spr, Game game){
		this.spr = spr;
		this.game = game;
	}
	
	public void addEnemy(int enemy_count){	
		for(int i=0; i<enemy_count; i++){
			addEntity(new Enemy(rdm.nextInt(640),-10,spr, this, game));
		}
	}
	
	public void addRock(int enemy_count){	
		for(int i=0; i<enemy_count; i++){
			addEntity(new Meteorite(rdm.nextInt(640),-10,spr, this, game));
		}
	}
	
	public void tick(){
		//A
		for(int i=0; i < ea.size(); i++){
			enta = ea.get(i);		
			enta.tick();
		}
		//B
		for(int i=0; i < eb.size(); i++){
			entb = eb.get(i);	
			entb.tick();
		}
	}
	
	public void render(Graphics g){
		//A
		for(int i=0; i < ea.size(); i++){	
			enta = ea.get(i);
					
			enta.render(g);
		}
		//b
		for(int i=0; i < eb.size(); i++){	
			entb = eb.get(i);
					
			entb.render(g);
		}
	}
	
	public void addEntity(EntityA block){
		ea.add(block);
	}
	
	public void removeEntity(EntityA block){
		ea.remove(block);
	}
	
	public void addEntity(EntityB block){
		eb.add(block);
	}
	
	public void removeEntity(EntityB block){
		eb.remove(block);
	}
	
	public ArrayList<EntityA> getEntityA(){
		return ea;
	}
	
	public ArrayList<EntityB> getEntityB(){
		return eb;
	}	
}
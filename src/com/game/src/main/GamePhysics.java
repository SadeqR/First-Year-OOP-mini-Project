package com.game.src.main;

import java.util.ArrayList;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

public class GamePhysics {
	
	public static boolean collision(EntityA enta, EntityB entb){
		
		if(enta.getBounds().intersects(entb.getBounds())){
				return true;
		}	
		return false;
	}
	
	public static boolean collision(EntityB entb, EntityA enta){
		
			if(entb.getBounds().intersects(enta.getBounds())){
				return true;
			}

		
		return false;
	}
}

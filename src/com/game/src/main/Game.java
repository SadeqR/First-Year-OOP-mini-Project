package com.game.src.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

//extending canvas allows us to have everything that is in the canvas class; implements runnable gets accessed by a thread
public class Game extends Canvas implements Runnable { 
	
	
	private static final long serialVersionUID = 1L;
	//constant variables
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH /12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Spaceship Adventure Game";
	
	private static JFrame frame;
	
	private boolean running = false;
	private Thread thread; //Initialising thread
	
						//buffers our whole window (thus needs our height and width)
						//private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	//BufferedImage actually loads image before it is rendered to screen
	private BufferedImage spriteSheet = null;
	private BufferedImage backG = null;
	
	//used to stop shooting glitch
	private boolean shooting = false; 
	
	private int enemy_count = 5;
	private int enemy_dead = 0;
	
	private int rock_count = 5;
	private int rock_dead = 0;
	
	private int SCORE;

	private Player p;
	private Controller c;
	private Sprites spr;
	
	public ArrayList<EntityA> ea;
	public ArrayList<EntityB> eb;
	
	public static int HP = 635;
	
	public static boolean moving = false;
	
	public static void main(String[] args){
		
		String uInput = JOptionPane.showInputDialog("Would you like to launch 'Spaceship Adventure Game'? Enter: YES or NO");
		
		if(uInput.equals("YES") || uInput.equals("yes")){
			JOptionPane.showMessageDialog(null, "ADVENTURE through space and protect the Earth \n		from Aliens and Metorites!");
			Game game = new Game();
											//     640     by	 480	
			game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
			
			frame = new JFrame(game.TITLE);
			frame.add(game); // takes above dimensions
			frame.pack(); // Makes sure we can see whole canvas - causes this Window to be sized to fit the preferred size and layouts of its subcomponents  automatically enlarged if either of dimensions is less than the minimum size
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null); 
			frame.setVisible(true);
			
			game.start();
		}
		else{
			JOptionPane.showMessageDialog(null, "GoodBye");
			System.exit(0);
		}
		
	}
	
	//Initialising components of game - runs once
	public void init(){
		requestFocus(); //don't need to click screen to start game
		BufferedImageLoader loader = new BufferedImageLoader();
		try{
			spriteSheet = loader.loadImage("/sprite_sheet.png");
			backG = loader.loadImage("/BG.png");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		addKeyListener(new KeyInput(this));
		
		spr = new Sprites(this);
		c = new Controller(spr, this);
		p = new Player(200, 200, spr, this , c);
		
		ea = c.getEntityA();
		eb = c.getEntityB();
		
		c.addEnemy(enemy_count);
		c.addRock(rock_count);

	}
	
	//method starts thread when called first time - also synchronised makes threads to access method one at a time
	private synchronized void start(){
		if(running){
			return; //gets out of method as if running is true we don't need it to be true again and start thread again
		}
		running = true;
		thread = new Thread(this); //this refers to this class that is ran under the thread
		thread.start(); //starting the thread runs run() method
	}
	
	private synchronized void stop(){
		if(!running){
			return;
		}
		
		try {
			thread.join(); // waits for all threads to finish and join to together and then can die out
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	//method will contain game loop; this method is overriding the run() method form Runnable
	//1st this game loop updates all the variable, positions of objects etc.;
	//2nd it renders (draws out) everything onto the screen
	//then the game-loop loops back to step 1 where it updates everything
	public void run(){
		init();
		long now;
		long lastTime = System.nanoTime(); //long same as int but bigger +ve/-ve numbers
		final double amountOfTicks = 60.0; //game updates 60 times - games is 60fps - no matter how good your PC is, the game will work at 60 ticks per second
		double ns = 1000000000/amountOfTicks; //in 1 second
		double delta = 0; //will calculate time passed
		int ticks = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		/*
		 * nanosecond is 1 billionth of a second, so 1000000000/60 is 60 frames per second, 	
		 * thus there needs to be a new tick every ~16666666.667 ns.	 
		 * So every iteration of the while loop, the ns's that have passed is calculated (via current nanosecond - the 
		 * previous nanosecond); and if the ns's passed divides into 16666666.667, the result will be 1,
		 * so this means ~16666666.667 ns's have passed and then the program will go to tick() again,
		 * then previous nanosecond is set to what it currently was and the whole process takes place again. 
		*/  
		while(running){
			now = System.nanoTime(); 
			delta += (now - lastTime)/ns; //(now - lastTime) gives the elapsed time since the run method was entered > then its divided by 1 billion to get time in seconds 
			lastTime = now; //current time
			if(delta >= 1){ //this loop makes sure the game only updates/goes to tick() method 60 times a second/60fps
				tick();
				ticks++;
				delta--;
			}
			render(); //but rendering is done as fast as possible but tick() is limited to 60 times/s
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000; //stop this loop happening again
				System.out.println(ticks + " Ticks, fps " + frames);
				ticks = 0;
				frames = 0;
			}
		}
		stop(); //after game loop we call stop() and end the program
	}
	
	//everything/all components in the game updates
	public void tick(){
		p.tick();
		c.tick();
		
		if(enemy_dead >= enemy_count){
			enemy_count += 2;
			enemy_dead = 0;
			c.addEnemy(enemy_count);
		}
		
		if(rock_dead >= rock_count){
			rock_count += 2;
			rock_dead = 0;
			c.addRock(rock_count);
		}
		
		if(HP == 0){
			frame.setVisible(false); //you can't see me!
			frame.dispose();
			
			JOptionPane.showMessageDialog(null, "GAMER OVER");
			if(SCORE>=10000){
				JOptionPane.showMessageDialog(null, "Your Highscore was " + SCORE + "\n...now that's what I'm talking about.");
				System.exit(0);
			}
			else if(SCORE>=5000){
				JOptionPane.showMessageDialog(null, "Your Highscore was " + SCORE + "\n...hmm, you have potential...");
				System.exit(0);
			}
			else if(SCORE>=1000){
			JOptionPane.showMessageDialog(null, "Your Highscore was " + SCORE + "\n...Meh...not bad.");
			System.exit(0);
			}
			else{
				JOptionPane.showMessageDialog(null, "Your Highscore was " + SCORE + "\n...THAT IS TERRIBLE!");
				System.exit(0);
			}
		}
	}
	
	//everything in the game that renders
	private void render(){
		
		//initialised bufferStrategy
		BufferStrategy bs = this.getBufferStrategy(); //'this' refers to canvas - as we inherit that class
		
		if(bs == null){
			createBufferStrategy(3); //2nd layer of buffering will take place - stops flickering
			return;
		}
		
		Graphics g = bs.getDrawGraphics(); //the graphics object allows us to draw to the screen
		/////////////////// in between here - we can draw out images
		
	//	g.drawImage(image, 0 , 0, getWidth(), getHeight(), this);
		
		g.drawImage(backG, 0, 0, null); //null refers to image observers which are not used in this game
		
		p.render(g);
		c.render(g);
		
		g.setColor(Color.gray);
		g.fillRect(5,  460,  635, 10);
		g.setColor(Color.red);
		g.fillRect(5,  460,  HP, 10);
		g.setColor(Color.white);
		g.drawRect(5,  460,  635, 10);
		
		/////////////////// end drawings:
		bs.show(); // shows graphics to window
		g.dispose(); //good practise to call .dispose() even though i don't really need it here		
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_RIGHT){
			p.setVX(5);
			moving = true; 
		}
		else if(key == KeyEvent.VK_LEFT){
			p.setVX(-5);
			moving = true; 
		}
		else if(key == KeyEvent.VK_DOWN){
			p.setVY(5);
			moving = true; 
		}
		else if(key == KeyEvent.VK_UP){
			p.setVY(-5);
			moving = true; 
		} 
		else if(key == KeyEvent.VK_SPACE && !shooting){
			shooting = true;
			c.addEntity(new BeamBlast(p.getX(), p.getY(), spr, this));
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_RIGHT){
			p.setVX(0);
			moving = false; 
		}
		else if(key == KeyEvent.VK_LEFT){
			p.setVX(0);
			moving = false; 
		}
		else if(key == KeyEvent.VK_DOWN){
			p.setVY(0);
			moving = false; 
		}
		else if(key == KeyEvent.VK_UP){
			p.setVY(0);
			moving = false; 
		}
		else if(key == KeyEvent.VK_SPACE){
			shooting = false; //forces player to release space bar to shoot
			moving = false; 
		}
	
	}
	
	
	public BufferedImage getSpriteSheet(){
		return spriteSheet;
	}

	public int getEnemy_count() {
		return enemy_count;
	}

	public void setEnemy_count(int enemy_count) {
		this.enemy_count = enemy_count;
	}

	public int getEnemy_dead() {
		return enemy_dead;
	}

	public void setEnemy_dead(int enemy_dead) {
		this.enemy_dead = enemy_dead;
	}
	
	
	public int getRock_count() {
		return rock_count;
	}

	public void setRock_count(int rock_count) {
		this.rock_count = rock_count;
	}

	public int getRock_dead() {
		return rock_dead;
	}

	public void setRock_dead(int rock_dead) {
		this.rock_dead = rock_dead;
	}
	

	public int getSCORE() {
		return SCORE;
	}

	public void setSCORE(int sCORE) {
		this.SCORE = sCORE;
	}
	
}

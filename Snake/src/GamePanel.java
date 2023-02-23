import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{
	static final int SCREEN_WIDTH=600;
	static final int SCREEN_HEIGHT=600;
	static final int UNIT_SIZE=25;
	static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY=75;
	final int x[]=new int[GAME_UNITS];
	final int y[]=new int[GAME_UNITS];
	int bodyParts=6; //snake's first bodyparts count
	int applesEaten; //0
	int appleX;
	int appleY;
	char direction='R'; //Begin with right
	boolean running=false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random=new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple(); //create apple
		running=true; //start movement
		timer=new Timer(DELAY,this); //Game speed with delay
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) { //if game is running
				for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) { //draw lines
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //x axis
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); //y axis
			}
			g.setColor(Color.red); //red apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //apple shape
			
			for(int i=0;i<bodyParts;i++) { //Snake as long as body parts
				if(i==0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //snake head
				}
				else {
					g.setColor(new Color(45,180,0)); //different green color
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			//Score text
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman",Font.BOLD,30));
			FontMetrics metrics=getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH-metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else { //game over
			gameOver(g); //pass g parameter
		}
	}
	
	public void newApple() { //create apple
		appleX=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //random x
		appleY=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; //random y
	}
	
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i]=x[i-1]; //shifting coordinates
			y[i]=y[i-1]; //shifting coordinates
		}
		
		switch(direction) {
		case 'U': //UP head
			y[0]=y[0]-UNIT_SIZE; 
			break;
		case 'D': //DOWN head
			y[0]=y[0]+UNIT_SIZE;
			break;
		case 'L': //LEFT head	
			x[0]=x[0]-UNIT_SIZE;
			break;
		case 'R': //RIGHT head
			x[0]=x[0]+UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() { //Apple situation
		if((x[0]==appleX)&&(y[0]==appleY)) { //if you eat apple
			bodyParts++; //grow snake +1
			applesEaten++; //eat apple score +1
			newApple(); //create new apple
		}
	}
	
	public void checkCollisions() { //look if there is collision
		for(int i=bodyParts;i>0;i--) { 
			if((x[0] == x[i])&&(y[0]==y[i])) { //if head collides with body
				running=false;
			}
		}
		//check if head touches left border
		if(x[0]<0) {
			running=false;
		}
		//check if head touches right border
		if(x[0]>SCREEN_WIDTH) {
			running=false;
		}
		//check if head touches top border
		if(y[0]<0) {
			running=false;
		}
		//check if head touches bottom border
		if(y[0]>SCREEN_HEIGHT) {
			running=false;
		}
		
		if(!running) { //if no running
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.RED);
		g.setFont(new Font("Times New Roman",Font.BOLD,30));
		FontMetrics metrics1=getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH-metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over Text
		g.setColor(Color.RED);
		g.setFont(new Font("Times New Roman",Font.BOLD,75));
		FontMetrics metrics2=getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH-metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(running) {
			move(); //move snake
			checkApple();
			checkCollisions();
		}
		//if game is not running
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{ //Key Control
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT: //Left Key
				if(direction!='R') {
					direction ='L'; //if not going right go left
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction!='L') {
					direction='R'; //if not going right go left
				}
				break;
			case KeyEvent.VK_UP:
				if(direction!='D') {
					direction='U'; //if not going down go up
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction!='U') {
					direction='D'; //if not going up go down
				}
				break;
			}
		}
	}
}

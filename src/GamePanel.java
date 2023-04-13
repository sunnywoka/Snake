//Yuekai Wu 13113181

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	JButton menu, start, suspend, help, quit;
	JTextField textAreaScore, textAreaLife, textHighestScore;
	int gameStage = 0; //0 for menu, 1 for start, 2 for suspend, 3 for over, 4 for guide
	Image apple, head, tail, food, poisonApple;
	int[] snakeX = new int[20]; 
	int[] snakeY = new int[20];
	int score,life, highestScore = 0, snakeLength, appleX, appleY, foodX, foodY,poisonAppleX, poisonAppleY;
	static final int gameX = 50, gameY = 50, gameWidth = 650, gameHeight = 650;//game area
	String direction = "up";
	Random random = new Random();
	int speed;
	
	public GamePanel(){
		initial();//Initial everything first when the program starts to run
		loadImage();//loading the image file
		
		menu = new JButton("Menu");//Create some buttons for player
		start = new JButton("Start");
		suspend = new JButton("Suspend");
        help = new JButton("Help");
        quit = new JButton("Quit");
        JLabel textScore = new JLabel("Score: ");//Player can read their score and life
        textAreaScore = new JTextField(4);
        JLabel textLife = new JLabel("Life: ");
        textAreaLife = new JTextField(4);
        JLabel textHighScore = new JLabel("Highest Score: ");
        textHighestScore = new JTextField(4);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        this.add(menu);
        this.add(start);
        this.add(suspend);
        this.add(help);
        this.add(quit);
        this.add(textScore);
        this.add(textAreaScore);
        this.add(textLife);
        this.add(textAreaLife);
        this.add(textHighScore);
        this.add(textHighestScore);
     
        menu.addActionListener(this);
        start.addActionListener(this);
        suspend.addActionListener(this);
        help.addActionListener(this);
        quit.addActionListener(this);
        menu.setFocusable(false);//make the screen that can focus on the keyboard
        start.setFocusable(false);
        suspend.setFocusable(false);
        help.setFocusable(false);
        quit.setFocusable(false);
        textAreaScore.setFocusable(false);
        textAreaLife.setFocusable(false);
        textHighestScore.setFocusable(false);
        
        this.addKeyListener(this);
        
        MyThread myThread = new MyThread();//call myThread to refresh the screen
        myThread.start();
	}
	
	public void initial() {//details when game is initial
		snakeLength = 3;
		score = 0;
		life = 3;
		snakeX[0] = 200;
		snakeY[0] = 200;
		snakeX[1] = 190;
		snakeY[1] = 200;
		snakeX[2] = 180;
		snakeY[2] = 200;
		direction = "right";
		gameStage = 0;
		appleX = 100 + 10*random.nextInt(55);
	    appleY = 100 + 10*random.nextInt(55);
	    foodX = 100 + 10*random.nextInt(55);
	    foodY = 100 + 10*random.nextInt(55);
	    poisonAppleX = 100 + 10*random.nextInt(55);
	    poisonAppleY = 100 + 10*random.nextInt(55);
		speed = 5;
		
	}
	
	public void gameUpdate() {//the game keep updating when thread is running
		snakeMove();
		checkAppleLocation();
		textAreaScore.setText(Integer.toString(score));
		textAreaLife.setText(Integer.toString(life));
		textHighestScore.setText(Integer.toString(highestScore));
	}
	
	public void loadImage() {//load images from local folder
		try {
			apple = ImageIO.read(new File("apple.png"));
			head = ImageIO.read(new File("head.png"));
			tail = ImageIO.read(new File("dot.png"));
			food = ImageIO.read(new File("food.png"));
			poisonApple = ImageIO.read(new File("poisonApple.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){//draw game main area
        super.paintComponent(g); 
        g.setColor(Color.black);
        g.fillRect(gameX, gameY, gameWidth, gameHeight);  
        g.setColor(Color.white);
        g.drawRect(gameX, gameY, gameWidth, gameHeight); 
        if(gameStage == 0) {//main page
        	g.setColor(Color.white);
        	g.setFont(new Font("Arial", Font.BOLD, 20));
        	g.drawString("Click button 'Start' to start the game", 200, 400);
        	g.drawString("Click button 'Help' to get game guide", 200, 430);
        	initial();
        }
        
        if(gameStage == 1) {//game start
        	g.setColor(Color.gray);
        	for(int i=5; i<=70; i++) {
        		g.drawLine(50,i*10,700,i*10);
        		g.drawLine(i*10,50,i*10,700);
        	}
        	g.setColor(Color.white);
        	g.drawImage(head, snakeX[0], snakeY[0], this);//draw snake head
        	for(int i=1; i<snakeLength; i++) {//draw snake body
        		g.drawImage(tail, snakeX[i], snakeY[i], this);
        	}
        	g.drawImage(apple, appleX, appleY, this);//draw apples
        	g.drawImage(food, foodX, foodY, this);
        	g.drawImage(poisonApple, poisonAppleX, poisonAppleY, this);
        }
        
        if(gameStage ==2) {//game suspend
        	g.setColor(Color.white);
        	g.setFont(new Font("Arial", Font.BOLD, 20));
        	g.drawString("The game is suspend, click button 'Suspend' back game", 130, 460);
        }
        
        if(gameStage == 3) {//game over
        	g.setColor(Color.white);
        	g.setFont(new Font("Arial", Font.BOLD, 20));
        	g.drawString("Game is over, your score is " +  Integer.toString(score), 200, 400);
        	g.drawString("Click button 'Menu' to back main page", 200, 430);
        }
        
        if(gameStage == 4) {//simple game rules
        	g.setColor(Color.white);
        	g.setFont(new Font("Arial", Font.BOLD, 20));
        	g.drawString("Use arrow keys up, down, left and right to control snack", 130, 400);
        	g.drawString("Click button 'Suspend' to suspend the game", 130, 430);
        	g.drawString("Click button 'Quit' to quit the game", 130, 460);
        	g.drawString("For more detail, please check Player Guide", 130, 490);
        	g.drawString("Click button 'Menu' to back main page", 130, 520);
        }
    }
		
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {//provide action to keys
		 switch(e.getKeyCode()) {
         case KeyEvent.VK_UP:
        	 if (direction.equals("down") == false){
        		 direction = "up";
        		 repaint();
        	}
        	break;
         case KeyEvent.VK_DOWN:
        	 if (direction.equals("up") == false) {
        		 direction = "down";
        		 repaint();
        	 }
        	 break;
         case KeyEvent.VK_LEFT:
        	 if (direction.equals("right") == false) {
        		 direction = "left";
        		 repaint();
        	}
        	 break;
         case KeyEvent.VK_RIGHT:
        	 if (direction.equals("left") == false) {
        		 direction = "right";
        		 repaint();
             }
        	 break;
       }
	}

	@Override
	public void keyReleased(KeyEvent e) {}



	@Override
	public void actionPerformed(ActionEvent e) {//provide the action to buttons
        if(e.getSource() == menu) {
        	gameStage = 0;
            repaint();
        }
		if(e.getSource() == start) {
			gameStage = 1;
            repaint();
        }
		if(e.getSource() == suspend) {
			if(gameStage==1) {
				gameStage = 2;
				repaint();
			}else if(gameStage==2) {
				gameStage = 1;  
				repaint();
			}
        }
		if(e.getSource() == help) {
            gameStage = 4;
            repaint();
        }
		if(e.getSource() == quit) {
            System.exit(0);
        }	
	}	
	
	class MyThread extends Thread{//inner class to run and refresh the game paint
		@Override
		public void run() {
			while(true){
				try {
					gameUpdate();
					sleep(100 - speed >= 0 ? 100 - speed : 0);
					repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void snakeMove() {//control the snake moving
		for (int i=snakeLength-1;i>0;i--) {//snake body moving
			snakeX[i] = snakeX[i-1];
			snakeY[i] = snakeY[i-1];
		}
		if ((direction == "up") &&(gameStage == 1)) {//snake head moves up
			snakeX[0] = snakeX[1];
			snakeY[0] = snakeY[1]-10;
			if(snakeY[0]<50) {
				gameStage = 3;
			}
		}
		if ((direction == "down") &&(gameStage == 1)){//snake head moves down
			snakeX[0] = snakeX[1];
			snakeY[0] = snakeY[1]+10;
			if(snakeY[0]>=700) {
				gameStage = 3;
			}
	    }
		
		if ((direction == "left") &&(gameStage == 1)){//snake head moves left
			snakeX[0] = snakeX[1]-10;
			snakeY[0] = snakeY[1];
			if(snakeX[0]<50) {
				gameStage = 3;
			}
		}
		if( (direction == "right") &&(gameStage == 1)){//snake head moves right
			snakeX[0] = snakeX[1]+10;
			snakeY[0] = snakeY[1];
			if(snakeX[0]>=700) {
				gameStage = 3;
			}
		}
		
		for (int i = 1; i < snakeLength; i++) {//if the head hit the body, the game is over
			 if ((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])  &&(gameStage == 1)){
			 gameStage = 3;
			 }
		}
		
	}
	
	public void checkAppleLocation() {
		if ((snakeX[0] == appleX) && (snakeY[0] == appleY)) {//snake eats an red apple
			snakeLength++;
			score += 1;
			appleX = 100 + 10*random.nextInt(55);
		    appleY = 100 + 10*random.nextInt(55);
		    speed +=4;
		    if(score%10==0) {//every ten red apples can gains a life
		    	life++;
		    }
		    if(score >= highestScore) {//record the highest score
		    	highestScore = score;
		    }
		    if(snakeLength>=20) {//maintain the snake length in max 20
		    	snakeLength = 20;
		    }
		    
	    }
		
		if ((snakeX[0] == foodX) && (snakeY[0] == foodY) ){//snake eats a yellow apple
			life += 1;
			foodX = 100 + 10*random.nextInt(55);
		    foodY = 100 + 10*random.nextInt(55);
		    speed +=3;
	    }
		
		if ((snakeX[0] == poisonAppleX) && (snakeY[0] == poisonAppleY) ){//snake easts a blue apple
			life -= 1;
			poisonAppleX = 100 + 10*random.nextInt(55);
		    poisonAppleY = 100 + 10*random.nextInt(55);
		    speed -=6;
		    if((life < 1) &&(gameStage == 1)){//life is 0, the game is over
		    	gameStage = 3;
		    }
	    }
	}
	
	
}
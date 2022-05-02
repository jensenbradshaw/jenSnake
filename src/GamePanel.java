import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH/UNIT_SIZE)*(SCREEN_HEIGHT/UNIT_SIZE);
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX1;
	int appleY1;
	int appleX2;
	int appleY2;
	int appleX3;
	int appleY3;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple(1);
		newApple(2);
		newApple(3);
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {
			// Draw grid lines.
			for (int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			
			// Draw apple.
			g.setColor(Color.red);
			g.fillOval(appleX1, appleY1, UNIT_SIZE, UNIT_SIZE);
			g.fillOval(appleX2, appleY2, UNIT_SIZE, UNIT_SIZE);
			g.fillOval(appleX3, appleY3, UNIT_SIZE, UNIT_SIZE);
			
			// Draw snake head and body.
			for(int i = 0;i<bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			// Draw current score.
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
	public void newApple(int i) {
		switch(i) {
		case 1:
			appleX1 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			appleY1 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			break;
		case 2:
			appleX2 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			appleY2 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			break;
		case 3:
			appleX3 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			appleY3 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			break;
		}
		
	}
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		// Move snake up.
		case 'U': 
			y[0] = y[0] - UNIT_SIZE;
			break;
		// Move snake down.
		case 'D': 
			y[0] = y[0] + UNIT_SIZE;
			break;
		// Move snake left.
		case 'L': 
			x[0] = x[0] - UNIT_SIZE;
			break;
		// Move snake right.
		case 'R': 
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX1) && (y[0] == appleY1)) {
			bodyParts++;
			applesEaten++;
			newApple(1);
		}
		if((x[0] == appleX2) && (y[0] == appleY2)) {
			bodyParts++;
			applesEaten++;
			newApple(2);
		}
		if((x[0] == appleX3) && (y[0] == appleY3)) {
			bodyParts++;
			applesEaten++;
			newApple(3);
		}
	}
	public void checkCollisions() {
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		if(x[0] < 0) {
			running = false;
		}
		if(x[0] > SCREEN_WIDTH-UNIT_SIZE) {
			running = false;
		}
		if(y[0] < 0) {
			running = false;
		}
		if(y[0] > SCREEN_HEIGHT-UNIT_SIZE) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		// Draw "game over" text.
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.red);
		
		// Draw final score.
		g.setFont(new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			// Left input changing snake's direction to left, if it isn't facing right.
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			// Right input changing snake's direction to right, if it isn't facing left.
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			// Up input changing snake's direction to up, if it isn't facing down.
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			// Down input changing snake's direction to down, if it isn't facing up.
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
	
}

package modifiedconnectfour;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

import javax.swing.JFrame;

public class Game extends Canvas {
	private static final long serialVersionUID = -869141580686314846L;
	//unnecessary but Canvas implements Serializable and "recommended that
	//all serializable classes explicitly declare serialVersionUID" (Oracle)

	public static final int width = 640, height = 480;
	
	private final BufferedImage board;
	private final Player player;
	private final PointTracker pointTracker;
	
	private Thread thread;
	
	public Game() {
		this.board = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		this.pointTracker = new PointTracker();
		this.player = new Player(board, pointTracker);
	}
	private void initUI() { //creates window for game
		JFrame frame = new JFrame("Modified Connect Four");
		
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		frame.setVisible(true);
	}
	public void start() {
		initUI();
		BoardUtil.reset(board);
		BoardUtil.displayInstructions(board);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_LEFT) player.moveLeft();
				else if (key == KeyEvent.VK_RIGHT) player.moveRight();
				else if (key == KeyEvent.VK_SPACE) {
					if (!player.noMovesLeft()) player.updateSlot();
					else { //if all slots full
						BoardUtil.reset(board);
						player.reset();
						pointTracker.reset();
						
						pointTracker.displayPoints(board.createGraphics());
						SoundEffect.RESET.play();
					}
				}
			}
		});
		this.requestFocus(); //makes it so don't have to manually
		//press screen for game to sense key strokes
		thread = new Thread(() -> {
			int frame = 1000 / 20;
			long lastRender = System.currentTimeMillis();
			long now;
			
			while (true) {
				now = System.currentTimeMillis();
				if (now - lastRender >= frame) { //so will run ~20 times per
					//second; less work for CPU than if run constantly)
					render(); lastRender = now; }
			}
		});
		thread.start(); //starts run() (i.e. lambda in Thread() (line 69))
	}
	private void render() {
		BufferStrategy buffer = this.getBufferStrategy();
		if (buffer == null) { this.createBufferStrategy(3); return; }
		//not having return causes NullPointerException in next line
		Graphics2D g2d = (Graphics2D) buffer.getDrawGraphics();
		
		g2d.drawImage(board, 0, 0, this); //reloads BufferedImage used
		//in user interface; image updated by Player and PointTracker
		//throughout game "behind the scenes"
		try {
			for (Point point : pointTracker.getPoints())
				point.drawCircling(g2d);
		} catch (ConcurrentModificationException e) {
			System.out.println("ConcurrentModificationException thrown");
		}
		player.showPlayer(g2d);
		
		g2d.dispose(); //not necessary (Java garbage collector would do
		//regardless) but good practice according to StackOverflow
		buffer.show();
	}
	public static void main(String[] args) {
		new Game().start();
	}
}

package mygame; //change name later

import java.awt.Canvas;
import java.awt.Color;
//import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 2443474138231561228L;
	private final int WIDTH = 600, HEIGHT = 450;
	private Thread thread; //only one thread; game is simple so don't need more
	private boolean running = false;
	private Graphics2D g; //use Graphics instead of Graphics2D
	
	public Game() {
		JFrame frame = new JFrame("game"); //change title later
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		frame.add(this);
	}
	public synchronized void start() {
		if (running) return; //unnecessary?
		running = true;
		thread = new Thread(this);
		thread.run(); //thread.start();
	}
	public synchronized void stop() {
		if (!running) return; //unnecessary?
		try { thread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
		running = false;
	}
	int x = 0;
	public void run() {
		final double MS_PER_FRAME = 1000.0 / 60.0; //caps rendering to 60 times per second
		boolean toRender;
		int now, mspassed, lastTick = (int) System.currentTimeMillis();
		int fps = 0, lastFPSCheck = lastTick; //for checking FPS, unnecessary for game
		
		while (running) {
			toRender = false;
			now = (int) System.currentTimeMillis();
			mspassed = now - lastTick;
			
			if (mspassed >= MS_PER_FRAME) {
				int ticks = (int) (mspassed / MS_PER_FRAME);
				tick(ticks);
				lastTick += ticks * MS_PER_FRAME; //lastTick = now; (*slightly* less accurate)
				toRender = true;
			}
			if (toRender) {render(g); toRender = false; fps++; x++; }
			else { //thread sleep when not rendering; less work for CPU
				try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
			};
			
			if (now - lastFPSCheck >= 1000) {
				lastFPSCheck = now;
				System.out.println("FPS: " + fps);
				fps = 0;
			} //for checking FPS (should be under 60), unnecessary for game
		}
		stop();
	}
	private void tick(int ticks) {} //TODO
	private void render(Graphics2D g) { //get rid of parameter?
		BufferStrategy buffer = this.getBufferStrategy();
		if (buffer == null) { this.createBufferStrategy(3); return; }
		
		g = (Graphics2D) buffer.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT); //set background to black
		
		g.setColor(Color.blue);
		g.fillRect(x, 0, 50, 50); //test if moving shape works 
		
		g.dispose();
		buffer.show();
	} //TODO
	public static void main(String[] args) {
		new Game().start();
	}
}

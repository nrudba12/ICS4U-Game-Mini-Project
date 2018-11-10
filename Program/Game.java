package mygame; //change name later

import java.awt.Canvas;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 2443474138231561228L;
	private final int WIDTH = 400, HEIGHT = 300;
	private Thread thread; //only one thread; game is simple so don't need more
	private boolean running = false;
	
	public Game() {
		JFrame frame = new JFrame("game"); //change title later
		
		frame.setSize(WIDTH, HEIGHT);
		//frame.setPreferredSize(new Dimension(width, height));
		//frame.setMaximumSize(new Dimension(width, height));
		//frame.setMinimumSize(new Dimension(width, height));
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.add(this);
		
		//start();
	}
	
	public synchronized void start() {
		//if (running) return;
		
		thread = new Thread(this);
		thread.run(); //thread.start();
		running = true;
	}
	public synchronized void stop() {
		//if (!running) return;
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		running = true;
		
		final double RENDER_CAP = 1.0/60.0; //caps rendering to 60 fps
		boolean toRender;
		double now, firstTime = System.currentTimeMillis()/1000.0;
		//double fps = 0, seconds = (int) (firstTime); //for testing fps, which also checks time
		
		while (running) {
			toRender = false;
			now = System.currentTimeMillis()/1000.0;
			
			/*if (now - seconds >= 1) {
				seconds++;
				System.out.println(fps);
				fps = 0;
			}*/ //for checking fps (should be 60) and time passed
			
			while (now - firstTime >= RENDER_CAP) {
				firstTime += RENDER_CAP;
				update();
				//fps++; //for checking fps
				//System.out.println(fps + " updated"); //for testing
				toRender = true;
			}
			if (toRender) {render(); /*System.out.println(fps+" rendered");*/ }
			//should output "n rendered" where n increases by one each time
			/*if (toRender) {
				render();
				toRender = false;
				//System.out.println(fps + " rendered"); //for testing
			} else { //since not rendering, no point in thread executing; less work for CPU
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}*/
		}
		//stop();
	}
	void update() {} //TODO and add appropriate access modifier
	void render() {} //TODO and add appropriate access modifier
	public static void main(String[] args) {
		new Game().start();
	}
}

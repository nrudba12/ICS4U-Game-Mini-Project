package modifiedconnectfour;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player {
	private final BufferedImage board;
	private final PointTracker pointTracker;
	
	private final int[] availableSlots; //keeps track of lowest available
	//slot in each column
	
	private Color colour; //current colour of player
	private int column; //current position of player
	private int movesRemaining; //number of slots not yet filled
	
	public Player(BufferedImage board, PointTracker pointTracker) {
		this.board = board;
		this.pointTracker = pointTracker;
		this.availableSlots = new int[7];
		this.colour = Color.red;
		this.column = 3;
		this.movesRemaining = 7 * 5; //columns * rows = total num of slots
	}
	public boolean restart() { //for Game to know whether or not slots full
		return movesRemaining == 0;
	}
	public void reset() {
		System.arraycopy(new int[7], 0, availableSlots, 0, 7);
		colour = Color.red;
		movesRemaining = 7 * 5;
	}
	//shows circle hovering over current column
	public void showPlayer(Graphics2D g2d) {
		g2d.setColor(colour);
		g2d.fillOval(BoardUtil.findX(column), BoardUtil.findY(5)
				- BoardUtil.spacing, BoardUtil.slotwid, BoardUtil.slotwid);
	}
	public void moveLeft() {
		if (column - 1 >= 0) column--;
	}
	public void moveRight() {
		if (column + 1 < 7) column++;
	}
	public void updateSlot() {
		if (availableSlots[column] > 4) return; //means no available slots
		//in current column
		
		int row = availableSlots[column];
	    char colourAsChar = (colour == Color.red ? 'R' : 'B');
		
		Graphics2D g2d = board.createGraphics();
		
		//fills lowest available slot of current column
		g2d.setColor(colour);
		g2d.fillOval(BoardUtil.findX(column), BoardUtil.findY(row),
				BoardUtil.slotwid, BoardUtil.slotwid);
		SoundEffect.TICK.play();
		
		//lowest available slots in column moves up one; one less slot left
		availableSlots[column]++; movesRemaining--;
		
		pointTracker.updatePoints(column, row, colourAsChar);
		pointTracker.displayPoints(g2d);
		if (movesRemaining == 0) gameOverDisplay(g2d);
		
		g2d.dispose();
		
		//changes colour of hovering token to next player's colour
		if (colour == Color.red) colour = Color.blue;
		else colour = Color.red;
	}
	private void gameOverDisplay(Graphics2D g2d) {
		g2d.setColor(BoardUtil.bkgdcolour);
		g2d.fillRect(10, 10, 120, 30); //"erases" score board
		
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.BOLD, 14));
		
		int[] scores = pointTracker.getScores(); //red first, blue second
		
		String strToDisplay;
		if (scores[0] == scores[1]) {
			int score = scores[0];
			strToDisplay = String.format("Tie game %d - %d!", score, score);
		}
		else {
			int winningSc = (scores[0] > scores[1] ? scores[0] : scores[1]);
			int losingSc = (winningSc == scores[0] ? scores[1] : scores[0]);
			String winner = (winningSc == scores[0] ? "RED" : "BLUE");
			
			strToDisplay = String.format("Player %s wins %d - %d!",
					winner, winningSc, losingSc);
		}
		int strWidth = g2d.getFontMetrics().stringWidth(strToDisplay);
		g2d.drawString(strToDisplay, (Game.width - strWidth)/2, 20);
		
		strToDisplay = "Press SPACE to restart";
		strWidth = g2d.getFontMetrics().stringWidth(strToDisplay);
		g2d.drawString(strToDisplay, (Game.width - strWidth)/2, 35);
		
		SoundEffect.GAMEOVER.play();
	}
}
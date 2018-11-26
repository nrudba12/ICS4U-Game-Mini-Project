package modifiedconnectfour;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class BoardUtil {
	private BoardUtil() {} //so no instances of class can be created
	//since all methods and values public static
	
	public static final Color bkgdcolour = new Color(240, 240, 240);
	public static final int boardwid = 470, boardheight = 320;
	public static final int spacing = 15, xshift = (Game.width - boardwid)/2, yshift = 100;
	public static final int slotwid = (boardwid - 8*spacing) / 7;
	
	public static void reset(BufferedImage board) {
		Graphics2D g2d = board.createGraphics();
		
		g2d.setColor(bkgdcolour);
		g2d.fillRect(0, 0, Game.width, Game.height);
		
		//draws board (i.e. a brown rectangle)
		g2d.setColor(new Color(130, 65, 0));
		g2d.fillRoundRect(xshift, yshift, boardwid, boardheight, 50, 50);
		g2d.setColor(bkgdcolour);
		
		//draws background-coloured circles on board
		for (int y = boardheight + yshift - spacing - slotwid; y > yshift;
				y -= spacing - 5 + slotwid) {
			for (int x = spacing + xshift; x < boardwid + xshift;
					x += slotwid + spacing)
				g2d.fillOval(x, y, slotwid, slotwid);
		} //ends up creating five rows, seven columns of circles
	    
	    g2d.dispose();
	}
	public static void displayInstructions(BufferedImage board) {
		Graphics2D g2d = board.createGraphics();
		
	    g2d.setColor(Color.black);
	    g2d.setFont(new Font("Arial", Font.BOLD, 14));
	    g2d.drawString("> Press LEFT/RIGHT to position", 10, 20);
	    g2d.drawString("> Press SPACE to drop", 10, 35);
	    g2d.drawString("> Game over when board is full", 10, 50);
	    g2d.drawString("> Player with most rows of four wins!", 10, 65);
	    
	    g2d.dispose();
	}
	//return x and y coordinates of circle in board (according to its row
	//or column, respectively); used by PointTracker and Points
	public static int findX(int column) {
		return spacing + xshift + column*(spacing + slotwid);
	}
	public static int findY(int row) {
		return boardheight + yshift - spacing - slotwid
				- row*(spacing - 5 + slotwid);
	}
}
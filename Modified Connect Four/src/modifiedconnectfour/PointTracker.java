package modifiedconnectfour;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class PointTracker {
	private final LinkedList<Point> points;
	private final char[][] slots; //which colours (red, blue, none) in slots
	
	private int redpts, bluepts;
	
	public PointTracker() {
		this.points = new LinkedList<>();
		this.slots = new char[7][5];
		this.redpts = 0;
		this.bluepts = 0;
	}
	public void reset() {
		points.clear();
		System.arraycopy(new char[7][5], 0, slots, 0, 7);
		redpts = 0; bluepts = 0;
	}
	public LinkedList<Point> getPoints() {
		return this.points;
	}
	public int[] getScores() { //used by Player once all slots filled
		return new int[] {redpts, bluepts};
	}
	public void displayPoints(Graphics2D g2d) {
		g2d.setColor(BoardUtil.bkgdcolour);
		g2d.fillRect(10, 10, 260, 65); //"erases" old score BoardUtil (or
		//instructions if beginning of game) by drawing over
		
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.BOLD, 14));
		g2d.drawString("Player RED - " + redpts, 10, 20);
		g2d.drawString("Player BLUE - " + bluepts, 10, 35);
	}
	public void updatePoints(int x, int y, char colour) {
		slots[x][y] = colour;
		
		//points earned from each row, column, diagonal slot intersects
		int points = 0;
		points += horizontal(y, colour);
		points += vertical(x, colour);
		points += rightDiagonal(x, y, colour);
		points += leftDiagonal(x, y, colour);
		
		if (colour == 'R') redpts += points;
		else bluepts += points;
	}
	private int horizontal(int y, char colour) {
		char[] row = new char[7];
		for (int i = 0; i < 7; i++) row[i] = slots[i][y];
		//creates char array of every slot colour in horizontal row
		
		int start = getStart(row, colour);
		if (start < 0) return 0;
		
		int points = 0;
		for (int i = start; i + 3 < 7; i++) {
			if (row[i + 3] != colour) break;
			if (newPoint(i, y, RowOfFourType.HORIZONTAL)) points++;
		}
		return points;
	}
	private int vertical(int x, char colour) {
		char[] column = slots[x];
		//creates char array of every slot colour in vertical row
		
		int start = getStart(column, colour);
		if (start < 0) return 0;
		
		int points = 0;
		for (int i = start; i + 3 < 5; i++) {
			if (column[i + 3] != colour) break;
			if (newPoint(x, i, RowOfFourType.VERTICAL)) points++;
		}
		return points;
	}
	private int rightDiagonal(int x, int y, char colour) {
		int startx = x - y, starty = 0;
		if (startx < 0) { starty -= startx; startx = 0; }
		
		char[] diagonal = new char[5];
		for (int i = 0; i < 5; i++) {
			if (startx + i == 7 || starty + i == 5) break;
			diagonal[i] = slots[startx + i][starty + i];
		}
		//creates char array of every slot colour in "/" diagonal
		
		int start = getStart(diagonal, colour);
		if (start < 0) return 0;
		
		int points = 0;
		for (int i = start; i + 3 < 5; i++) {
			if (diagonal[i + 3] != colour) break;
			if (newPoint(startx + i, starty + i, RowOfFourType.R_DIAGONAL))
				points++;
		}
		return points;
	}
	private int leftDiagonal(int x, int y, char colour) {
		int startx = x - (4 - y), starty = 4;
		if (startx < 0) { starty += startx; startx = 0; }
		
		char[] diagonal = new char[5];
		for (int i = 0; i < 5; i++) {
			if (startx + i == 7 || starty - i < 0) break;
			diagonal[i] = slots[startx + i][starty - i];
		}
		//creates char array of every token colour in "\" diagonal
		
		int start = getStart(diagonal, colour);
		if (start < 0) return 0;
		
		int points = 0;
		for (int i = start; i + 3 < 5; i++) {
			if (diagonal[i + 3] != colour) break;
			if (newPoint(startx + i, starty - i, RowOfFourType.L_DIAGONAL))
				points++;
		}
		return points;
	}
	private boolean newPoint(int x, int y, RowOfFourType type) {
		boolean newPoint = true;
		//checks if row of four already counted
		for (Point point : points) {
			if (point.getRowOfFourType() == type) {
				int[] coordinates = point.getCoordinates();
				if (coordinates[0] == x && coordinates[1] == y) {
					newPoint = false; break; }
			}
		}
		if (newPoint) points.add(new Point(x, y, type));
		return newPoint;
	}
	private static int getStart(char[] slotsToCheck, char c) {
		//returns index at which specified slots have streak of four
		//(returns -1 if no streak, row "RBBBBBR" checking for B returns 1)
		String strSlots = new String(slotsToCheck);
		String streak = new String(new char[] {c, c, c, c});
		return strSlots.indexOf(streak);
	}
}
package modifiedconnectfour;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Point {
	private final int x, y;
	private final RowOfFourType type;
	
	public Point(int x, int y, RowOfFourType type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	public int[] getCoordinates() {
		return new int[] {x, y};
	}
	public RowOfFourType getRowOfFourType() {
		return type;
	}
	public void drawCircling(Graphics2D g2d) { //circles row of four
		//associated with Point object with green line
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(Color.green);
		
		//variables to make less cluttered and to prevent having to calculate
		//same numbers and/or call same methods over and over
		int newX = BoardUtil.findX(x) - BoardUtil.spacing/2;
		int newY = BoardUtil.findY(y) - (BoardUtil.spacing - 5)/2;
		int wid = BoardUtil.slotwid + BoardUtil.spacing;
		int halfOfYSpacing = (BoardUtil.spacing - 5)/2;
		
		if (type == RowOfFourType.HORIZONTAL) {
			g2d.drawRoundRect(newX, newY, 4 * wid, wid - 5, 60, 60);
		}
		else if (type == RowOfFourType.VERTICAL) {
			g2d.drawRoundRect(newX, BoardUtil.findY(y + 3) - halfOfYSpacing,
					wid, 4*(wid - 5), 60, 60);
		}
		else {
			int midSlotX = BoardUtil.findX(x) + BoardUtil.slotwid/2;
			int midThirdSlotX = BoardUtil.findX(x + 3) + BoardUtil.slotwid/2;
			int addToY = BoardUtil.slotwid + (BoardUtil.spacing - 5)/2 - 10;
			
			if (type == RowOfFourType.R_DIAGONAL) {
				int otherY = BoardUtil.findY(y + 3);
				
				g2d.drawArc(newX, newY, wid, wid - 5, 135, 180);
				g2d.drawLine(midSlotX - 25, newY + 10, midThirdSlotX
						- 20, otherY - halfOfYSpacing + 6);
				g2d.drawLine(midSlotX + 25, BoardUtil.findY(y) + addToY,
						midThirdSlotX + 25, otherY + addToY);
				g2d.drawArc(BoardUtil.findX(x + 3) - BoardUtil.spacing/2,
						otherY - halfOfYSpacing, wid, wid - 5, 315, 180);
			}
			else { //meaning type == RowOfFourType.L_DIAGONAL
				int otherY = BoardUtil.findY(y - 3);
				
				g2d.drawArc(newX, newY, wid, wid - 5, 45, 180);
				g2d.drawLine(midSlotX + 25, newY + 10, midThirdSlotX
						+ 24, otherY - halfOfYSpacing + 10);
				g2d.drawLine(BoardUtil.findX(x) + BoardUtil.slotwid/2 - 24,
						BoardUtil.findY(y) + addToY, BoardUtil.findX(x + 3),
						otherY + BoardUtil.slotwid - 6);
				g2d.drawArc(BoardUtil.findX(x + 3) - BoardUtil.spacing/2,
						otherY - halfOfYSpacing, wid, wid - 5, 225, 180);
			}
		}
	}
}
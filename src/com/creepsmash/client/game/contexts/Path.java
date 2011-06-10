package com.creepsmash.client.game.contexts;

import java.util.ArrayList;

import com.creepsmash.client.game.grids.Grid;


/**
 * This class represents the path a creep walks on the board.
 * The path is a list of segments. A segment is basically one
 * grid within the board, and means the access integer used
 * get a grid out of the gridarray in the board class.
 * So segment 0,0 is the center of the grid in the upper left corner.
 */
public class Path {

	private static class Segment {
		public int x;
		public int y;

		public Segment(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}
	}

	private ArrayList<Segment> segments;

	public Path() {
		this.segments = new ArrayList<Segment>();
	}
	
	/**
	 * Adds one segment to the path.
	 * 
	 * @param x the x value
	 * @param y the y value
	 */
	public void addSegment(int x, int y) {
		this.segments.add(new Segment(x,y));
	}

	/**
	 * Gets the next position.
	 * @param segment the current segment
	 * @param segmentStep the step in the current segment
	 * @return a float array with the x and y coordinates
	 */
	public double[] getStep(int segment, int segmentStep) {
		double f = (double) segmentStep / 1000.0d;
		Segment segment1 = this.getSegmentOrLast(segment);
		Segment segment2 = this.getSegmentOrLast(segment + 1);
		Segment segment3 = this.getSegmentOrLast(segment + 2);

		double[] ret = new double[] { 0d , 0d, 0d };

		// Calculate Position
		double xStart = segment1.getX() * Grid.SIZE;
		double yStart = segment1.getY() * Grid.SIZE;
		double xEnd = segment2.getX() * Grid.SIZE;
		double yEnd = segment2.getY() * Grid.SIZE;
		ret[0] = xStart + (xEnd - xStart) * f;
		ret[1] = yStart + (yEnd - yStart) * f;

		// Calculate Angle
		double angle1 = calcAngle(segment1, segment2);
		double angle2 = calcAngle(segment2, segment3);
		if (angle2 != -1.0d) {
			if (angle1 == -1.0d)
				angle1 = angle2;
			if (angle2 > angle1 + Math.PI)
				angle2 -= Math.PI * 2.0d;
			if (angle2 < angle1 - Math.PI)
				angle2 += Math.PI * 2.0d;
			// IMBA smooth function ^^
			f += 0.5d;
			f %= 1.0d;
			double new_f = ((Math.pow(f * 2 - 1, 3) + 1) / 2.0d + f) / 2.0d;
			if (f < 0.5d)
				new_f += 0.5d;
			else
				new_f -= 0.5d;
			ret[2] = angle1 * (1.0d - new_f) + angle2 * new_f;
		}
		return ret;
	}

	private Segment getSegmentOrLast(int index) {
		if (index < this.segments.size()) {
			return this.segments.get(index);
		}
		return this.segments.get(this.segments.size()-1);
	}

	/**
	 * Berechnet den winkel von anfangs (x1) & endpunkt (x2)
	 * bei stopps gibt diese funktion -1.0d zurück wegen length = 0;
	 */
	private static double calcAngle(Segment segment1, Segment segment2) {
		double x1 = segment1.getX() * Grid.SIZE;
		double y1 = segment1.getY() * Grid.SIZE;
		double x2 = segment2.getX() * Grid.SIZE;
		double y2 = segment2.getY() * Grid.SIZE;

		double length = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		if (length == 0.0d)
			return -1.0d;
		double angle = Math.acos((double) (x2 - x1) / length);
		if (y1 > y2)
			angle = Math.PI * 2 - angle;
		return angle;
	}

	/**
	 * Getter for the length of the path.
	 * @return the length in segments
	 */
	public int size() {
		return this.segments.size();
	}
}

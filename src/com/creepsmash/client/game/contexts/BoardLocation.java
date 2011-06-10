package com.creepsmash.client.game.contexts;

import java.awt.Rectangle;

public enum BoardLocation {
	/**
	 * Enumeration for the four positions where a GameBoard can be.
	 */
	TOPLEFT(30, 30, 320, 320),
	BOTTOMLEFT(30, 350, 320, 320),
	TOPRIGHT(350, 30, 320, 320),
	BOTTOMRIGHT(350, 350, 320, 320);

	private final double x;
	private final double y;
	private final int width;
	private final int height;
	private final Rectangle bounds;

	/**
	 * BoardLocation constructor.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param width the width
	 * @param height the height
	 */
	BoardLocation(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bounds = new Rectangle((int) x, (int) y, width, height);
	}

	/**
	 * Getter for the x location.
	 * 
	 * @return the x location
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Getter for the y location.
	 * 
	 * @return the x location
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Getter for the width.
	 * 
	 * @return the the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Getter for the the height.
	 * 
	 * @return the the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Getter for the bounding box.
	 * 
	 * @return a Rectangle specifying the bounding box
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * get the Successor.
	 * 
	 * @param loc
	 *            location
	 * @return location
	 */
	public static BoardLocation getSuccessor(BoardLocation loc) {
		switch (loc) {
		case TOPLEFT:
			return BoardLocation.TOPRIGHT;
		case TOPRIGHT:
			return BoardLocation.BOTTOMRIGHT;
		case BOTTOMRIGHT:
			return BoardLocation.BOTTOMLEFT;
		case BOTTOMLEFT:
			return BoardLocation.TOPLEFT;
		default:
			return null;
		}
	}
}

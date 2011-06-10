package com.creepsmash.client.game.grids;

import java.awt.Graphics2D;

import com.creepsmash.client.game.towers.Tower;


/**
 * Interface for the grid.
 */
public interface Grid {
	/**
	 * grid size in pixel
	 */
	static final int SIZE = 20;
	
	/**
	 * Paints the grid with its outline.
	 * This should only be called when creating the map. For Highlighting
	 * when placing a tower, the paintHighlight method should be called.
	 * @param g the graphics object
	 */
	void paint(Graphics2D g);

	/**
	 * Paints a highlighted grid.
	 * @param g the graphics object
	 */
	void paintHighlight(Graphics2D g);

	/**
	 * Indicates if the grid is empty.
	 * @return true if no tower resides in the grid
	 */
	boolean isFree();

	/**
	 * Sets a tower into the grid.
	 * @param t the tower
	 */
	void setTower(Tower t);

	/**
	 * returns the tower object.
	 * @return tower object
	 */
	Tower getTower();

	/**
	 * removes a tower from the grid.
	 * Used for selling towers.
	 */
	void removeTower();

	/**
	 * Gets the location of the grid in x and y coordinates.
	 * @return an int[] with x and y
	 */
	int[] getLocation();

	/**
	 * Getter for the x position.
	 * @return the x pos
	 */
	int getX();

	/**
	 * Getter for the y position.
	 * @return the y pos
	 */
	int getY();

	/**
	 * Sets if the grid should be painted with highlight.
	 * @param highlight the highlight to set
	 */
	void setHighlight(boolean highlight);

	boolean getHighlight();
}

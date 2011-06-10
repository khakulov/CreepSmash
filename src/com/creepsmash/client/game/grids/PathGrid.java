package com.creepsmash.client.game.grids;

import java.awt.Graphics2D;

import com.creepsmash.client.game.contexts.Context;


/**
 * Grid to paint the path where the creeps walk/fly/drive along.
 */
public class PathGrid extends EmptyGrid {
	
	/**
	 * Creates a new instance of PathGrid.
	 * @param x the x location
	 * @param y the y location
	 * @param context the gamecontext
	 */
	public PathGrid(int x, int y, Context context) {
		super(x, y, context);
	}

	public boolean isFree() {
		// the PathGrid is never free
		return false;
	}

	public void paint(Graphics2D g) {
	}

	public void paintHighlight(Graphics2D g) {
	}
}

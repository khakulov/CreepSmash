package com.creepsmash.client.game.grids;

import java.awt.Graphics2D;

import com.creepsmash.client.game.contexts.Context;


/**
 * Just holy Grid in the map where noting
 * can be built.
 */
public class HolyGrid extends EmptyGrid {
	
	/**
	 * Creates a new instance of HolyGrid.
	 * @param x the x location
	 * @param y the y location
	 * @param context the gamecontext
	 */
	public HolyGrid(int x, int y, Context context) {
		super(x, y, context);
	}

	public boolean isFree() {
		// the HolyGrid is never free
		return false;
	}
	public void paint(Graphics2D g) {
	}
	public void paintHighlight(Graphics2D g) {
	}

}

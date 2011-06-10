package com.creepsmash.client.game.towers;

import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.common.TowerType;


/**
 * Factory to create towers by their type.
 */
public class TowerFactory {

	/**
	 * Creates a tower by the given type.
	 * 
	 * @param t
	 *            the type of the tower
	 * @param context
	 *            the gameContext for the tower
	 * @param grid
	 *            the grid in which the tower is placed
	 * @return the created tower
	 */
	public static Tower createTower(Context context, TowerType t, Grid grid) {
		return new AbstractTower(t, context, grid);
	}
}

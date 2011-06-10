package com.creepsmash.client.game.towers;

import java.awt.Graphics2D;

import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.game.strategies.Strategy;
import com.creepsmash.common.TowerType;


/**
 * Interface for all towers.
 */
public interface Tower {
	/**
	 * Updates the tower.
	 * @param round the current tick in the gameloop
	 */
	void update(long round);

	/**
	 * Paints the tower and his projectiles.
	 * @param graphics the graphics2D instance to draw at
	 */
	void paint(Graphics2D graphics);

	/**
	 * Paint the shooting effect.
	 * @param graphics the graphics context
	 */
	void paintEffect(Graphics2D graphics);

	/**
	 * Getter for the tower's id.
	 * @return the id from the tower
	 */
	int getId();

	/**
	 * Setter for the tower's id.
	 * @param id the id
	 */
	void setId(int id);

	/**
	 * @return the type
	 */
	TowerType getType();

	/**
	 * @return context
	 */
	Context getContext();

	/**
	 * @return grid
	 */
	Grid getGrid();

	/**
	 * is tower active
	 * @return the active
	 */
	boolean isActive();

	/**
	 * is tower ready for new operations
	 * @return the ready
	 */
	boolean isReady();

	void build(int buildTime);
	int getBuildTime();

	void upgrade(int upgradeTime);
	int getUpgradeTime();
	boolean isUpgradable();

	void changeStrategy(int changeStrategyTime);
	int getChangeStrategyTime();
	void setNewStrategy(Strategy strategy);
	Strategy getNewStrategy();
	Strategy getStrategy();

	void sell(int sellTime);
	int getSellTime();


	/**
	 * Sets the tower to be selected. If a tower is selected, it should be
	 * painted with a highlight.
	 * 
	 * @param selected
	 *            true if the tower is selected
	 */
	void setSelected(boolean selected);

	/**
	 * @return true if tower is selected, else false
	 */
	boolean isSelected();

	/**
	 * @return the sellPrice
	 */
	int getTotalPrice();

	boolean isCreepInRange(Creep creep);
	int getCoolDown();
}

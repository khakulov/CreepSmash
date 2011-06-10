package com.creepsmash.client.game.strategies;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.towers.Tower;

/**
 * Strategy interface to find a creep to shoot on.
 */
public interface Strategy {
	
	/**
	 * Startegy to find a creep to shot on.
	 * @return creep
	 */
	Creep findCreep();

	void init(Tower tower);
	void lock();
	void unlock();
	boolean isLocked();
}

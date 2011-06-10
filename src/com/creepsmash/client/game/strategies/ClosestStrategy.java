package com.creepsmash.client.game.strategies;

import java.util.List;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.grids.Grid;


/**
 * A strategy to find the closest Creep.
 */
public class ClosestStrategy extends AbstractStrategy {
	/**
	 * Strategy.
	 * @return Creep
	 */
	protected Creep findNewCreep(List<Creep> creeps) {
		Creep found = null;
		double distance = 99999d;
		for (Creep creep : creeps) {
			double dX = (creep.getX() + Grid.SIZE / 2) - (tower.getGrid().getLocation()[0] + Grid.SIZE / 2);
			double dY = (creep.getY() + Grid.SIZE / 2) - (tower.getGrid().getLocation()[1] + Grid.SIZE / 2);
			// squared distance
			double dist = dX * dX + dY * dY;
			if (found != null && dist > distance)
				continue;
			distance = dist;
			found = creep;
		}
		return found;
	}
}

package com.creepsmash.client.game.strategies;

import java.util.List;

import com.creepsmash.client.game.creeps.Creep;


/**
 * A strategy to find the farthest Creep.
 */
public class FarthestStrategy extends AbstractStrategy {
	/**
	 * Strategy.
	 * @return Creep
	 */
	public Creep findNewCreep(List<Creep> creeps) {
		Creep found = null;
		for (Creep creep : creeps) {
			if (found != null && creep.getTotalSegmentSteps() < found.getTotalSegmentSteps())
				continue;
			found = creep;
		}
		return found;
	}
}

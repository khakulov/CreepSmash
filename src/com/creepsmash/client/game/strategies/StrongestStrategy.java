package com.creepsmash.client.game.strategies;

import java.util.List;

import com.creepsmash.client.game.creeps.Creep;


/**
 * A strategy to find the strongest Creep.
 */
public class StrongestStrategy extends AbstractStrategy {
	/**
	 * Strategy.
	 * @return Creep
	 */
	public Creep findNewCreep(List<Creep> creeps) {
		Creep found = null;
		for (Creep creep : creeps) {
			if (found != null && creep.getHealth() > found.getHealth())
				continue;
			found = creep;
		}
		return found;
	}
}

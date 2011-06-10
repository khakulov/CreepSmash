package com.creepsmash.client.game.strategies;

import java.util.List;

import com.creepsmash.client.game.creeps.Creep;


/**
 * A strategy to find the fastest Creep.
 */
public class FastestStrategy extends AbstractStrategy {
	/**
	 * Strategy.
	 * @return Creep
	 */
	public Creep findNewCreep(List<Creep> creeps) {
		Creep found = null;
		for (Creep creep : creeps) {
			// shoot immune Creeps only if there are no others to shoot
			if (found != null && creep.getSpeed() > found.getSpeed())
				continue;
			if (found != null && creep.getType().isSlowImmun())
				continue;
			found = creep;
		}
		return found;
	}
}

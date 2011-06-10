package com.creepsmash.client.game.creeps;

import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.common.CreepType;


/**
 * Factory to create new creeps.
 */
public class CreepFactory {
	
	/**
	 * creates a Creep.
	 * @param context gets GameContext
	 * @param t type of creep
	 * @return type of creep
	 */
	public static Creep createCreep(Context context, CreepType t) {
		return new CreepImpl(context, t);
	}
}

package com.creepsmash.client.game.weapons;


import com.creepsmash.client.Core;
import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.towers.Tower;
import com.creepsmash.client.utils.SoundManager;

public abstract class AbstractWeapon implements Weapon {
	protected Tower tower;
	protected Creep target;

	@Override
	public void init(Tower tower) {
		this.tower = tower;
	}

	@Override
	public void update(long round) {
		// do nothing
	}

	/**
	 * Calculates the damage done to the creep.
	 * @param creep creep to damage
	 * @param damage value of the damage
	 */
	protected void damageCreep(Creep creep, int damage) {
		creep.setHealth(creep.getHealth() - damage);
		// if the creep is death ...
		if (creep.getHealth() <= 0) {
			// ... play sound
			// play sound if own context
			if (this.tower.getContext().getPlayerId() == Core.getPlayerId())
				SoundManager.creepDiesSound(creep.getType());
			// ... remove the creep
			this.tower.getContext().getCreeps().remove(creep);
			// ... increase money by bounty
			this.tower.getContext().setCredits(this.tower.getContext().getCredits() + creep.getType().getBounty());
		}
	}
}

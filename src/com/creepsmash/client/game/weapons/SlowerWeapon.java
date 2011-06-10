package com.creepsmash.client.game.weapons;

import com.creepsmash.client.game.creeps.Creep;

public class SlowerWeapon extends LaserWeapon {
	@Override
	public void attack(Creep creep) {
		super.attack(creep);
		if (this.target != null && !this.target.getType().isSlowImmun()) {
			double slowedSpeed = this.target.getType().getSpeed() * (1 - this.tower.getType().getSlowRate());
			if (this.target.getSpeed() > slowedSpeed) {
				this.target.slow(slowedSpeed, this.tower.getType().getSlowTime());
			}
		}
	}
}

package com.creepsmash.client.game.weapons;

import com.creepsmash.client.game.creeps.Creep;

public class SlowerSplashWeapon extends SplashLaserWeapon {
	@Override
	public void attack(Creep creep) {
		super.attack(creep);
		if (this.targets != null) {
			for (Creep cr: this.targets) {
				if (!cr.getType().isSlowImmun()) {
					double slowedSpeed = cr.getType().getSpeed() * (1 - this.tower.getType().getSlowRate());
					if (cr.getSpeed() > slowedSpeed) {
						cr.slow(slowedSpeed, this.tower.getType().getSlowTime());
					}
				}
			}
		}
	}

}

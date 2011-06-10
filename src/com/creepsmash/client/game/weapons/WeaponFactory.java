package com.creepsmash.client.game.weapons;


import com.creepsmash.client.Core;
import com.creepsmash.client.game.towers.Tower;

public class WeaponFactory {
	public static Weapon createWeapon(Tower tower) {
		Weapon weapon;
		switch(tower.getType().getWeaponType()) {
			case laser:
				weapon = new LaserWeapon();
				break;
			case splashlaser:
				weapon = new SplashLaserWeapon();
				break;
			case slower:
				weapon = new SlowerWeapon();
				break;
			case slowersplash:
				weapon = new SlowerSplashWeapon();
				break;
			case projectile:
				weapon = new ProjectileWeapon();
				break;
			default:
				Core.logger.warning("Unknow tower weapon type, take laser as default");
				weapon = new LaserWeapon();
				break;
		}
		weapon.init(tower);
		return weapon;
	}
}

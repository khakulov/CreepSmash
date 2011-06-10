package com.creepsmash.client.game.weapons;

import java.awt.Graphics2D;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.towers.Tower;


public interface Weapon {
	void init(Tower tower);
	void attack(Creep creep);
	void update(long round);
	void paintEffect(Graphics2D graphics);
}

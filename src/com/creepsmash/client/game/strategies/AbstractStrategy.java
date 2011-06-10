package com.creepsmash.client.game.strategies;

import java.util.ArrayList;
import java.util.List;

import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.towers.Tower;


public abstract class AbstractStrategy implements Strategy {

	private boolean locked;
	protected Tower tower;
	protected Creep lastCreep;

	//protected abstract boolean checkCreep(Creep lastCreep);

	@Override
	public void init(Tower tower) {
		this.tower = tower;
		this.lastCreep = null;
		this.locked = false;
	}

	@Override
	public void lock() {
		this.locked = true;
	}

	@Override
	public void unlock() {
		this.locked = false;
	}

	@Override
	public boolean isLocked() {
		return this.locked;
	}

	@Override
	public Creep findCreep() {
		if (this.isLocked() && this.lastCreep != null
				&& tower.getContext().getCreeps().contains(this.lastCreep)
				&& this.lastCreep.isActive()
				&& tower.isCreepInRange(this.lastCreep)) {
			return this.lastCreep;
		}
		List<Creep> creeps = new ArrayList<Creep>();
		for (Creep creep : tower.getContext().getCreeps()) {
			if (creep.isActive() && tower.isCreepInRange(creep))
				creeps.add(creep);
		}
		this.lastCreep = this.findNewCreep(creeps);
		return this.lastCreep;
	}

	protected abstract Creep findNewCreep(List<Creep> creeps);
}

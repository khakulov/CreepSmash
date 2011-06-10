package com.creepsmash.client.game.towers;

import java.awt.Graphics2D;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.game.strategies.Strategy;
import com.creepsmash.client.game.strategies.StrategyFactory;
import com.creepsmash.client.game.weapons.Weapon;
import com.creepsmash.client.game.weapons.WeaponFactory;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.TowerType;


/**
 * Implementation for a tower.
 */
public class AbstractTower implements Tower {
	private int id;
	private TowerType type;
	private Context context;
	private Grid grid;

	private boolean active;
	private boolean ready;

	private int buildTime;
	private int upgradeTime;
	private int changeStrategyTime;
	private int sellTime;

	private Strategy newStrategy;
	private Strategy strategy;
	private Weapon weapon;

	private int totalPrice;
	private int coolDown;

	private TowerRenderer renderer;
	private boolean selected;

	/**
	 * Creates a new instance of AbstractTower.
	 * @param context the gameContext for the tower
	 * @param type the type of the tower
	 * @param grid the grid where the tower is placed
	 */
	protected AbstractTower(TowerType type, Context context, Grid grid) {
		this.type = type;
		this.context = context;
		this.grid = grid;
		this.active = false;
		this.ready = false;
		this.totalPrice = type.getPrice();

		this.grid.setTower(this);

		this.renderer = new TowerRenderer(this);

		this.weapon = WeaponFactory.createWeapon(this);
		this.strategy = StrategyFactory.createStrategy(this);
	}

	@Override
	public void update(long round) {
		if (!this.active)
			return;

		// upgrade and build animation
		if (buildTime > 0) {
			assert !this.ready;
			this.buildTime--;
			if (this.buildTime == 0) {
				this.ready = true;
			} else {
				return;
			}
		} 

		//upgrade animation
		if (this.upgradeTime > 0) {
			assert !this.ready;
			this.upgradeTime--;
			if (this.upgradeTime == 0) {
				this.proceedUpgrade();
				this.ready = true;
			}
		}

		// strategy change update
		if (this.changeStrategyTime > 0) {
			assert !this.ready;
			this.changeStrategyTime--;
			if (this.changeStrategyTime == 0) {
				this.proceedChangeStrategy();
				this.ready = true;
			}
		}

		// sell animation
		if (this.sellTime > 0) {
			assert !this.ready;
			this.sellTime--;
			if (this.sellTime == 0) {
				this.proceedSell();
				return;
			}
		}

		// decrease cooldown
		if (this.coolDown > 0) {
			this.coolDown--;
		}

		// find Creep
		if (this.coolDown == 0) {
			Creep target = this.getStrategy().findCreep();
			if (target != null && target.isActive()) {
				this.weapon.attack(target);
				this.coolDown = this.type.getSpeed();
				// play shoot-sound
				if (this.context.getPlayerId() == Core.getPlayerId())
					SoundManager.towerShootsSound(this.getType());
			}
		}

		this.weapon.update(round);
	}

	@Override
	public void paint(Graphics2D graphics) {
		this.renderer.paint(graphics);
	}

	@Override
	public void paintEffect(Graphics2D graphics) {
		this.renderer.paintEffect(graphics);
		this.weapon.paintEffect(graphics);
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public TowerType getType() {
		return type;
	}

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public Grid getGrid() {
		return grid;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isReady() {
		return this.ready;
	}

	@Override
	public void build(int buildTime) {
		assert !this.active;
		assert this.ready;
		this.active = true;
		this.buildTime = buildTime;
		this.ready = false;
	}

	@Override
	public void upgrade(int upgradeTime) {
		assert this.active;
		assert this.ready;
		assert this.isUpgradable();
		this.upgradeTime = upgradeTime;
		this.ready = false;
	}

	@Override
	public boolean isUpgradable() {
		return this.type.getNext() != null;
	}

	@Override
	public void changeStrategy(int changeStrategyTime) {
		assert this.active;
		assert this.ready;
		assert this.newStrategy != null;
		this.changeStrategyTime = changeStrategyTime;
		this.ready = false;
	}

	@Override
	public void setNewStrategy(Strategy strategy) {
		assert this.newStrategy == null;
		this.newStrategy = strategy;
	}

	@Override
	public Strategy getNewStrategy() {
		return this.newStrategy;
	}

	@Override
	public Strategy getStrategy() {
		return strategy;
	}

	@Override
	public void sell(int sellTime) {
		assert this.active;
		assert this.ready;
		this.sellTime = sellTime;
		this.ready = false;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public int getTotalPrice() {
		return totalPrice;
	}

	@Override
	public boolean isCreepInRange(Creep creep) {
		return this.inRange(creep.getX() + Grid.SIZE / 2,
							creep.getY() + Grid.SIZE / 2,
							this.getGrid().getLocation()[0] + Grid.SIZE / 2,
							this.getGrid().getLocation()[1] + Grid.SIZE / 2,
				            this.type.getRange());
	}

	private void proceedUpgrade() {
		TowerType nextTower = this.type.getNext();
		
		if (nextTower != null) {
			this.totalPrice = getTotalPrice() + nextTower.getPrice();
			this.coolDown = nextTower.getSpeed();
			this.type = nextTower;

			this.renderer.loadImage();
			context.getGameBoard().clearImage();
		}

		// play music
		SoundManager.towerUpgradeSound(this.type);
	}

	private void proceedChangeStrategy() {
		assert this.newStrategy != null;
		this.strategy = this.newStrategy;
		this.newStrategy = null;
	}

	private void proceedSell() {
		this.context.setCredits(this.context.getCredits() + (int) (this.getTotalPrice() * 0.75));
		this.context.removeTower((Tower) this);
	}
	private boolean inRange(double x1, double y1, double x2, double y2, double range) {
		return (((x2-x1) * (x2-x1)) + ((y2-y1) * (y2-y1))) < (range*range);
	}

	@Override
	public int getBuildTime() {
		return this.buildTime;
	}

	@Override
	public int getUpgradeTime() {
		return this.upgradeTime;
	}

	@Override
	public int getChangeStrategyTime() {
		return this.changeStrategyTime;
	}

	@Override
	public int getSellTime() {
		return this.sellTime;
	}

	@Override
	public int getCoolDown() {
		return this.coolDown;
	}
}

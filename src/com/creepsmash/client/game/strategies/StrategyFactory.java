package com.creepsmash.client.game.strategies;

import com.creepsmash.client.game.towers.Tower;

public class StrategyFactory {
	public static Strategy createStrategy(Tower tower) {
		Strategy strategy = null;
		switch(tower.getType()) {
			case tower1:
			case tower11:
			case tower12:
			case tower13:
			case tower3:
			case tower31:
			case tower32:
			case tower33:
				strategy = new ClosestStrategy();
				break;
			case tower2:
			case tower21:
			case tower22:
			case tower23:
				strategy = new FastestStrategy();
				break;
			default:
				strategy = new StrongestStrategy();
				break;
		}
		strategy.init(tower);
		return strategy;
	}

	public static Strategy createStrategy(String strategyName, Tower tower) {
		Strategy strategy = null;
		if(strategyName.equals(ClosestStrategy.class.getSimpleName())) {
			strategy = new ClosestStrategy();
		} else if(strategyName.equals(FarthestStrategy.class.getSimpleName())) {
			strategy = new FarthestStrategy();
		} else if(strategyName.equals(FastestStrategy.class.getSimpleName())) {
			strategy = new FastestStrategy();
		} else if(strategyName.equals(StrongestStrategy.class.getSimpleName())) {
			strategy = new StrongestStrategy();
		} else {
			assert strategyName.equals(WeakestStrategy.class.getSimpleName());
			strategy = new WeakestStrategy();
		}
		strategy.init(tower);
		return strategy;
	}
}

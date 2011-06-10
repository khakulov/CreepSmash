package com.creepsmash.client.game.contexts;

public class Calculator {
/*
	private Context context;

	//private long lastRound = 0;
	private long currentRound = 0;

	private int lastCredits = IConstants.START_CREDITS;
	private int lastIncome = IConstants.START_INCOME;

	//private int currentCredits = IConstants.START_CREDITS;
	//private int currentIncome = IConstants.START_INCOME;

	private List<GameMessage> lastRoundList = new ArrayList<GameMessage>();
	private List<GameMessage> currentRoundList = new ArrayList<GameMessage>();
	
	public Calculator(Context context) {
		this.context = context;
	}
	public void update(GameMessage gameMessage, long roundId) {
		if (gameMessage.getRoundId() < this.currentRound)
			lastRoundList.add(gameMessage);
		else
			currentRoundList.add(gameMessage);
	}
	
	public void preUpdate(long roundId) {
		if (roundId % 100 == 0) {
			this.currentRound = roundId;
			//this.lastRound = roundId - 100;
		}
	}

	public void postUpdate(long roundId) {
		int [] last = this.calculate(this.lastRoundList, this.lastIncome, this.lastCredits);
		int [] curr = this.calculate(this.currentRoundList, last[0], last[1]);
		System.out.println(curr[0]);
		System.out.println(curr[1]);
	}

	public int[] calculate(List<GameMessage> list, int oldincome, int oldcredits) {
		int [] pair = new int [2];
		pair[0] = oldincome;
		pair[1] = oldcredits;

		for (GameMessage gameMessage : list) {
			if (gameMessage instanceof BuildTowerRoundMessage) {
				BuildTowerRoundMessage message = (BuildTowerRoundMessage) gameMessage;
				TowerType tower = TowerType.valueOf(
						TowerType.class, message.getTowerType());
				pair[1] -= tower.getPrice();
			} else if (gameMessage instanceof UpgradeTowerRoundMessage) {
				UpgradeTowerRoundMessage message = (UpgradeTowerRoundMessage) gameMessage;
				for (Tower tower : this.context.getTowers()) {
					if (tower.getId() == message.getTowerId()) {
						pair[1] -= tower.getType().getNext().getPrice();
					}
				}
			} else if (gameMessage instanceof SellTowerRoundMessage) {
				SellTowerRoundMessage message = (SellTowerRoundMessage) gameMessage;
				for (Tower tower : this.context.getTowers()) {
					if (tower.getId() == message.getTowerId()) {
						pair[1] += tower.getTotalPrice() * 0.75;
					}
				}
				
			} else if (gameMessage instanceof BuildCreepRoundMessage) {
				BuildCreepRoundMessage message = (BuildCreepRoundMessage) gameMessage;
				CreepType creep = CreepType.valueOf(
						CreepType.class, message.getCreepType());
				pair[0] += creep.getIncome();
				pair[1] -=  creep.getPrice();
			}
		}
		return pair;
	}
*/
}

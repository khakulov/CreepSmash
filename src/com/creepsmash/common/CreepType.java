package com.creepsmash.common;
/**
 * The type of the creep.
 */
public enum CreepType {
	// 10% income
	creep1(50, 5, 300, 70, 5, false, 0, "Mercury", "", "1.png"),
	creep2(100, 10, 700, 65, 10, false, 0, "Mako", "", "2.png"),
	creep3(250, 25, 1400, 80, 25, false, 0, "Fast Nova", "", "3.png"),
	creep4(500, 50, 3500, 50, 50, false, 0, "Large Manta", "", "4.png"),

	// 9% income
	creep5(1000, 90, 7000, 60, 90, false, 0, "Demeter", "", "5.png"),
	creep6(2000, 180, 14000, 65, 180, true, 0, "Ray", "Slow immunity", "6.png"),
	creep7(4000, 360, 30000, 90, 360, false, 0, "Speedy Raider", "fast", "7.png"),
	creep8(8000, 720, 80000, 60, 720, false, 0, "Big Toucan", "", "8.png"),

	// 8% income
	creep9(15000, 1200, 140000, 70, 1200, false, 0, "Vulture", "", "19.png"),
	creep10(25000, 2000, 250000, 75, 2000, true, 0, "Shark", "Slow immunity", "10.png"),
	creep11(40000, 3200, 500000, 100, 3200, false, 0, "Racing Mamba", "fast", "11.png"),
	creep12(60000, 4800, 1200000, 65, 4800, false, 0, "Huge Titan", "", "12.png"),

	// 7% income
	creep13(100000, 7000, 1500000, 65, 7000, false, 500, "Zeus", "Regenerates", "13.png"),
	creep14(200000, 14000, 2500000, 80, 14000, true, 0, "Phoenix", "Slow immunity", "14.png"),
	creep15(400000, 28000, 6000000, 140, 28000, false, 0, "Express Raptor", "Super fast", "15.png"),
	creep16(1000000, 56000, 15000000, 70, 56000, false, 0, "Fat Colossus", "", "16.png");

	private int price;
	private int income;
	private int health;
	private int speed;
	private int bounty;
	private boolean slowImmun;
	private int regeneration;
	private String name;
	private String special;
	private String imageFileName;
	String CREEPS_URL = "com/creepsmash/client/resources/creeps/";

	CreepType(int price, int income, int health, int speed, int bounty, boolean slowImmun, int regeneration, String name, String special, String imageFileName) {
		this.price = price;
		this.income = income;
		this.health = health;
		this.speed = speed;
		this.bounty = bounty;
		this.slowImmun = slowImmun;
		this.regeneration = regeneration;
		this.name = name;
		this.special = special;
		this.imageFileName = imageFileName;
	}

	public int getPrice() {
		return price;
	}

	public int getIncome() {
		return income;
	}

	public int getHealth() {
		return health;
	}

	public int getSpeed() {
		return speed;
	}

	public int getBounty() {
		return bounty;
	}

	public boolean isSlowImmun() {
		return slowImmun;
	}

	public int getRegeneration() {
		return regeneration;
	}

	public String getName() {
		return name;
	}

	public String getSpecial() {
		return special;
	}

	public String getImageFileName() {
		return CREEPS_URL + this.imageFileName;
	}

	public String getSpeedString() {
		return translateSpeed(speed);
	}


	/**
	 * Translates the speedValue of the creep to a human readable
	 * description.
	 * 
	 * @return the human readable string
	 * @param value
	 *            int value
	 */
	public static String translateSpeed(int value) {
		String speed = "";
		if (value > 100) {
			speed = "ultra fast";
		} else if (value > 80) {
			speed = "very fast";
		} else if (value > 70) {
			speed = "fast";
		} else if (value > 65) {
			speed = "medium";
		} else if (value > 60) {
			speed = "slow";
		} else if (value > 55) {
			speed = "very slow";
		} else {
			speed = "ultra slow";
		}
		return speed;
	}
}

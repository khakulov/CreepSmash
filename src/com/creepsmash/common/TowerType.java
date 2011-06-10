package com.creepsmash.common;

/**
 * The type of a tower.
 */
public enum TowerType {
	// price | range | speed | damage | splashRadius | splasReduction | slowRate |  slowTime
	// weapontype | next | name | special | image

	tower13(2000, 50, 13, 750, 0, 0.0, 0, 0, // 1153 dmg/s 2900
			WeaponType.laser, null, "Basictower lvl 4", "", "13.png"),
	tower12(750, 45, 13, 200, 0, 0.0, 0, 0, // 307 dmg/s 900
			WeaponType.laser, tower13, "Basictower lvl 3", "", "12.png"),
	tower11(100, 40, 13, 50, 0, 0.0, 0, 0, // 76 dmg/s 150
			WeaponType.laser, tower12, "Basictower lvl 2", "", "11.png"),
	tower1(50, 35, 13, 25, 0, 0.0, 0, 0, // 38 dmg/s 50
			WeaponType.laser, tower11, "Basictower lvl 1", "", "1.png"),

	tower23(3000, 55, 18, 100, 25, 0.7, 0.50, 50, // 111 dmg/s 3700
			WeaponType.slowersplash, null, "Slowtower lvl 4", "slows multiple targets", "23.png"),
	tower22(400, 50, 17, 75, 0, 0.0, 0.45, 50, // 88 dmg/s 700
			WeaponType.slower, tower23, "Slowtower lvl 3", "slows target", "22.png"),
	tower21(200, 45, 16, 50, 0, 0.0, 0.35, 40, // 62 dmg/s 300
			WeaponType.slower, tower22, "Slowtower lvl 2", "slows target", "21.png"),
	tower2(100, 35, 15, 25, 0, 0.0, 0.30, 40, // 33 dmg/s 100
			WeaponType.slower, tower21, "Slowtower lvl 1", "slows target", "2.png"),

	tower33(7500, 60, 10, 1100, 35, 0.5, 0, 0, // 2200 dmg/s 11150
			WeaponType.splashlaser, null, "Splashtower lvl 4", "attacks multiple targets", "33.png"),
	tower32(3000, 55, 10, 400, 35, 0.6, 0, 0, // 800 dmg/s 3650
			WeaponType.splashlaser, tower33, "Splashtower lvl 3", "attacks multiple targets", "32.png"),
	tower31(750, 45, 12, 200, 35, 0.7, 0, 0, // 333 dmg/s 1000
			WeaponType.splashlaser, tower32, "Splashtower lvl 2", "attacks multiple targets", "31.png"),
	tower3(250, 40, 15, 50, 35, 0.7, 0, 0, // 66 dmg/s 250
			WeaponType.splashlaser, tower31, "Splashtower lvl 1", "attacks multiple targets", "3.png"),

	tower43(15000, 80, 60, 15000, 35, 0.6, 0, 0, // 5000 dmg/s 26500
			WeaponType.projectile, null, "Rockettower lvl 4", "attacks multiple targets", "43.png"),
	tower42(7500, 70, 65, 7500, 30, 0.7, 0, 0, // 2307 dmg/s 11500
			WeaponType.projectile, tower43, "Rockettower lvl 3", "attacks multiple targets", "42.png"),
	tower41(3000, 60, 75, 2500, 25, 0.8, 0, 0, // 666 dmg/s 4000
			WeaponType.projectile, tower42, "Rockettower lvl 2", "attacks multiple targets", "41.png"),
	tower4(1000, 50, 75, 1000, 25, 0.8, 0, 0, // 266 dmg/s 1000
			WeaponType.projectile, tower41, "Rockettower lvl 1", "attacks multiple targets", "4.png"),

	tower53(15000, 65, 3, 1800, 0, 0.0, 0, 0, // 12000 dmg/s 26500
			WeaponType.laser, null, "Speedtower lvl 4", "", "53.png"),
	tower52(7500, 60, 5, 1100, 0, 0.0, 0, 0, // 4400 dmg/s 11500
			WeaponType.laser, tower53, "Speedtower lvl 3", "", "52.png"),
	tower51(3000, 55, 7, 450, 0, 0.0, 0, 0, // 1285 dmg/s 4000
			WeaponType.laser, tower52, "Speedtower lvl 2", "", "51.png"),
	tower5(1000, 50, 9, 225, 0, 0.0, 0, 0, // 500 dmg/s 1000
			WeaponType.laser, tower51, "Speedtower lvl 1", "", "5.png"),

	tower61(50000, 150, 50, 40000, 0, 0.0, 0, 0, // 16000 dam/s 70000
			WeaponType.laser, null, "Ultimatetower lvl 2", "", "61.png"),
	tower6(20000, 100, 100, 25000, 0, 0.0, 0, 0, // 5000 dmg/s 20000
			WeaponType.laser, tower61, "Ultimatetower lvl 1", "", "6.png");

	/**
	 * The types of damage a tower can do on creeps.
	 */
	public static enum WeaponType {
		laser,
		splashlaser,
		slower,
		slowersplash,
		projectile
		/*
		laser1(Color.BLUE, Color.WHITE),
		laser2(Color.WHITE, Color.GREEN),
		laser3(Color.GREEN, Color.WHITE),
		laser4(Color.ORANGE, Color.RED),
		slower(Color.WHITE, Color.BLUE),
		projectile(Color.WHITE, null);
		*/
	}

	private int price;
	private float range;
	private int speed;
	private int damage;
	private int splashRadius;
	private double damageReductionAtRadius;
	private double slowRate;
	private int slowTime;
	private WeaponType weaponType;
	private TowerType next;
	private String name;
	private String special;
	private String imageFileName;
	String TOWERS_URL = "com/creepsmash/client/resources/towers/";

	TowerType(int price, int range, int speed, int damage, int spashRadius,
			double damageReductionAtRadius, double slowRate, int slowTime,
			WeaponType weaponType, TowerType next,
			String name, String special, String imageFileName) {
		this.price = price;
		this.range = range;
		this.speed = speed;
		this.damage = damage;
		this.splashRadius = spashRadius;
		this.damageReductionAtRadius = damageReductionAtRadius;
		this.slowRate = slowRate;
		this.slowTime = slowTime;
		this.weaponType = weaponType;
		this.next = next;
		this.name = name;
		this.special = special;
		this.imageFileName = imageFileName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the special
	 */
	public String getSpecial() {
		return special;
	}

	/**
	 * @return the slowTime
	 */
	public int getSlowTime() {
		return slowTime;
	}

	/**
	 * @return the splashRadius
	 */
	public int getSplashRadius() {
		return splashRadius;
	}

	/**
	 * @return the damageReductionAtRadius
	 */
	public double getDamageReductionAtRadius() {
		return damageReductionAtRadius;
	}

	/**
	 * @return the slowRate
	 */
	public double getSlowRate() {
		return slowRate;
	}

	/**
	 * @return the damageType
	 */
	public WeaponType getWeaponType() {
		return weaponType;
	}

	/**
	 * getter for price of tower.
	 * 
	 * @return priece
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * getter for range of tower.
	 * 
	 * @return range
	 */
	public float getRange() {
		return range;
	}

	/**
	 * getter for speed of tower.
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * getter.
	 * 
	 * @return the speed of the tower as a string
	 */
	public String getSpeedString() {
		return translateSpeed(speed);
	}

	/**
	 * getter for damage of tower.
	 * 
	 * @return damage
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * getter.
	 * 
	 * @return next Towertype after upgrade
	 */
	public TowerType getNext() {
		return next;
	}

	/**
	 * Translates the speedValue of the tower to a human readable
	 * description.
	 * 
	 * @return the human readable string
	 * @param value
	 *            int value
	 */
	public static String translateSpeed(int value) {
		String speed = "";
		if (value > 50) {
			speed = "ultra slow";
		} else if (value > 20) {
			speed = "very slow";
		} else if (value > 15) {
			speed = "slow";
		} else if (value > 10) {
			speed = "medium";
		} else if (value > 7) {
			speed = "fast";
		} else if (value > 3) {
			speed = "very fast";
		} else {
			speed = "ultra fast";
		}
		return speed;
	}

	public String getImageFileName() {
		return TOWERS_URL + this.imageFileName;
	}
}

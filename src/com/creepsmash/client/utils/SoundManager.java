package com.creepsmash.client.utils;

import java.applet.Applet;
import java.applet.AudioClip;

import com.creepsmash.common.CreepType;
import com.creepsmash.common.TowerType;


/**
 * If you want to use sound in game, use the object
 * from this class, it's instanced in GameLoop.
 */
public class SoundManager {
	static enum Sounds implements Runnable {
		// game
		FIN("com/creepsmash/client/resources/sounds/fin.wav"),
		WON("com/creepsmash/client/resources/sounds/won.wav"),

		// Tower upgrade
		HOLY("com/creepsmash/client/resources/sounds/holy.wav"),

		// Tower shoot
		SHOOT1("com/creepsmash/client/resources/sounds/shoot1.wav"),
		SHOOT2("com/creepsmash/client/resources/sounds/shoot2.wav"),
		SHOOT3("com/creepsmash/client/resources/sounds/shoot3.wav"),
		SHOOT4("com/creepsmash/client/resources/sounds/shoot4.wav"),
		SHOOT5("com/creepsmash/client/resources/sounds/shoot5.wav"),
		SHOOT6("com/creepsmash/client/resources/sounds/shoot6.wav"),
		LASER1("com/creepsmash/client/resources/sounds/laser1.wav"),
		LASER2("com/creepsmash/client/resources/sounds/laser2.wav"),
		LASER3("com/creepsmash/client/resources/sounds/laser3.wav"),

		// Creep dead or starts
		DEAD1("com/creepsmash/client/resources/sounds/dead1.wav"),
		DEAD2("com/creepsmash/client/resources/sounds/dead2.wav"),
		DEAD3("com/creepsmash/client/resources/sounds/dead3.wav"),
		DEAD4("com/creepsmash/client/resources/sounds/dead4.wav"),
		DEAD5("com/creepsmash/client/resources/sounds/dead5.wav"),

		ESCAPE("com/creepsmash/client/resources/sounds/dcloak.wav"), // Creep escape
		WARN("com/creepsmash/client/resources/sounds/warn.wav"),
		CLAP("com/creepsmash/client/resources/sounds/clap.wav"),
		HORNBEEP("com/creepsmash/client/resources/sounds/hornbeep.wav");

		private AudioClip clip;

		Sounds(String filename) {
			this.clip = Applet.newAudioClip(SoundManager.class.getClassLoader().getResource(filename));
		}
		
		@Override
		public void run() {
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			this.clip.play();
		}
	}

	private static Thread usedThreadCreepDiesS = new Thread();
	private static Thread usedThreadCreepGoesToNextPlayerS = new Thread();
	private static Thread usedThreadCreepStartsS = new Thread();
	private static Thread usedThreadTowerShootS = new Thread();
	private static Thread usedThreadTowerUpgradeS = new Thread();
	private static boolean mute;

	/**
	 * @return the thread
	 */
	public Thread getUsedThreadCreepDiesS() {
		return usedThreadCreepDiesS;
	}

	/**
	 * @return the thread
	 */
	public Thread getUsedThreadCreepGoesToNextPlayerS() {
		return usedThreadCreepGoesToNextPlayerS;
	}

	/**
	 * @return the thread
	 */
	public Thread getUsedThreadCreepStartsS() {
		return usedThreadCreepStartsS;
	}

	/**
	 * @return the thread
	 */
	public Thread getUsedThreadTowerShootS() {
		return usedThreadTowerShootS;
	}

	/**
	 * @return the thread
	 */
	public Thread getUsedThreadTowerUpgradeS() {
		return usedThreadTowerUpgradeS;
	}

	/**
	 * Constructor plays a sound to welcome each other.
	 */
	static {
		mute = false;
		Thread t = new Thread(Sounds.HORNBEEP);
		usedThreadCreepDiesS = t;
		usedThreadCreepStartsS = t;
		usedThreadCreepGoesToNextPlayerS = t;
		usedThreadCreepStartsS = t;
		usedThreadTowerShootS = t;
		usedThreadTowerUpgradeS = t;
		t.start();
	}

	/**
	 * Toggle Mute/Play.
	 */
	public static void toggleMute() {
		mute = !mute;
	}

	/**
	 * Use this for dying creeps.
	 * 
	 * @param t
	 *            Type of creep
	 * @return true if the last thread terminated false if the last thread not
	 *         terminated
	 */
	public static void creepDiesSound(CreepType t) {
		if (mute) return;
		if (usedThreadCreepDiesS.getState() != Thread.State.TERMINATED) return;

		switch (t) {
			case creep1:
				Thread creepd1 = new Thread(Sounds.DEAD1);
				usedThreadCreepDiesS = creepd1;
				creepd1.start();
				break;
			case creep2:
				Thread creepd2 = new Thread(Sounds.DEAD2);
				usedThreadCreepDiesS = creepd2;
				creepd2.start();
				break;
			case creep3:
				Thread creepd3 = new Thread(Sounds.DEAD3);
				usedThreadCreepDiesS = creepd3;
				creepd3.start();
				break;
			case creep4:
				Thread creepd4 = new Thread(Sounds.DEAD4);
				usedThreadCreepDiesS = creepd4;
				creepd4.start();
				break;
			case creep5:
				Thread creepd5 = new Thread(Sounds.DEAD5);
				usedThreadCreepDiesS = creepd5;
				creepd5.start();
				break;
			default:
				Thread creepSixTOsixteen = new Thread(Sounds.DEAD5);
				usedThreadCreepDiesS = creepSixTOsixteen;
				creepSixTOsixteen.start();
				break;
		}
	}

	/**
	 * Use this for a creep that is just born.
	 * 
	 * @param t
	 *            Type of creep
	 * @return true if the last thread terminated false if the last thread not
	 *         terminated
	 */
	public static void creepStartsSound(CreepType t) {
		if (mute) return;
		if (usedThreadCreepStartsS.getState() != Thread.State.TERMINATED) return;

		usedThreadCreepStartsS = new Thread(Sounds.WARN);
		usedThreadCreepStartsS.start();
	}

	/**
	 * A creepy creatures walks its way.
	 * 
	 * @param t
	 *            Type of creep
	 * @return true if the last thread terminated false if the last thread not
	 *         terminated
	 */
	public static void creepGoesToNextPlayerSound(CreepType t) {
		if (mute) return;
		if (usedThreadCreepGoesToNextPlayerS.getState() != Thread.State.TERMINATED) return;

		switch (t) {
			case creep1:
				Thread creepn1 = new Thread(Sounds.ESCAPE);
				usedThreadCreepGoesToNextPlayerS = creepn1;
				creepn1.start();
				break;
			case creep2:
				Thread creepn2 = new Thread(Sounds.ESCAPE);
				usedThreadCreepGoesToNextPlayerS = creepn2;
				creepn2.start();
				break;
			case creep3:
				Thread creepn3 = new Thread(Sounds.ESCAPE);
				usedThreadCreepGoesToNextPlayerS = creepn3;
				creepn3.start();
				break;
			case creep4:
				Thread creepn4 = new Thread(Sounds.ESCAPE);
				usedThreadCreepGoesToNextPlayerS = creepn4;
				creepn4.start();
				break;
			default:
				Thread creepnDef = new Thread(Sounds.ESCAPE);
				usedThreadCreepGoesToNextPlayerS = creepnDef;
				creepnDef.start();
				break;
		}
	}

	/**
	 * Towers shoot with sound.
	 * 
	 * @param t
	 *            Type of tower
	 * @return true if the last thread terminated false if the last thread not
	 *         terminated
	 */
	public static void towerShootsSound(TowerType t) {
		if (mute) return;
		if (usedThreadTowerShootS.getState() != Thread.State.TERMINATED) return;

		switch (t) {
			case tower1:
				Thread towers1 = new Thread(Sounds.SHOOT1);
				usedThreadTowerShootS = towers1;
				towers1.start();
				break;
			case tower11:
				Thread towers11 = new Thread(Sounds.SHOOT2);
				usedThreadTowerShootS = towers11;
				towers11.start();
				break;
			case tower12:
				Thread towers12 = new Thread(Sounds.SHOOT2);
				usedThreadTowerShootS = towers12;
				towers12.start();
				break;
			case tower13:
				Thread towers13 = new Thread(Sounds.SHOOT2);
				usedThreadTowerShootS = towers13;
				towers13.start();
				break;
			case tower2:
				Thread towers2 = new Thread(Sounds.LASER1);
				usedThreadTowerShootS = towers2;
				towers2.start();
				break;
			case tower21:
				Thread towers21 = new Thread(Sounds.LASER2);
				usedThreadTowerShootS = towers21;
				towers21.start();
				break;
			case tower22:
				Thread towers22 = new Thread(Sounds.LASER2);
				usedThreadTowerShootS = towers22;
				towers22.start();
				break;
			case tower23:
				Thread towers23 = new Thread(Sounds.LASER2);
				usedThreadTowerShootS = towers23;
				towers23.start();
				break;
			case tower3:
				Thread towers3 = new Thread(Sounds.SHOOT3);
				usedThreadTowerShootS = towers3;
				towers3.start();
				break;
			case tower31:
				Thread towers31 = new Thread(Sounds.LASER3);
				usedThreadTowerShootS = towers31;
				towers31.start();
				break;
			case tower32:
				Thread towers32 = new Thread(Sounds.LASER3);
				usedThreadTowerShootS = towers32;
				towers32.start();
				break;
			case tower33:
				Thread towers33 = new Thread(Sounds.LASER3);
				usedThreadTowerShootS = towers33;
				towers33.start();
				break;
			case tower4:
				Thread towers4 = new Thread(Sounds.SHOOT4);
				usedThreadTowerShootS = towers4;
				towers4.start();
				break;
			case tower41:
				Thread towers41 = new Thread(Sounds.SHOOT4);
				usedThreadTowerShootS = towers41;
				towers41.start();
				break;
			case tower42:
				Thread towers42 = new Thread(Sounds.SHOOT4);
				usedThreadTowerShootS = towers42;
				towers42.start();
				break;
			case tower43:
				Thread towers43 = new Thread(Sounds.SHOOT4);
				usedThreadTowerShootS = towers43;
				towers43.start();
				break;
			case tower5:
				Thread towers5 = new Thread(Sounds.SHOOT5);
				usedThreadTowerShootS = towers5;
				towers5.start();
				break;
			case tower51:
				Thread towers51 = new Thread(Sounds.SHOOT5);
				usedThreadTowerShootS = towers51;
				towers51.start();
				break;
			case tower52:
				Thread towers52 = new Thread(Sounds.SHOOT5);
				usedThreadTowerShootS = towers52;
				towers52.start();
				break;
			case tower53:
				Thread towers53 = new Thread(Sounds.SHOOT5);
				usedThreadTowerShootS = towers53;
				towers53.start();
				break;
			case tower6:
				Thread towers6 = new Thread(Sounds.SHOOT6);
				usedThreadTowerShootS = towers6;
				towers6.start();
				break;
			case tower61:
				Thread towers61 = new Thread(Sounds.SHOOT6);
				usedThreadTowerShootS = towers61;
				towers61.start();
				break;
			default:
				break;
		}
	}

	/**
	 * Towers upgrade with sound.
	 * 
	 * @param t
	 *            Type of tower
	 * @return true if the last thread terminated false if the last thread not
	 *         terminated
	 */
	public static void towerUpgradeSound(TowerType t) {
		if (mute) return;
		if (usedThreadTowerUpgradeS.getState() != Thread.State.TERMINATED) return;

		switch (t) {
			case tower1:
				Thread toweru1 = new Thread(Sounds.HOLY);
				usedThreadTowerUpgradeS = toweru1;
				toweru1.start();
				break;
			case tower2:
				Thread toweru2 = new Thread(Sounds.HOLY);
				usedThreadTowerUpgradeS = toweru2;
				toweru2.start();
				break;
			case tower3:
				Thread toweru3 = new Thread(Sounds.HOLY);
				usedThreadTowerUpgradeS = toweru3;
				toweru3.start();
				break;
			case tower4:
				Thread toweru4 = new Thread(Sounds.HOLY);
				usedThreadTowerUpgradeS = toweru4;
				toweru4.start();
				break;
			default:
				Thread towerDef = new Thread(Sounds.HOLY);
				usedThreadTowerUpgradeS = towerDef;
				towerDef.start();
				break;
		}
	}

	/**
	 * Player looses.
	 * 
	 * @return if played or not
	 */
	public static void playerLooseSound() {
		if (mute) return;
		Thread loose = new Thread(Sounds.FIN);
		loose.start();
	}

	/**
	 * Player wins.
	 * 
	 * @return if played or not
	 */
	public static void playerWonSound() {
		if (mute) return;
		Thread won = new Thread(Sounds.WON);
		won.start();
	}

	/**
	 * Plays a CLAP, *knock* *knock*.
	 * 
	 * @return if played or not
	 */
	public static void clapSound() {
		if (mute) return;
		Thread clap = new Thread(Sounds.CLAP);
		clap.start();
	}

	/**
	 * Plays a nice HORNBEEP, *beep*.
	 * 
	 * @return if played or not
	 */
	public static void hornbeepSound() {
		if (mute) return;
		Thread hbeep = new Thread(Sounds.HORNBEEP);
		hbeep.start();
	}
}

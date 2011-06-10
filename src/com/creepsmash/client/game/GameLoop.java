package com.creepsmash.client.game;

import com.creepsmash.client.Core;
import com.creepsmash.common.IConstants;



/**
 * Main GameLoop for updates and repaint.
 * Sync with Server tick and frame rate approximation.
 */
public class GameLoop extends Thread {
	private static final int NO_DELAYS_PER_YIELD = 10;
	private static final int MAX_FRAME_SKIPS = 5;
	private static final long PERIOD = IConstants.TICK_MS * 1000000;

	private boolean terminate;
	private Game game;
	private long maxRound = 0;

	public GameLoop(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		Core.logger.info("GameLoop run...");

		this.terminate = false;

		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		long excess = 0L;
		int noDelays = 0;
		long oldMaxRound = 0;

		beforeTime = System.nanoTime();
		while (!this.terminate) {
			if (this.game.getRound() >= this.maxRound) {
				Thread.yield();
				continue;
			}
			if (oldMaxRound != this.maxRound) {
				if (this.game.getRound() < oldMaxRound) {
					int skips = 0;
					while ((this.game.getRound() < oldMaxRound) && (skips < MAX_FRAME_SKIPS)) {
						this.game.update();
						skips++;
					}
					Core.logger.warning("-------------------- SYNCHRONIZE " + skips + " --------------------");
				} else {
					oldMaxRound = this.maxRound;
				}
			}

			this.game.update(); // updates the gamestate
			this.game.render(); // paints new screen in a buffer
			afterTime = System.nanoTime();

			timeDiff = afterTime - beforeTime;
			sleepTime = (PERIOD - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while ((excess > PERIOD) && (skips < MAX_FRAME_SKIPS)) {
				excess -= PERIOD;
				this.game.update(); // update state but don't render
				skips++;
			}
		}

		Core.logger.info("GameLoop stop...");
	}

	public void setMaxRound(long maxRound) {
		this.maxRound = maxRound;
	}

	public synchronized void terminate() {
		this.terminate = true;
		this.interrupt();
	}
}
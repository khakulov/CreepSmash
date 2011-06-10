package com.creepsmash.server.game;

/**
 * A thread that periodically calls a TickReceiver instance's {@link
 * TickReceiver#tick tick} method.
 */
public class TickThread extends Thread {
	private volatile boolean terminate;
	private TickReceiver receiver;
	private long interval;

	/**
	 * An object that receives 'ticks'.
	 */
	public interface TickReceiver {
		/**
		 * Called periodically by TickThread.
		 */
		void tick();
	}

	/**
	 * Create the thread.
	 * 
	 * @param receiver
	 *            the TickReceiver instance
	 * @param interval
	 *            the interval (in milliseconds) between two ticks.
	 */
	public TickThread(TickReceiver receiver, long interval) {
		this.terminate = false;
		this.receiver = receiver;
		this.interval = interval;
	}

	/**
	 * Start the thread.
	 */
	@Override
	public void run() {
		long beforeTime = System.nanoTime();
		long afterTime, sleepTime, timeDiff;
		long overSleepTime = 0L;
		long excess = 0L;

		while (!this.terminate) {
			this.receiver.tick();
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (this.interval - timeDiff) - overSleepTime;
			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;
			}

			beforeTime = System.nanoTime();

			while (excess > this.interval) {
				excess -= this.interval;
				this.receiver.tick();
			}
		}
	}

	/**
	 * Ask the thread to terminate gracefully.
	 */
	public void terminate() {
		this.terminate = true;
		this.interrupt();
	}
}

package com.creepsmash.client.game.contexts;


/**
 * Interface for a ContextListener.
 * This interface should be used by all classes interested in
 * updates on game states or game information.
 */
public interface ContextListener {

	/**
	 * Invoked in case the lives changed.
	 * @param context the context the event comes from
	 */
	void livesChanged(Context context);
	
	/**
	 * Invoked in case the credits changed.
	 * @param context the context the event comes from	 
	 */
	void creditsChanged(Context context);
	
	/**
	 * Invoked in case the income changed.
	 * @param context the context the event comes from
	 */
	void incomeChanged(Context context);

	/**
	 * Invoked in case the income time changed.
	 * @param context the context the event comes from
	 * @param seconds the time to the next income
	 */
	void incomeTimeChanged(Context context, int seconds);

	/**
	 * Invoked in case the selected tower changed.
	 * @param context the context the event comes from
	 * @param message the information as message
	 */
	void selectedChanged(Context context, String message);
}

package com.creepsmash.server.game;

/**
 * An object that observes a Game.
 */
public interface GameObserverInterface {
	/**
	 * Notify the observer of a change in the game's state.
	 * 
	 * @param game
	 *            the game that changed.
	 */
	void gameStateChanged(Game game);

	/**
	 * Notify the observer of a change in the game's players.
	 * 
	 * @param game
	 *            the game that changed.
	 */
	void gamePlayersChanged(Game game);
}
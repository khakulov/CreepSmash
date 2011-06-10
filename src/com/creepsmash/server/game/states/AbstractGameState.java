package com.creepsmash.server.game.states;

import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.PlayerInGame;


/**
 * A GameState is used by a Game instance to handle incoming messages and
 * "ticks". GameState and its subclasses implement the state pattern as well as
 * the related Strategy pattern.
 */
public abstract class AbstractGameState {
	private Game game;

	/**
	 * Constructor to be called by subclasses.
	 * 
	 * @param game
	 *            the game. Must not be null.
	 */
	protected AbstractGameState(Game game) {
		if (game == null)
			throw new IllegalArgumentException("'game' was null");
		this.game = game;
	}

	/**
	 * Handle a message (from a client, presumably).
	 * 
	 * @param message
	 *            a message (from a client, presumably).
	 * @param sender
	 *            the player who sent the message.
	 * @return the new GameState.
	 */
	public abstract AbstractGameState consume(GameMessage message,
			PlayerInGame sender);

	public abstract void enter();

	public abstract void leave();

	/**
	 * Returns the game.
	 * 
	 * @return the game.
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * Handle SEND_MESSAGE.
	 * 
	 * @param m
	 *            the message.
	 */
	protected void handle(SendMessageMessage m, PlayerInGame player) {
		this.game.sendAll(new MessageMessage(player.getClient()
				.getPlayerModel().getName(), m.getMessage()));
	}
}

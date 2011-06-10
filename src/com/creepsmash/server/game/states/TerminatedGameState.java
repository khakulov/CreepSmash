package com.creepsmash.server.game.states;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.GameManager;
import com.creepsmash.server.game.PlayerInGame;


/**
 * GameState for a game that has terminated, meaning that there are no players
 * left.
 */
public class TerminatedGameState extends AbstractGameState {

	private static Logger logger = Logger.getLogger(TerminatedGameState.class);

	/**
	 * Creates a new one.
	 * @param game the game. Must not be null.
	 */
	public TerminatedGameState(Game game) {
		super(game);
	}

	/**
	 * Handle a message. All messages are ignored.
	 * @param message the message
	 * @param sender the player who sent the message.
	 * @return this.
	 */
	public AbstractGameState consume(GameMessage message, PlayerInGame sender) {
		logger.info("ignoring message: " + message);
		return this;
	}

	/**
	 * Returns a string identifying this state.
	 * @return "terminated"
	 */
	public String toString() {
		return "terminated";
	}

	@Override
	public void enter() {
		this.getGame().terminate();
		GameManager.remove(this.getGame());
	}

	@Override
	public void leave() {
	}
}
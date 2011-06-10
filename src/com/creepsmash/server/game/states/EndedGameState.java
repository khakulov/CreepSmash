package com.creepsmash.server.game.states;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.server.PlayerQuitMessage;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.PlayerInGame;


/**
 * GameState for a game that has ended, meaning that all clients have sent
 * GAME_OVER, but there are still clients lurking around. Chat still works in
 * this state, but that's about it.
 */
public class EndedGameState extends AbstractGameState {

	private static Logger logger = Logger.getLogger(EndedGameState.class);

	/**
	 * Creates a new one.
	 * 
	 * @param game
	 *            the game. Must not be null.
	 */
	public EndedGameState(Game game) {
		super(game);
	}

	/**
	 * Handle a message.
	 * 
	 * @param message
	 *            the message
	 * @param sender
	 *            the player who sent the message.
	 * @return the new state.
	 */
	public AbstractGameState consume(GameMessage message, PlayerInGame sender) {
		if (message == null)
			throw new IllegalArgumentException("'message' was null!");
		if (sender == null)
			throw new IllegalArgumentException("'sender' was null!");

		if (message instanceof ExitGameMessage)
			return removePlayer(sender);
		else if (message instanceof SendMessageMessage)
			handle((SendMessageMessage) message, sender);
		else if (message instanceof LogoutMessage)
			return removePlayer(sender);
		else
			logger.error("cannot handle message: " + message);
		return this;
	}

	/**
	 * Returns a string identifying this state.
	 * 
	 * @return "ended"
	 */
	public String toString() {
		return "ended";
	}

	@Override
	public void enter() {
	}

	@Override
	public void leave() {
	}

	private AbstractGameState removePlayer(PlayerInGame sender) {
		this.getGame().removePlayer(sender);
		this.getGame().sendAll(
				new PlayerQuitMessage(sender.getClient().getPlayerModel()
						.getName(), "", sender.getClient().getClientID()));
		if (this.getGame().getPlayersSize() == 0)
			return new TerminatedGameState(this.getGame());
		return this;
	}
}

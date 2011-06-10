package com.creepsmash.server.game.states;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.creepsmash.common.IConstants.ResponseType;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.KickPlayerRequestMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.client.StartGameRequestMessage;
import com.creepsmash.common.messages.server.KickPlayerResponseMessage;
import com.creepsmash.common.messages.server.KickedMessage;
import com.creepsmash.common.messages.server.PlayerQuitMessage;
import com.creepsmash.common.messages.server.StartGameMessage;
import com.creepsmash.common.messages.server.StartGameResponseMessage;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.GameManager;
import com.creepsmash.server.game.PlayerInGame;


/**
 * GameState for a game that has not yet started.
 */
public class WaitingGameState extends AbstractGameState {
	private Client creator;
	private static Logger logger = Logger.getLogger(WaitingGameState.class);

	/**
	 * Creates a new one, initially with zero players.
	 * 
	 * @param game
	 *            the game. Must not be null.
	 * @param creator
	 *            the client who created the game.
	 */
	public WaitingGameState(Game game, Client creator) {
		super(game);
		this.creator = creator;
	}

	/**
	 * Handle a message (from a client, presumably).
	 * 
	 * @param message
	 *            the message. Must not be null.
	 * @param sender
	 *            the player who sent the message. Must not be null.
	 * @return the new state
	 */
	public AbstractGameState consume(GameMessage message, PlayerInGame sender) {
		if (message == null)
			throw new IllegalArgumentException("'message' was null!");
		if (sender == null)
			throw new IllegalArgumentException("'sender' was null!");

		if (message instanceof StartGameRequestMessage) {
			return handle((StartGameRequestMessage) message, sender);
		} else if (message instanceof KickPlayerRequestMessage) {
			handle((KickPlayerRequestMessage) message, sender);
		} else if (message instanceof ExitGameMessage) {
			return this.removePlayer(sender);
		} else if (message instanceof LogoutMessage) {
			return this.removePlayer(sender);
		} else if (message instanceof SendMessageMessage) {
			handle((SendMessageMessage) message, sender);
		} else {
			logger.error("cannot handle message: " + message);
		}
		return this;
	}

	/**
	 * Handles the StartGameRequestMessage.
	 * 
	 * @param m
	 *            the message
	 * @param sender
	 *            the player who sent the message.
	 * @return the new state
	 */
	private AbstractGameState handle(StartGameRequestMessage m,
			PlayerInGame sender) {
		if (this.creator != sender.getClient()) {
			logger.info(sender.getClient()
					+ " tried to start game, but he isn't creator.");
			sender.getClient().send(
					new StartGameResponseMessage(ResponseType.failed));
			return this;
		}
		if (this.getGame().getPlayersSize() < this.getGame().getMaxPlayers()) {
			sender.getClient().send(
					new StartGameResponseMessage(ResponseType.failed));
			return this;
		}

		this.getGame().shufflePlayers();
		if (this.getGame().getMapId() == 0)
			this.getGame().setRandomMap();

		// Create and send the StartGameMessage to all users.
		StartGameMessage sgm = new StartGameMessage();
		List<Integer> list = new LinkedList<Integer>();
		for (PlayerInGame p : this.getGame().getPlayers())
			list.add(p.getClient().getClientID());
		sgm.setPlayers(list);
		sgm.setMapID(this.getGame().getMapId());
		this.getGame().sendAll(sgm);

		sender.getClient().send(new StartGameResponseMessage(ResponseType.ok));

		return new RunningGameState(this.getGame());
	}

	/**
	 * Handles the KickPlayerRequestMessage.
	 * 
	 * @param m
	 *            the message
	 * @param sender
	 *            the player who sent the message.
	 */
	private void handle(KickPlayerRequestMessage m, PlayerInGame sender) {
		PlayerInGame player = this.getGame().findPlayer(m.getPlayerName());
		if (player == null) {
			logger.warn("cannot find player '" + m.getPlayerName() + "'");
			sender.getClient().send(
					new KickPlayerResponseMessage(ResponseType.failed));
			return;
		}
		logger.info("kicking " + player.getClient() + " from game '"
				+ this.getGame() + "'");
		player.getClient().send(new KickedMessage());
		this.getGame().removePlayer(player);
		sender.getClient().send(new KickPlayerResponseMessage(ResponseType.ok));
	}

	private AbstractGameState removePlayer(PlayerInGame sender) {
		this.getGame().removePlayer(sender);
		if (sender.getClient() == this.creator) {
			for (PlayerInGame p : this.getGame().getPlayers()) {
				p.getClient().send(new KickedMessage());
				this.getGame().removePlayer(p);
			}
		}
		this.getGame().sendAll(
				new PlayerQuitMessage(sender.getClient().getPlayerModel()
						.getName(), "", sender.getClient().getClientID()));
		if (this.getGame().getPlayersSize() == 0)
			return new TerminatedGameState(this.getGame());
		return this;
	}

	/**
	 * Returns a string identifying this state.
	 * 
	 * @return "waiting"
	 */
	public String toString() {
		return "waiting";
	}

	@Override
	public void enter() {
		GameManager.add(this.getGame());
	}

	@Override
	public void leave() {
	}
}
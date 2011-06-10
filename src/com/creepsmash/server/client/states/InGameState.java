package com.creepsmash.server.client.states;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.server.AuthenticationService;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.Game;


/**
 * This class represents the state of the client when it is in an active game.
 */
public class InGameState extends AbstractClientState {

	private final Game game;
	private static Logger logger = Logger.getLogger(InGameState.class);
	private final AuthenticatedState authenticatedState;

	/**
	 * Constructor method instantiates the InGameState object.
	 * 
	 * @param client
	 *            Client
	 * @param game
	 *            Game
	 * @param authenticatedState
	 *            the previous state.
	 */
	public InGameState(Client client, Game game, AuthenticatedState authenticatedState) {
		super(client);
		this.game = game;
		this.authenticatedState = authenticatedState;
	}

	/**
	 * Handles the game-messages.
	 * 
	 * @param message
	 *            ClientMessage
	 * @return ClientState
	 */
	@Override
	public AbstractClientState receiveMessage(ClientMessage message) {
		if (message == null) {
			LogoutMessage m = new LogoutMessage();
			m.setClientId(this.getClient().getClientID());
			this.game.receive(m);
			AuthenticationService.logout(this.getClient());
			logger.info("client " + this.getClient()
					+ " disconnected in InGameState");
			return null;
		}
		if (message instanceof GameMessage) {
			if (message instanceof SendMessageMessage) {
				String[] msgSplit = ((SendMessageMessage) message).getMessage()
						.split(" ");
				if (msgSplit.length >= 1) {
					if ((msgSplit.length > 2)
							&& msgSplit[0].equalsIgnoreCase("/to")
							&& !this.getClient().getPlayerModel().getName()
									.equalsIgnoreCase(msgSplit[1])) {
						authenticatedState.receiveMessage(message);
						return this;
					}
				}
			}
			this.game.receive((GameMessage) message);
			if (message instanceof ExitGameMessage) {
				return this.authenticatedState;
			}
			if (message instanceof LogoutMessage) {
				AuthenticationService.logout(this.getClient());
				return this.authenticatedState.getAnonymousState();
			}
			return this;
		}
		logger.error("Wrong messagetype for GameQueue: "
				+ message.toString());
		return this;
	}

	public AbstractClientState getAuthenticatedState() {
		return this.authenticatedState;
	}

	@Override
	public void enter() {
	}

	@Override
	public void leave() {
	}
}

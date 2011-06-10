package com.creepsmash.server.client.states;

import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.server.client.Client;


/**
 * Implements the basics for the different States of a client:
 * InGameState, AnonymousState and AuthenticatedState. 
 */
public abstract class AbstractClientState {
	private Client client;

	/**
	 * Initiates the outQeue.
	 * @param outQueue
	 * Expects an BlockingQueue object with the ServerMessage.
	 * @param client Client
	 * @param authenticationService the AuthenticationService
	 */
	public AbstractClientState(Client client) {
		if (client == null)
			throw new IllegalArgumentException("'client' was null");
		this.client = client;
	}

	/**
	 * Returns the Client.
	 * @return Client
	 */
	protected Client getClient() {
		return this.client;
	}

	/**
	 * receive messages from Client. 
	 * @param message
	 * Expects a ClientMessage.
	 * @return ClientState
	 */
	public abstract AbstractClientState receiveMessage(ClientMessage message); 

	/**
	 * Enter to this state. 
	 */
	public abstract void enter();

	/**
	 * Leave from this state. 
	 */
	public abstract void leave();	
}

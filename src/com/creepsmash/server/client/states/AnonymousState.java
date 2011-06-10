package com.creepsmash.server.client.states;

import org.apache.log4j.Logger;

import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.common.messages.client.LoginRequestMessage;
import com.creepsmash.common.messages.client.RegistrationRequestMessage;
import com.creepsmash.common.messages.server.LoginResponseMessage;
import com.creepsmash.common.messages.server.RegistrationResponseMessage;
import com.creepsmash.server.AuthenticationService;
import com.creepsmash.server.Server;
import com.creepsmash.server.client.Client;


/**
 * This class represents the state of the client when it is not yet logged in.
 */
public class AnonymousState extends AbstractClientState {

	private static Logger logger = Logger.getLogger(AnonymousState.class);

	/**
	 * Constructor.
	 * 
	 * @param client
	 *            Client
	 */
	public AnonymousState(Client client) {
		super(client);
	}

	/**
	 * Receiving messages from the client.
	 * 
	 * @param message
	 *            ClientMessage
	 * @return ClientState
	 */
	@Override
	public AbstractClientState receiveMessage(ClientMessage message) {
		if (message == null) {
			logger.info("Client " + this.getClient()
					+ " disconnected in AnonymousState");
			return null;
		}
		if (message instanceof RegistrationRequestMessage) {
			RegistrationResponseMessage registrationResponseMessage = new RegistrationResponseMessage();
			registrationResponseMessage.setResponseType(AuthenticationService
					.create((RegistrationRequestMessage) message));
			this.getClient().send(registrationResponseMessage);
			return this;
		}
		if (message instanceof LoginRequestMessage) {
			LoginRequestMessage loginRequestMessage = (LoginRequestMessage) message;
			LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
			String clientVersion = loginRequestMessage.getVersion();

			// Check Version
			if (!clientVersion.equals(Server.getVersion())) {
				logger.warn("client " + this.getClient()
						+ " has wrong version: " + clientVersion);
				loginResponseMessage
						.setResponseType(IConstants.ResponseType.version);
				this.getClient().send(loginResponseMessage);
				return this;
			}

			// Check BanList
			if (AuthenticationService.isBanned(this.getClient(),
					loginRequestMessage)) {
				logger.warn("blocked user try to login: "
						+ loginRequestMessage.getUsername());
				loginResponseMessage
						.setResponseType(IConstants.ResponseType.failed);
				this.getClient().send(loginResponseMessage);
				return this;
			}

			// Check login
			if (!AuthenticationService.login(this.getClient(),
					loginRequestMessage)) {
				loginResponseMessage
						.setResponseType(IConstants.ResponseType.failed);
				this.getClient().send(loginResponseMessage);
				return this;
			}
			loginResponseMessage.setResponseType(IConstants.ResponseType.ok);
			this.getClient().send(loginResponseMessage);
			return new AuthenticatedState(this.getClient(), this);
		}

		logger.error("cannot handle message: " + message);
		return this;
	}

	@Override
	public void enter() {
		// do nothing
	}

	@Override
	public void leave() {
		// do nothing
	}
}

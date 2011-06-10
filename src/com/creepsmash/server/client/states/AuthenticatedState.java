package com.creepsmash.server.client.states;

import org.apache.log4j.Logger;

import com.creepsmash.common.Permission;
import com.creepsmash.common.IConstants.ResponseType;
import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.common.messages.client.CreateGameMessage;
import com.creepsmash.common.messages.client.DeleteRequestMessage;
import com.creepsmash.common.messages.client.HighscoreRequestMessage;
import com.creepsmash.common.messages.client.JoinGameRequestMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.RefreshMessage;
import com.creepsmash.common.messages.client.ScoreRequestMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.client.UpdateDataRequestMessage;
import com.creepsmash.common.messages.server.CreateGameResponseMessage;
import com.creepsmash.common.messages.server.DeleteResponseMessage;
import com.creepsmash.common.messages.server.JoinGameResponseMessage;
import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.common.messages.server.ScoreResponseMessage;
import com.creepsmash.common.messages.server.UpdateDataResponseMessage;
import com.creepsmash.server.AuthenticationService;
import com.creepsmash.server.HighscoreService;
import com.creepsmash.server.Lobby;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.GameManager;
import com.creepsmash.server.model.Player;


/**
 * This class represents the state of the client when it is logged in.
 */
public class AuthenticatedState extends AbstractClientState {

	private static Logger logger = Logger.getLogger(AuthenticatedState.class);
	private AnonymousState anonymousState;

	/**
	 * Constructor.
	 * 
	 * @param outQueue
	 *            BlockingQueue for the outgoing messages.
	 * @param client
	 *            Client
	 * @param lobby
	 *            Lobby
	 * @param anonymousState
	 *            the previous state.
	 * @param authenticationService
	 *            the AuthenticationService.
	 */
	public AuthenticatedState(Client client, AnonymousState anonymousState) {
		super(client);
		this.anonymousState = anonymousState;
	}

	/**
	 * Method for sending a Message.
	 * 
	 * @param message
	 *            ClientMessage
	 * @return ClientState
	 */
	@Override
	public AbstractClientState receiveMessage(ClientMessage message) {
		if (message == null) {
			AuthenticationService.logout(this.getClient());
			logger.info("client " + this.getClient() + " disconnected in AuthenticatedState");
			return null;
		}
		if (message instanceof UpdateDataRequestMessage) {
			UpdateDataResponseMessage m = new UpdateDataResponseMessage();
			m.setResponseType(AuthenticationService.update(this.getClient(), (UpdateDataRequestMessage) message));
			this.getClient().send(m);
			return this;
		}
		if (message instanceof DeleteRequestMessage) {
			DeleteResponseMessage m = new DeleteResponseMessage();
			m.setResponseType(AuthenticationService.delete(this.getClient()));
			this.getClient().send(m);
			return this;
		}
		if (message instanceof SendMessageMessage) {
			handleChatMessage(((SendMessageMessage) message).getMessage());
			return this;
		}
		if (message instanceof RefreshMessage) {
			this.getClient().send(Lobby.getPlayersMessage());
			this.getClient().send(GameManager.getGamesMessage());
			return this;
		}
		if (message instanceof ScoreRequestMessage) {
			ScoreRequestMessage requestMessage = (ScoreRequestMessage) message;
			ScoreResponseMessage responseMessage = HighscoreService.getScoreMessage(requestMessage.getPlayerName());
			this.getClient().send(responseMessage);
			return this;
		}
		if (message instanceof HighscoreRequestMessage) {
			HighscoreRequestMessage requestMessage = (HighscoreRequestMessage) message;
			this.getClient().send(HighscoreService.getHighscoreMessage(requestMessage.getStart()));
			return this;
		}
		if (message instanceof CreateGameMessage) {
			Game game = new Game(this.getClient(), (CreateGameMessage) message);
			this.getClient().send(new CreateGameResponseMessage(ResponseType.ok));
			game.addPlayer(this.getClient());
			return new InGameState(this.getClient(), game, this);
		}
		if (message instanceof JoinGameRequestMessage) {
			JoinGameRequestMessage jgrm = (JoinGameRequestMessage) message;
			Game game = GameManager.find(jgrm.getGameId());
			if ((game == null) || (!game.canPlayerJoin(this.getClient(), jgrm))) {
				logger.error("failed to join to game " + jgrm.getGameId());
				this.getClient().send(new JoinGameResponseMessage(ResponseType.failed));
				return this;
			}
			logger.info("client " + this.getClient() + " joined to game " + game);
			this.getClient().send(new JoinGameResponseMessage(ResponseType.ok));
			game.addPlayer(this.getClient());
			return new InGameState(this.getClient(), game, this);
		}
		if (message instanceof LogoutMessage) {
			AuthenticationService.logout(this.getClient());
			return this.anonymousState;
		}
		logger.error("cannot handle message: " + message);
		return this;
	}

	/**
	 * Returns the AnonymousState.
	 * 
	 * @return the AnonymousState.
	 */
	public AnonymousState getAnonymousState() {
		return this.anonymousState;
	}

	/**
	 * Process chat messages. If message starts with a slash, it is considered
	 * to be a command.
	 * 
	 * @param message
	 *            The message to process
	 */
	private void handleChatMessage(String message) {
		if (message.startsWith("/")) {
			String[] msgSplit = message.split(" ", 2);
			String param = "";
			if (msgSplit.length > 1) {
				param = msgSplit[1];
			}
			if (handleCommand(msgSplit[0], param)) {
				return;
			}
		}
		Lobby.sendAll(new MessageMessage(getClient().getPlayerModel().getName(), message));
	}

	/**
	 * Process chat commands.
	 * 
	 * @param command
	 *            The command starting with a slash
	 * @param message
	 *            Optional command parameters
	 * @return true if command was successfully processed, false otherwise.
	 */
	private boolean handleCommand(String command, String message) {
		Player player = getClient().getPlayerModel();
		if ("/to".equalsIgnoreCase(command)) {
			String[] msgSplit = message.split(" ", 2);
			if (msgSplit.length > 1 && !player.getName().equalsIgnoreCase(msgSplit[0])) {
				Player user = AuthenticationService.getPlayer(msgSplit[0]);
				if (user != null) {
					String messageStr = "<b><span style=\"color:yellow;\">" + player.getName() + " -> "
							+ user.getName() + ": " + msgSplit[1] + "</span></b>";
					if (!Lobby.sendDirectMessage(this.getClient(), user.getName(), messageStr)
							&& !GameManager.sendDirectMessage(this.getClient(), user.getName(), messageStr)) {
						sendSystemMessage(user.getName() + " is not online.");
					}
				} else {
					sendSystemMessage(msgSplit[0] + " not exist.");
				}
			}
			return true;
		}
		if ("/msg".equalsIgnoreCase(command) && player.hasPermission(Permission.MOD_CHAT)) {
			message = "Message from " + player.getName() + ": <b>" + message + "</b>";
			Lobby.sendAll(new MessageMessage("System", message));
			return true;
		}
		if ("/kick".equalsIgnoreCase(command) && player.hasPermission(Permission.KICK)) {
			Player targetPlayer = AuthenticationService.getPlayer(message);
			if (targetPlayer != null) {
				if (targetPlayer.hasPermission(Permission.KICK_IMMUN)) {
					sendSystemMessage(message + " user can't be kicked.");
				} else {
					Lobby.kickClient(targetPlayer, this.getClient(), false, false);
				}
			} else {
				sendSystemMessage(message + " user not found.");
			}
			return true;
		}
		if ("/ban".equalsIgnoreCase(command) && player.hasPermission(Permission.BAN)) {
			Player targetPlayer = AuthenticationService.getPlayer(message);
			if (targetPlayer != null) {
				if (targetPlayer.hasPermission(Permission.BAN_IMMUNE)) {
					sendSystemMessage(targetPlayer.getName() + " user can't be banned.");
				} else {
					Lobby.kickClient(targetPlayer, this.getClient(), true, true);
				}
			} else {
				sendSystemMessage(message + " user not found.");
			}
			return true;
		}
		if ("/unban".equalsIgnoreCase(command) && player.hasPermission(Permission.UNBAN)) {
			Player targetPlayer = AuthenticationService.getPlayer(message);
			if (targetPlayer != null) {
				Lobby.unBanClient(targetPlayer, this.getClient());
			} else {
				sendSystemMessage(message + " user not found.");
			}
			return true;
		}
		return false;
	}

	/**
	 * Notify the current user with a system message.
	 * 
	 * @param message The message
	 */
	private void sendSystemMessage(String message) {
		this.getClient().send(new MessageMessage("System", message));
	}

	@Override
	public void enter() {
		Lobby.add(this.getClient());
	}

	@Override
	public void leave() {
		Lobby.remove(this.getClient());
	}
}

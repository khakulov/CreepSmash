package com.creepsmash.server.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.GamesMessage;
import com.creepsmash.server.Lobby;
import com.creepsmash.server.client.Client;


public class GameManager implements GameObserverInterface {

	private static Logger logger = Logger.getLogger(GameManager.class);
	private static List<Game> games = new LinkedList<Game>();
	private static final GameManager INSTANCE = new GameManager();

	private GameManager() { }

	public static void add(Game game) {
		if (game == null)
			throw new IllegalArgumentException("'newClient' was null");
		synchronized(games) {
			if (games.contains(game))
				return;
			games.add(game);
		}
		game.addObserver(INSTANCE);
		Lobby.sendAll(getGamesMessage());
		logger.info("game added: " + game);
	}

	public static void remove(Game game) {
		synchronized(games) {
			if (games.contains(game))
				games.remove(game);
		}
		Lobby.sendAll(getGamesMessage());
		logger.info("game removed: " + game);
	}

	/**
	 * Find the game with the given id.
	 * @param gameId the gameId to look for.
	 * @return the game, of null if it could not be found.
	 */
	public static Game find(int gameId) {
		synchronized(games) {
			for (Game game : games) {
				if (game.getGameId() == gameId)
					return game;
			}
		}
		return null;
	}

	/**
	 * Creates a GAMES message with the current list of games.
	 * @return the message
	 */
	public static GamesMessage getGamesMessage() {
		Set<GameDescription> gameDescriptions = new HashSet<GameDescription>();
		synchronized(games) {
			for (Game game : games) {
				gameDescriptions.add(game.getGameDescription());
			}
		}
		return new GamesMessage(gameDescriptions);
	}

	public static boolean sendDirectMessage(Client sender, String receiverName, String message) {
		/* TODO synchronized(games) {
			for (Game game : games) {
				synchronized(game.getClients()) {
					for (PlayerInGame player : game.getClients()) {
						if (player.getClient().getPlayerModel().getName().equals(receiverName)) {
							player.getClient().send(new MessageMessage("System", message));
							sender.send(new MessageMessage("System", message));
							return true;
						}
					}
				}
			}
		}*/
		return false;
	}
	
	public static boolean kickClient(String username, Client adminClient) {
		/* TODO synchronized(games) {
			for (Game game : games) {
				for (PlayerInGame player : game.getClients()) {
					if (player.getClient().getPlayerModel().getName().equals(username)) {
						logger.info("Kick Player inGame: " + player.getClient().getPlayerModel().getName());
						game.sendAll(new MessageMessage("System",
										"<span style=\"color:red;\">"
												+ player.getClient().getPlayerModel().getName()
												+ " was kicked by <b>"
												+ adminClient.getPlayerModel().getName()
												+ "</b></span>"));
						game.sendAll(new MessageMessage("System", player
								.getClient().getPlayerModel().getName()
								+ " has left..."));
						//game.removeClient(player.getClient());
	
						if (game.getClients().isEmpty()) {
							game.shutdown();
							//game.gameTerminated();
						} else {
							game.sendAll(new PlayerQuitMessage(username,
									"Kick", player.getClient().getClientID()));
							game.gamePlayersChanged();
						}
	
						player.getClient().disconnect();
	
						adminClient
								.send(new MessageMessage("System",
										"<span style=\"color:red;\">"
												+ player.getClient().getPlayerModel().getName()
												+ " was kicked by <b>"
												+ adminClient.getPlayerModel().getName()
												+ "</b></span>"));
						return true;
					}
				}
			}
		} */
		return false;
	}

	/**
	 * A game's players changed.
	 * @param game the game that changed.
	 */
	@Override
	public void gamePlayersChanged(Game game) {
		Lobby.sendAll(getGamesMessage());
	}

	/**
	 * A game's state changed.
	 * @param game the game that changed.
	 */
	@Override
	public void gameStateChanged(Game game) {
		Lobby.sendAll(getGamesMessage());
	}
}

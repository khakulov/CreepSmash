package com.creepsmash.server;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.common.messages.server.PlayersMessage;
import com.creepsmash.common.messages.server.ServerMessage;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.GameManager;
import com.creepsmash.server.model.BlackList;
import com.creepsmash.server.model.Player;

import java.util.Hashtable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


/**
 * The lobby manages games and clients who are authenticated but not currently
 * playing.
 */
public class Lobby {
	private static Logger logger = Logger.getLogger(Lobby.class);
	private static List<Client> clients = new LinkedList<Client>();

	private Lobby() {}

	/**
	 * Join the lobby.
	 * @param newClient the client who wants to join the lobby.
	 */
	public static void add(Client client) {
		if (client == null)
			throw new IllegalArgumentException("'newClient' was null");
		synchronized(clients) {
			if (clients.contains(client))
				return;
			clients.add(client);
		}
		client.send(GameManager.getGamesMessage());
		sendAll(getPlayersMessage());
		logger.info("client enter the lobby: " + client);
	}

	/**
	 * Allows a client to leave the lobby.
	 * @param client the client. Must not be null.
	 */
	public static void remove(Client client) {
		if (client == null)
			throw new IllegalArgumentException("'client' was null");
		synchronized(clients) {
			if (!clients.contains(client))
				return;
			clients.remove(client);
		}
		sendAll(getPlayersMessage());
		logger.info("client left the lobby: " + client);
	}

	public static Client find(String username) {
		if ((username==null) || username.equals(""))
			throw new IllegalArgumentException("'username' was null or empty");
		synchronized(clients) {
			for (Client client : clients) {
				if (client.getPlayerModel().getName().equalsIgnoreCase(username)) {
					return client;
				}
			}
		}
		return null;
	}

	/**
	 * Send a message to all clients in the lobby.
	 * @param message the message to send.
	 */
	public static void sendAll(ServerMessage message) {
		if (message == null)
			throw new IllegalArgumentException("'message' was null");
		synchronized(clients) {
			for (Client client : clients) {
				client.send(message);
			}
		}
	}

	/**
	 * Creates a PLAYERS message with the current list of clients.
	 * @return the message
	 */
	public static PlayersMessage getPlayersMessage() {
		Hashtable<String, Integer> playerNames = new Hashtable<String, Integer>();
		List<Client> clientsToRemove = new LinkedList<Client>();
		synchronized(clients) {
			for (Client client : clients) {
				if (client.check()) {
					playerNames.put(client.getPlayerModel().getName(), client.getPlayerModel().getElopoints() - 500);
				} else {
					logger.info("PlayersMessage/leaveLobby: " + client);
					clientsToRemove.add(client);
				}
			}
			for (Client client : clientsToRemove) {
				clients.remove(client);
			}
		}
		return new PlayersMessage(playerNames);
	}

	/**
	 * 
	 * @param sender
	 * @param receiverName
	 * @param message
	 * @return boolean
	 */
	public static boolean sendDirectMessage(Client sender, String receiverName, String message) {
		synchronized(clients) {
			for (Client client : clients) {
				if (client.getPlayerModel().getName().equalsIgnoreCase(receiverName)) {
					client.send(new MessageMessage("System", message));
					sender.send(new MessageMessage("System", message));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Find the Player and Kick/Ban from Server
	 * @param player the name to look for.
	 * @param adminClient Moderator Client
	 * @param banUser the user account
	 * @param banUserAndMac the user account and add MAC to the Blacklist
	 * @return boolean
	 */
	public static boolean kickClient(Player player, Client adminClient, boolean banUser, boolean banUserAndMac) {
		boolean kik = GameManager.kickClient(player.getName(), adminClient);
		if (kik == false) {
			Client kickedClient = null;
			synchronized(clients) {
				for (Client client : clients) {
					if (client.getPlayerModel().getName().equalsIgnoreCase(player.getName())) {
						kickedClient = client;
						break;
					}
				}
			}
			if (kickedClient != null) {
				logger.info("Kick Player inLobby: " + kickedClient);
				sendAll(new MessageMessage("System",
						"<span style=\"color:red;\">"
								+ kickedClient.getPlayerModel().getName()
								+ " was kicked by <b>"
								+ adminClient.getPlayerModel().getName() + "</b></span>"));
				kickedClient.disconnect();
				kik = true;
			}
		}
		if (kik == false) {
			adminClient.send(new MessageMessage("System",
					"<span style=\"color:red;\"> " + player.getName()
							+ " User is not Online!</span>"));
		} 
		if (banUser == true) {
				player.setBlocked(true);
				try {
				EntityManager entityManager = 
					PersistenceManager.getInstance().getEntityManager();
				EntityTransaction entityTransaction = entityManager
					.getTransaction();
				entityTransaction.begin();	
				entityManager.merge(player);
				entityManager.flush();
				entityTransaction.commit();	
				adminClient.send(new MessageMessage("System","<span style=\"color:red;\">"
													+ player.getName()
													+ " (Account) has been banned!</span>"));
				
				logger.debug("Block for User " + player.getName() + " / "
						+ player.getMac() + " saved.");
			} catch (Throwable t) {
				logger.error("error while saving block for User "
						+ player.getName() + " / " + player.getMac() + " ",	t);
			}
		}
		if (banUserAndMac == true) {
				adminClient.send(new MessageMessage("System","<span style=\"color:red;\">"
						+ player.getName()
						+ " (Mac) has been blacklisted!</span>"));
				
				try {
					EntityManager entityManager = PersistenceManager
							.getInstance().getEntityManager();
					EntityTransaction entityTransaction = entityManager
							.getTransaction();
					entityTransaction.begin();
					BlackList blacklist = new BlackList();
					blacklist.setData(player.getMac());
					entityManager.persist(blacklist);
					entityManager.flush();
					entityTransaction.commit();

					logger.debug("Block for MAC " + player.getName() + " / "
							+ player.getMac() + " saved.");
				} catch (Throwable t) {
					logger.error("error while saving block for MAC "
							+ player.getName() + " / " + player.getMac() + " ",	t);
				}
		}
		return kik;
	}
	/**
	 * Find the Player and unBan and Remove from Blacklist
	 * @param player the name to look for.
	 * @param adminClient Moderator Client
	 * @return boolean
	 */
	public static boolean unBanClient(Player player, Client adminClient) {
		boolean retrun = false;
		try {
			EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			BlackList blacklist = entityManager.find(BlackList.class, player.getMac());

			if (blacklist != null) {
				entityManager.remove(blacklist);
				adminClient.send(new MessageMessage("System","<span style=\"color:red;\">"
						+ player.getName()
						+ " (Mac) has been unbanned !</span>"));
				retrun = true;
			}
			entityTransaction.commit();
		} catch (Throwable t) {
			logger.error("error while remove block for User "
					+ player.getName() + " / " + player.getMac() + " ",	t);
		}
		
		try {
			player.setBlocked(false);
			EntityManager entityManager = 
				PersistenceManager.getInstance().getEntityManager();
			EntityTransaction entityTransaction = entityManager
				.getTransaction();
			entityTransaction.begin();	
			entityManager.merge(player);
			entityManager.flush();
			entityTransaction.commit();	
			adminClient.send(new MessageMessage("System","<span style=\"color:red;\">"
					+ player.getName()
					+ " (User) has been unbanned !</span>"));
			
			logger.debug("undBlock for User " + player.getName() + " / "
					+ player.getMac() + " removed.");
		} catch (Throwable t) {
			logger.error("error while saving undBlock for User "
					+ player.getName() + " / " + player.getMac() + " ",	t);
		}
		return retrun;
	}
}
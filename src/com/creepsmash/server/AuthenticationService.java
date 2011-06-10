package com.creepsmash.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.LoginRequestMessage;
import com.creepsmash.common.messages.client.RegistrationRequestMessage;
import com.creepsmash.common.messages.client.UpdateDataRequestMessage;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.model.BlackList;
import com.creepsmash.server.model.Player;


/**
 * Service for player-management. Persists player-data to the database.
 */
public class AuthenticationService {

	private static Logger logger = Logger
			.getLogger(AuthenticationService.class);

	private static Set<String> loggedIn;

	private static final String QUERY_PLAYERS_ORDERBY_ELO = "SELECT player FROM Player AS player ORDER BY player.elopoints DESC";

	private static final String QUERY_BANLIST = "SELECT * FROM BlackList WHERE data IN ";

	static {
		loggedIn = new HashSet<String>();
	}

	private AuthenticationService() {
	}

	/**
	 * @param playerName
	 *            the name of the player
	 * @param password
	 *            the password of the player
	 * @param email
	 *            the email of the player
	 * @return ok if successful, username if the username is in use, failed for
	 *         other errors.
	 */
	public static IConstants.ResponseType create(RegistrationRequestMessage registrationRequestMessage) {

		if (AuthenticationService.getPlayer(registrationRequestMessage.getUsername()) != null) {
			logger.error("registration failed (username used)");
			return IConstants.ResponseType.username;
		}

		try {
			EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();

			Player player = new Player();
			player.setName(registrationRequestMessage.getUsername());
			player.setPassword(registrationRequestMessage.getPassword());
			player.setEmail(registrationRequestMessage.getEmail());
			player.setElopoints(500);
			player.setOldElopoints(500);

			entityManager.persist(player);
			entityManager.flush();
			entityTransaction.commit();
		} catch (Exception e) {
			logger.error("registration failed");
			logger.debug(e.getLocalizedMessage());
			return IConstants.ResponseType.failed;

		}
		logger.info("new player registered");
		return IConstants.ResponseType.ok;
	}

	/**
	 * @param playerName
	 *            the name of the player to update
	 * @param oldPassword
	 *            the old password
	 * @param password
	 *            the new password
	 * @param mail
	 *            the new mailadress
	 * @return the response type for ResponseMessage
	 */
	public static IConstants.ResponseType update(Client client,
			UpdateDataRequestMessage updateDataRequestMessage) {
		boolean update = false;
		IConstants.ResponseType responseType = IConstants.ResponseType.failed;
		EntityManager entityManager = PersistenceManager.getInstance()
				.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Player player = entityManager.find(Player.class, client
				.getPlayerModel().getName());
		if (player != null) {
			String oldPassword = updateDataRequestMessage.getOldPassword();
			String password = updateDataRequestMessage.getPassword();
			String mail = updateDataRequestMessage.getEmail();
			if ((oldPassword != null) && (oldPassword.length() > 0)
					&& (password != null) && (password.length() > 0)) {
				if (player.getPassword().equals(oldPassword)) {
					player.setPassword(password);
					update = true;
				}
			}
			if ((mail != null) && (mail.length() > 0)) {
				player.setEmail(mail);
				update = true;
			} else {
				logger.error("password not changed, "
						+ "because old password was wrong");
			}

			if (update) {
				entityManager.merge(player);
				entityManager.flush();
				responseType = IConstants.ResponseType.ok;
				logger.info("registartion data for client " + client
						+ " changed.");
			}
		} else {
			logger.error("player with client " + client + " was not found.");
		}
		entityTransaction.commit();
		return responseType;
	}

	/**
	 * @param playerName
	 *            the name of the player
	 * @return the response type
	 */
	public static IConstants.ResponseType delete(Client client) {
		IConstants.ResponseType responseType = IConstants.ResponseType.failed;
		if (client.getPlayerModel() != null) {
			EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			entityManager.remove(entityManager.find(Player.class, client.getPlayerModel().getName()));
			entityManager.flush();
			entityTransaction.commit();
			responseType = IConstants.ResponseType.ok;
		}
		return responseType;
	}

	/**
	 * @param playerName
	 *            the player's name
	 * @param password
	 *            the password
	 * @return the lobby, of null if the login failed
	 */
	public static boolean login(Client client,
			LoginRequestMessage loginRequestMessage) {
		EntityManager entityManager = PersistenceManager.getInstance()
				.getEntityManager();

		Player player = entityManager.find(Player.class, loginRequestMessage
				.getUsername());

		if ((player != null)) {
			for (String UserName : loggedIn) {
				if (UserName.equalsIgnoreCase(player.getName())) {
					logger.info(player.getName() + " tried to log in twice");
					return false;
				}
			}
			if (player.getPassword().equals(loginRequestMessage.getPassword())
					&& !player.isBlocked()) {
				loggedIn.add(player.getName());
				logger.info(player.getName() + " logged in successfully");

				EntityTransaction entityTransaction = entityManager
						.getTransaction();
				entityTransaction.begin();
				player.setLastlogin(System.currentTimeMillis() / 1000L);
				player.setIp(client.getIPAddress());
				player.setMac(loginRequestMessage.getMacaddress());
				entityManager.merge(player);
				entityManager.flush();
				entityTransaction.commit();

				client.setPlayerModel(player);
				return true;
			}
		}
		logger.info("login failed");
		return false;
	}

	/**
	 * Log out.
	 * 
	 * @param playerName
	 *            the name of the player who wants to log out.
	 */
	public static void logout(Client client) {
		if (loggedIn.remove(client.getPlayerModel().getName())) {
			logger.info(client + " logged out");
		} else {
			logger.warn(client + " tried to log out, but wasn't logged in!");
		}
	}

	/**
	 * @param firstResult
	 *            the index of the first result returned.
	 * @return 30 players sorted by elopoints starting with firstResult.
	 */
	public static Set<Player> getPlayers(int firstResult) {
		EntityManager entityManager = PersistenceManager.getInstance()
				.getEntityManager();

		Query query = entityManager.createQuery(QUERY_PLAYERS_ORDERBY_ELO);
		query.setMaxResults(30);
		query.setFirstResult(firstResult);

		List<?> resultList = query.getResultList();

		Set<Player> players = new HashSet<Player>();
		if ((resultList != null) && (resultList.size() > 0)) {
			for (Object o : resultList) {
				Player player = (Player) o;
				players.add(player);
			}
		}
		return players;
	}

	/**
	 * @param playerName
	 *            the name of a player
	 * @return the player with playerName
	 */
	public static Player getPlayer(String playerName) {
		EntityManager entityManager = PersistenceManager.getInstance()
				.getEntityManager();

		Player player = null;
		try {
			player = entityManager.find(Player.class, playerName);
		} catch (PersistenceException e) {
			logger.error("DB Connection lost ?");
		}
		return player;
	}

	public static boolean isBanned(Client client,
			LoginRequestMessage loginRequestMessage) {
		EntityManager entityManager = PersistenceManager.getInstance()
				.getEntityManager();
		String queryString = QUERY_BANLIST + "('" + client.getIPAddress()
				+ "', '" + loginRequestMessage.getMacaddress() + "')";
		Query query = entityManager.createNativeQuery(queryString,
				BlackList.class);
		List<?> resultList = query.getResultList();
		if ((resultList != null) && (resultList.size() > 0)) {
			return true;
		}
		return false;
	}
}

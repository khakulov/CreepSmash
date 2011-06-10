package com.creepsmash.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.server.HighscoreEntry;
import com.creepsmash.common.messages.server.HighscoreResponseMessage;
import com.creepsmash.common.messages.server.ScoreResponseMessage;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.PlayerInGame;
import com.creepsmash.server.model.Player;


/**
 * 
 * Service for the High scores.
 * 
 */
public class HighscoreService {

	private static Logger logger = Logger.getLogger(HighscoreService.class);

	private HighscoreService() {
	};

	public static class ExtendedPlayer {
		private Player player;
		private int postion;

		public ExtendedPlayer(Player player, int postion) {
			super();
			this.player = player;
			this.postion = postion;
		}

		public Player getPlayer() {
			return this.player;
		}

		public int getPostion() {
			return this.postion;
		}
	}

	/**
	 * @param playerName
	 *            the username.
	 * @return a ScoreResponseMessage with the user's data.
	 */
	public static ScoreResponseMessage getScoreMessage(String playerName) {
		ScoreResponseMessage scoreResponseMessage = new ScoreResponseMessage();
		Player player = AuthenticationService.getPlayer(playerName);
		if (player != null) {
			scoreResponseMessage.setPlayerName(player.getName());
			scoreResponseMessage.setPoints(player.getElopoints() - 500);
			scoreResponseMessage.setOldPoints(player.getOldElopoints() - 500);
		}
		return scoreResponseMessage;
	}

	/**
	 * @param offset
	 * @return the actual HighscoreResponseMessage to send to clients.
	 */
	public static HighscoreResponseMessage getHighscoreMessage(int offset) {
		HighscoreResponseMessage highscoreResponseMessage = new HighscoreResponseMessage();
		Set<HighscoreEntry> highscoreEntries = new HashSet<HighscoreEntry>();
		Set<Player> players = AuthenticationService.getPlayers(offset);
		for (Player player : players) {
			HighscoreEntry highscoreEntry = new HighscoreEntry();
			highscoreEntry.setPlayerName(player.getName());
			highscoreEntry.setPoints(player.getElopoints() - 500);
			highscoreEntry.setOldPoints(player.getOldElopoints() - 500);
			highscoreEntries.add(highscoreEntry);
		}
		highscoreResponseMessage.setHighscoreEntries(highscoreEntries);
		return highscoreResponseMessage;
	}

	/**
	 * Updates the highscore table with the results of a game.
	 * 
	 * @param playerNamePositionMap
	 *            maps player names to position (position 1 means the player won
	 *            the game etc)
	 */
	public static void createHighscoreEntry(Map<PlayerInGame, Integer> playerPositionMap) {
		try {
			EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();

			HashSet<ExtendedPlayer> players = new HashSet<ExtendedPlayer>();
			for (PlayerInGame player : playerPositionMap.keySet()) {
				Client client = player.getClient();
				if (client.getPlayerModel() != null) {
					players.add(new ExtendedPlayer(client.getPlayerModel(),	playerPositionMap.get(client)));
				}
			}

			ArrayList<ExtendedPlayer> pl = new ArrayList<ExtendedPlayer>(players);
			Collections.sort(pl, new Comparator<ExtendedPlayer>() {
				public int compare(ExtendedPlayer p1, ExtendedPlayer p2) {
					return p1.getPostion() - p2.getPostion();
				}
			});

			double[] eloPoints = new double[pl.size()];
			for (int i = 0; i < pl.size(); i++) {
				eloPoints[i] = pl.get(i).getPlayer().getElopoints();
			}

			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();

			double[] newEloPoints = calcEloPoints(eloPoints);
			for (int i = 0; i < newEloPoints.length; i++) {
				pl.get(i).player.setOldElopoints((int) newEloPoints[i]
						- (pl.get(i).player.getElopoints() - 500));
				pl.get(i).player.setElopoints((int) newEloPoints[i]);
				entityManager.merge(pl.get(i).getPlayer());
			}

			entityTransaction.commit();
			logger.debug("highscores saved");
		} catch (Throwable t) {
			logger.error("error while saving highscores", t);
		}
	}

	public static void clearOldPoints(Set<PlayerInGame> playerPositionMap) {
		EntityManager entityManager = PersistenceManager.getInstance().getEntityManager();
		for (PlayerInGame p : playerPositionMap) {
			Player player = entityManager.find(Player.class, p.getClient().getPlayerModel().getName());
			EntityTransaction entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			player.setOldElopoints(500);
			p.getClient().setPlayerModel(player);
			entityManager.merge(player);
			entityTransaction.commit();
		}
	}
	/**
	 * @param pl
	 * @param ePoints
	 * 
	 */
	private static double[] calcEloPoints(double[] eloPoints) {
		double[] probabilities = calceProbabilities(eloPoints);
		double[] newEloPoints = new double[eloPoints.length];

		for (int j = 0; j < eloPoints.length; j++)
			newEloPoints[j] = getPoint(eloPoints[j], 3 - j, probabilities[j], 4);
		return newEloPoints;
	}

	/**
	 * @param pl
	 * @param ePoints
	 */
	private static double[] calceProbabilities(double[] ePoints) {
		double[] eVal = new double[ePoints.length];
		if (ePoints.length == 4) {
			eVal[0] = getProbability(ePoints[0], ePoints[1])
					+ getProbability(ePoints[0], ePoints[2])
					+ getProbability(ePoints[0], ePoints[3]);
			eVal[1] = getProbability(ePoints[1], ePoints[0])
					+ getProbability(ePoints[1], ePoints[2])
					+ getProbability(ePoints[1], ePoints[3]);
			eVal[2] = getProbability(ePoints[2], ePoints[0])
					+ getProbability(ePoints[2], ePoints[1])
					+ getProbability(ePoints[2], ePoints[3]);
			eVal[3] = getProbability(ePoints[3], ePoints[0])
					+ getProbability(ePoints[3], ePoints[1])
					+ getProbability(ePoints[3], ePoints[2]);
			return eVal;
		}
		if (ePoints.length == 3) {
			eVal[0] = getProbability(ePoints[0], ePoints[1]) + getProbability(ePoints[0], ePoints[2]);
			eVal[1] = getProbability(ePoints[1], ePoints[0]) + getProbability(ePoints[1], ePoints[2]);
			eVal[2] = getProbability(ePoints[2], ePoints[0]) + getProbability(ePoints[1], ePoints[1]);
			return eVal;
		}
		if (ePoints.length == 2) {
			eVal[0] = getProbability(ePoints[0], ePoints[1]);
			eVal[1] = getProbability(ePoints[1], ePoints[0]);
		}
		return eVal;
	}

	private static double getProbability(double p1, double p2) {
		return 1/(1 + Math.pow(10, (p2-p1)/400));
	}

	private static double getPoint(double ro, double w, double we, double n) {
		double rn = ro + 800 * ((w - we) / (Math.pow(ro / 1000, n) + 9 + n));
		if (rn < 500) rn = 500;
		return rn;
	}
}

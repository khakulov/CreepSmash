package com.creepsmash.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;

import com.creepsmash.common.GameMap;
import com.creepsmash.common.messages.client.CreateGameMessage;
import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.JoinGameRequestMessage;
import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.PlayerJoinedMessage;
import com.creepsmash.common.messages.server.ServerMessage;
import com.creepsmash.server.PersistenceManager;
import com.creepsmash.server.client.Client;
import com.creepsmash.server.game.states.AbstractGameState;
import com.creepsmash.server.game.states.WaitingGameState;
import com.creepsmash.server.model.GameJournal;


/**
 * Coordinates a game of Tower Defence. This class's job is mainly coordinating
 * the various queues and threads, handling message and other events is
 * delegated to instances of {@link AbstractGameState GameState}.
 */
public class Game extends AbstractGame {
	private static Logger logger = Logger.getLogger(Game.class);

	private AbstractGameState gameState;
	private List<PlayerInGame> players;
	private List<GameObserverInterface> observers;

	private BlockingQueue<GameMessage> queue;
	private volatile boolean terminate = false;

	/**
	 * Create a new game with the given gameId. This sets up the queue and the
	 * thread that takes messages from the queue.
	 * 
	 * @param gameId
	 *            the game's id.
	 */
	public Game(Client client, CreateGameMessage message) {
		super(message);

		this.players = new ArrayList<PlayerInGame>();
		this.observers = new LinkedList<GameObserverInterface>();
		this.queue = new LinkedBlockingQueue<GameMessage>();

		this.changeState(new WaitingGameState(this, client));

		this.setName("Game: " + this);
		this.start();
		logger.debug("New game created: " + this);
	}

	@Override
	public void run() {
		while (!this.terminate) {
			try {
				GameMessage message = queue.take();
				if (message == null) {
					logger.error("Game " + this + ": shutdown...");
					this.terminate();
				}
				PlayerInGame p = this.findPlayer(message.getClientId());
				this.changeState(this.getGameState().consume(message, p));
			} catch (InterruptedException e) {
				// do nothing
			}
		}
	}

	public synchronized void terminate() {
		this.terminate = true;
		this.interrupt();
	}

	public void receive(GameMessage message) {
		if (message == null)
			throw new IllegalArgumentException("'message' was null!");

		PlayerInGame sender = this.findPlayer(message.getClientId());
		if (sender == null) {
			logger.error("got message from client " + message.getClientId()
					+ ", but that client is not in the game.");
			return;
		}
		this.queue.add(message);
	}

	private void changeState(AbstractGameState newGameState) {
		if (newGameState == null)
			return;

		AbstractGameState oldGameState = this.gameState;
		logger.debug("Changing state of game " + this + " from " + oldGameState
				+ " to " + newGameState);
		if (oldGameState != null) {
			if (newGameState.getClass().equals(oldGameState.getClass()))
				return;
			oldGameState.leave();
		}

		this.gameState = newGameState;
		this.gameState.enter();

		gameStateChanged();
		logger.debug("GameStatus changed");
	}

	protected AbstractGameState getGameState() {
		return this.gameState;
	}

	public void sendAll(ServerMessage message) {
		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
				p.getClient().send(message);
			}
		}
	}

	public void addPlayer(Client client) {
		if (client == null)
			throw new IllegalArgumentException("'newClient' was null!");
		if (!(this.getGameState() instanceof WaitingGameState))
			throw new RuntimeException(
					"game has started--no more players can join "
							+ this.getGameState());
		if (this.getPlayersSize() >= this.getMaxPlayers())
			throw new RuntimeException(
					"max # of players reached--no more players can join");
		if (this.findPlayer(client.getClientID()) != null)
			return;

		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
				client.send(new PlayerJoinedMessage(p.getClient()
						.getPlayerModel().getName(), p.getClient()
						.getClientID(), p.getClient().getPlayerModel()
						.getElopoints() - 500));
			}
			this.players.add(new PlayerInGame(client));
		}

		this.sendAll(new PlayerJoinedMessage(client.getPlayerModel().getName(),
				client.getClientID(),
				client.getPlayerModel().getElopoints() - 500));
		gamePlayersChanged();
		logger.debug("Player joined to the game");
	}

	public void removePlayer(PlayerInGame player) {
		if (player == null)
			throw new IllegalArgumentException("'player' was null!");
		synchronized (this.players) {
			this.players.remove(player);
		}
		gamePlayersChanged();
	}

	public void removePlayer(Client client) {
		if (client == null)
			throw new IllegalArgumentException("'client' was null!");
		this.removePlayer(this.findPlayer(client.getClientID()));
	}

	public int getPlayersSize() {
		synchronized (this.players) {
			return this.players.size();
		}
	}

	public List<PlayerInGame> getPlayers() {
		List<PlayerInGame> list = new ArrayList<PlayerInGame>();
		synchronized (this.players) {
			for (PlayerInGame p : this.players)
				list.add(p);
		}
		return list;
	}

	public PlayerInGame findPlayer(int clientId) {
		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
				if (p.getClient().getClientID() == clientId)
					return p;
			}
		}
		return null;
	}

	public PlayerInGame findPlayer(String playerName) {
		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
				if (p.getClient().getPlayerModel().getName().equals(playerName)) {
					return p;
				}
			}
		}
		return null;
	}

	public void shufflePlayers() {
		synchronized (this.players) {
			Collections.shuffle(this.players);
		}
	}

	public GameDescription getGameDescription() {
		String player1 = "";
		String player2 = "";
		String player3 = "";
		String player4 = "";

		int count = 0;
		int score = 0;
		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
				if (count == 0)
					player1 = p.getClient().getPlayerModel().getName();
				else if (count == 1)
					player2 = p.getClient().getPlayerModel().getName();
				else if (count == 2)
					player3 = p.getClient().getPlayerModel().getName();
				else if (count == 3)
					player4 = p.getClient().getPlayerModel().getName();
				score += p.getClient().getPlayerModel().getElopoints() - 500;
				count++;
			}
			if (count != 0)
				score = (int) (score / count);
		}

		return new GameDescription(this.getGameId(), "[" + score + "]"
				+ this.getGameName(), this.getMapId(), this.getMaxPlayers(),
				count, this.getMaxPoints(), this.getMinPoints(), this
						.getPasswort().length() > 0 ? "yes" : "no", this
						.getMode(), player1, player2, player3, player4, this
						.getGameState().toString());
	}

	public boolean canPlayerJoin(Client client, JoinGameRequestMessage jgrm) {
		if (!(this.getGameState() instanceof WaitingGameState))
			return false;
		if (this.getPlayersSize() >= this.getMaxPlayers())
			return false;
		if ((!this.getPasswort().equals(""))
				&& (!this.getPasswort().equals(jgrm.getPasswort())))
			return false;
		int points = client.getPlayerModel().getOldElopoints() - 500;
		if (points > 1000) {
			synchronized (this.players) {
				for (PlayerInGame p : this.players) {
					if (p.getClient().getPlayerModel().getElopoints() < (points-500))
						return false;
				}
			}
		}
		synchronized (this.players) {
			for (PlayerInGame p : this.players) {
					if ((p.getClient().getPlayerModel().getElopoints() > 1500) &&
							(points < p.getClient().getPlayerModel().getElopoints()-1500))
						return false;
			}
		}
		return true;
	}


	/**
	 * Adds an observer. It will be notified of any change in the game's state
	 * or list of players.
	 * 
	 * @param observer
	 *            the observer. Must not be null.
	 */
	public void addObserver(GameObserverInterface observer) {
		if (observer == null)
			throw new IllegalArgumentException("'observer' was null");
		this.observers.add(observer);
	}

	/**
	 * Notifies all observers.
	 */
	public void gameStateChanged() {
		for (GameObserverInterface observer : this.observers) {
			observer.gameStateChanged((Game) this);
		}
	}

	/**
	 * Notifies all observers.
	 */
	public void gamePlayersChanged() {
		for (GameObserverInterface observer : this.observers) {
			observer.gamePlayersChanged((Game) this);
		}
	}

	/**
	 * The Function saves actual ended game into DB
	 * 
	 * @param playerNamePositionMap
	 *            Players with position
	 * @param startDate
	 *            Start date of game
	 */
	public void saveToJournal(Map<PlayerInGame, Integer> playerPositionMap,
			long startDate, long endDate) {
		try {
			EntityManager entityManager = PersistenceManager.getInstance()
					.getEntityManager();
			EntityTransaction entityTransaction = entityManager
					.getTransaction();
			entityTransaction.begin();

			GameJournal gameJournalEntry = new GameJournal();
			String mapname = GameMap.getMapById(this.getMapId())
					.getFilename();
			mapname = mapname.replaceAll(
					"com/creepsmash/client/resources/maps/map_", "");
			gameJournalEntry.setMap(mapname.replaceAll(".map", ""));
			gameJournalEntry.setName(this.getGameName());
			gameJournalEntry.setNumPlayers(playerPositionMap.size());
			gameJournalEntry.setStart_date(startDate);
			gameJournalEntry.setEnd_date(endDate);
			int i = 0;
			for (PlayerInGame player : playerPositionMap.keySet()) {
				i++;
				Client client = player.getClient();
				if (client.getPlayerModel() != null) {
					int num = playerPositionMap.get(client);
					// Ich habe keine idee wie man da ohne switch macht^^
					switch (i) {
					case 1:
						gameJournalEntry.setPlayer1(client.getPlayerModel()
								.getName());
						gameJournalEntry.setPlayer1_score(client
								.getPlayerModel().getOldElopoints());
						gameJournalEntry.setScore1(client.getPlayerModel()
								.getElopoints());
						gameJournalEntry.setPlayer1_position(num);
						gameJournalEntry
								.setIp1(client.getPlayerModel().getIp());
						gameJournalEntry.setMac1(client.getPlayerModel()
								.getMac());
						break;
					case 2:
						gameJournalEntry.setPlayer2(client.getPlayerModel()
								.getName());
						gameJournalEntry.setPlayer2_score(client
								.getPlayerModel().getOldElopoints());
						gameJournalEntry.setScore2(client.getPlayerModel()
								.getElopoints());
						gameJournalEntry.setPlayer2_position(num);
						gameJournalEntry
								.setIp2(client.getPlayerModel().getIp());
						gameJournalEntry.setMac2(client.getPlayerModel()
								.getMac());
						break;
					case 3:
						gameJournalEntry.setPlayer3(client.getPlayerModel()
								.getName());
						gameJournalEntry.setPlayer3_score(client
								.getPlayerModel().getOldElopoints());
						gameJournalEntry.setScore3(client.getPlayerModel()
								.getElopoints());
						gameJournalEntry.setPlayer3_position(num);
						gameJournalEntry
								.setIp3(client.getPlayerModel().getIp());
						gameJournalEntry.setMac3(client.getPlayerModel()
								.getMac());
						break;
					case 4:
						gameJournalEntry.setPlayer4(client.getPlayerModel()
								.getName());
						gameJournalEntry.setPlayer4_score(client
								.getPlayerModel().getOldElopoints());
						gameJournalEntry.setScore4(client.getPlayerModel()
								.getElopoints());
						gameJournalEntry.setPlayer4_position(num);
						gameJournalEntry
								.setIp4(client.getPlayerModel().getIp());
						gameJournalEntry.setMac4(client.getPlayerModel()
								.getMac());
						break;
					default:
						logger.error("False number of players: " + i);
						break;
					}
				}
			}

			entityManager.persist(gameJournalEntry);
			entityManager.flush();
			entityTransaction.commit();
			logger.debug("GameJournal saved.");
		} catch (Throwable t) {
			logger.error("error while saving GameJournal", t);
		}
	}

	/**
	 * The function check for multiaccounting.
	 * 
	 * @param ip
	 *            IP Address of Client
	 */
	public boolean check4Multi(String ip, String mac) {
		/*
		 * TODO if (IConstants.MUTIACCOUNT_IP_CHECK ||
		 * IConstants.MUTIACCOUNT_MAC_CHECK) { boolean a = false; synchronized
		 * (this.clients) { for (PlayerInGame p : this.clients) { Client c =
		 * p.getClient(); // If the client from same Computer if
		 * (mac.equalsIgnoreCase(c.getPlayerModel().getMac()) &&
		 * IConstants.MUTIACCOUNT_MAC_CHECK) {
		 * logger.warn("Multiaccounting detected. MAC: " + mac); return false; }
		 * // If the client from same IP if
		 * (ip.equalsIgnoreCase(c.getIPAddress()) &&
		 * IConstants.MUTIACCOUNT_IP_CHECK) { if (a) {
		 * logger.warn("Multiaccounting detected. IP: " + ip); return false; } a
		 * = true; } } } }
		 */
		return true;
	}
}

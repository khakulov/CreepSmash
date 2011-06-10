package com.creepsmash.server.game.states;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.BuildCreepMessage;
import com.creepsmash.common.messages.client.BuildTowerMessage;
import com.creepsmash.common.messages.client.ChangeStrategyMessage;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.GameMessage;
import com.creepsmash.common.messages.client.GameOverMessage;
import com.creepsmash.common.messages.client.LiveTakedMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.SellTowerMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.client.UpgradeTowerMessage;
import com.creepsmash.common.messages.server.BuildCreepRoundMessage;
import com.creepsmash.common.messages.server.BuildTowerRoundMessage;
import com.creepsmash.common.messages.server.ChangeStrategyRoundMessage;
import com.creepsmash.common.messages.server.PlayerQuitMessage;
import com.creepsmash.common.messages.server.RoundMessage;
import com.creepsmash.common.messages.server.SellTowerRoundMessage;
import com.creepsmash.common.messages.server.UpgradeTowerRoundMessage;
import com.creepsmash.server.HighscoreService;
import com.creepsmash.server.game.Game;
import com.creepsmash.server.game.PlayerInGame;
import com.creepsmash.server.game.TickThread;


/**
 * GameState for a game that's running (has started).
 */
public class RunningGameState extends AbstractGameState implements
		TickThread.TickReceiver {

	private int nextTowerId;
	private long maxTick;
	private Map<PlayerInGame, Integer> playerPositionMap;
	private long startDate;

	Random generator = null;
	TickThread tickThread;

	private static Logger logger = Logger.getLogger(RunningGameState.class);

	/**
	 * Constructor.
	 * 
	 * @param game
	 *            the game. Must not be null. Must not be null or empty.
	 */
	public RunningGameState(Game game) {
		super(game);
		this.nextTowerId = 1;
		this.maxTick = 0;
		this.playerPositionMap = new HashMap<PlayerInGame, Integer>();
		this.startDate = System.currentTimeMillis() / 1000;

		if (game.getMode() == 2) {
			generator = new Random();
		}

		tickThread = new TickThread(this, IConstants.TICK_MS * 1000000);
	}

	/**
	 * Advance the maxTick counter and send a "ROUND n OK" message to all
	 * players.
	 */
	public void tick() {
		if (this.maxTick == 0) {
			RoundMessage message = new RoundMessage();
			message.setRoundId(this.maxTick + IConstants.USER_ACTION_DELAY);
			this.getGame().sendAll(message);
		}

		this.maxTick += 1;

		if ((this.maxTick < IConstants.USER_ACTION_DELAY * 10)
				&& (this.maxTick % IConstants.USER_ACTION_DELAY == 0)) {
			RoundMessage message = new RoundMessage();
			message.setRoundId(this.maxTick + IConstants.USER_ACTION_DELAY);
			this.getGame().sendAll(message);
		}

		if (this.maxTick % (IConstants.USER_ACTION_DELAY * 10) == 0) {
			RoundMessage message = new RoundMessage();
			message.setRoundId(this.maxTick
					+ (IConstants.USER_ACTION_DELAY * 10));
			this.getGame().sendAll(message);
		}

		if (this.maxTick % (IConstants.INCOME_TIME / IConstants.TICK_MS) == 0) {
			for (PlayerInGame p : this.getGame().getPlayers()) {
				p.anticheat_updateMoney(this.maxTick);
				if (p.anticheat_getCurrentMoney() < 0) {
					logger.warn("Cheater Detected!!! Username is "
							+ p.getClient().getPlayerModel().getName()
							+ " and current money is "
							+ p.anticheat_getCurrentMoney());
					p.anticheat_kickAndBan(this);

				}/*
				 * else { logger.info("User " + p.getClient().getUserName() +
				 * " hatte in tick " + (this.maxTick - 300) + " " +
				 * p.anticheat_getCurrentMoney() + " Credits und " +
				 * p.anticheat_getCurrentIncome() + " Income."); }
				 */
			}
		}
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
			throw new IllegalArgumentException("'message' was null");
		if (sender == null)
			throw new IllegalArgumentException("'sender' was null");

		if (message instanceof BuildCreepMessage) {
			this.handle((BuildCreepMessage) message);
		} else if (message instanceof UpgradeTowerMessage) {
			this.handle((UpgradeTowerMessage) message, sender);
		} else if (message instanceof BuildTowerMessage) {
			this.handle((BuildTowerMessage) message);
		} else if (message instanceof ChangeStrategyMessage) {
			this.handle((ChangeStrategyMessage) message, sender);
		} else if (message instanceof SellTowerMessage) {
			this.handle((SellTowerMessage) message, sender);
		} else if (message instanceof LiveTakedMessage) {
			this.handle((LiveTakedMessage) message);
		} else if (message instanceof GameOverMessage) {
			this.handle((GameOverMessage) message, sender);
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
		/*
		 * for (PlayerInGame p : this.getGame().getClients()) {
		 * logger.info("Player " + p.getClient().getUserName() + " have Money "
		 * + p.anticheat_getCurrentMoney() + " at Round " + this.maxTick); }
		 */
	}

	private void handle(ChangeStrategyMessage message, PlayerInGame sender) {
		int towerId = message.getTowerId();

		if (towerId <= 0) {
			logger.error("Invalid tower id (tried to change strategy "
					+ towerId + ")");
			return;
		}
		if (towerId >= this.nextTowerId) {
			logger.error("No such tower (tried to change strategy " + towerId
					+ ")");
			return;
		}

		ChangeStrategyRoundMessage csm = new ChangeStrategyRoundMessage();
		csm.setRoundId(this.maxTick + IConstants.USER_ACTION_DELAY);
		csm.setPlayerId(message.getClientId());
		csm.setTowerId(towerId);
		csm.setStrategyType(message.getStrategyType());
		csm.setLocked(message.isLocked());
		this.getGame().sendAll(csm);

	}

	private void handle(LiveTakedMessage m) {
		PlayerInGame from = this.getGame().findPlayer(m.getFromPlayerId());
		PlayerInGame to = this.getGame().findPlayer(m.getToPlayerId());
		;
		PlayerInGame sender = this.getGame().findPlayer(m.getSenderId());
		;

		if (from != null) {
			from.takeLive();
			from.anticheat_transferThisCreep(m.getCreepType(), m.getRoundId());
		}
		if (to != null) {
			to.anticheat_receivedThisCreep(m.getCreepType(), m.getRoundId());
		}
		if (sender != null) {
			sender.incraseTakedLive();
		}
	}

	/**
	 * Handles the BuildTowerMessage.
	 * 
	 * @param m
	 *            the message
	 */
	private void handle(BuildTowerMessage m) {
		String type = m.getTowerType();
		Point position = m.getPosition();
		int senderId = m.getClientId();
		long roundID = this.maxTick + IConstants.USER_ACTION_DELAY;

		BuildTowerRoundMessage n = new BuildTowerRoundMessage();
		n.setRoundId(roundID);
		n.setPlayerId(senderId);
		n.setTowerType(type);
		n.setTowerPosition(position);
		n.setTowerId(this.nextTowerId++);
		this.getGame().sendAll(n);

		/*
		 * for (PlayerInGame p : this.getGame().getClients()) { if
		 * (p.getClient().getClientID() == senderId) {
		 * p.anticheat_TowerBuilt(type, n.getTowerId(), m.getRoundId()); break;
		 * } }
		 */
	}

	/**
	 * Handles the UpgradeTowerMessage.
	 * 
	 * @param m
	 *            the message.
	 * @param sender
	 *            the player who sent the message.
	 */
	private void handle(UpgradeTowerMessage m, PlayerInGame sender) {
		int towerId = m.getTowerId();
		long roundID = this.maxTick + IConstants.USER_ACTION_DELAY;

		if (towerId <= 0) {
			logger.error("Invalid tower id (tried to upgrade tower " + towerId
					+ ")");
			return;
		} else if (towerId >= this.nextTowerId) {
			logger.error("No such tower (tried to upgrade tower " + towerId
					+ ")");
			return;
		}

		UpgradeTowerRoundMessage n = new UpgradeTowerRoundMessage();
		n.setRoundId(roundID);
		n.setPlayerId(m.getClientId());
		n.setTowerId(towerId);
		this.getGame().sendAll(n);

		sender.anticheat_TowerUpgraded(towerId, m.getRoundId());
	}

	/**
	 * Handles the SellTowerMessage.
	 * 
	 * @param m
	 *            the message.
	 * @param sender
	 *            the player who sent the message.
	 */
	private void handle(SellTowerMessage m, PlayerInGame sender) {
		int towerId = m.getTowerId();

		if (towerId <= 0) {
			logger.error("Invalid tower id (tried to sell tower " + towerId
					+ ")");
			return;
		}
		if (towerId >= this.nextTowerId) {
			logger.error("No such tower (tried to sell tower " + towerId + ")");
			return;
		}

		SellTowerRoundMessage n = new SellTowerRoundMessage();
		n.setRoundId(this.maxTick + IConstants.USER_ACTION_DELAY);
		n.setPlayerId(m.getClientId());
		n.setTowerId(towerId);
		this.getGame().sendAll(n);

		sender.anticheat_TowerSold(m.getTowerId(), m.getRoundId());
	}

	/**
	 * Handles the BuildCreepMessage.
	 * 
	 * @param m
	 *            the message
	 */
	private void handle(BuildCreepMessage m) {
		String type = m.getCreepType();
		int senderId = m.getClientId();
		long roundID = this.maxTick + IConstants.USER_ACTION_DELAY;

		/*
		 * for (PlayerInGame p : this.getGame().getClients()) { if
		 * (p.getClient().getClientID() == senderId) {
		 * p.anticheat_sentThisCreep(type, m.getRoundId()); break; } }
		 */

		if ((this.getGame().getMode() == 1)
				&& (this.getGame().getPlayersSize() > 2)) {
			// All vs. All Mode
			for (PlayerInGame p : this.getGame().getPlayers()) {
				if ((p.getClient().getClientID() != senderId)
						&& (!p.getGameOver())) {
					BuildCreepRoundMessage n = new BuildCreepRoundMessage();
					n.setRoundId(roundID);
					n.setCreepType(type);
					n.setSenderId(senderId);
					n.setPlayerId(p.getClient().getClientID());
					this.getGame().sendAll(n);

					p.anticheat_receivedThisCreep(type, m.getRoundId());
				}
			}
		} else if ((this.getGame().getMode() == 2)
				&& (this.getGame().getPlayersSize() > 2)) {
			// Random send mode
			List<PlayerInGame> pl = this.getGame().getPlayers();
			while (!pl.isEmpty()) {
				int random = this.generator.nextInt(pl.size());
				PlayerInGame p = pl.get(random);
				if (p != null) {
					if ((p.getClient().getClientID() != senderId)
							&& (!p.getGameOver())) {
						BuildCreepRoundMessage n = new BuildCreepRoundMessage();
						n.setRoundId(roundID);
						n.setCreepType(type);
						n.setSenderId(senderId);
						n.setPlayerId(p.getClient().getClientID());
						this.getGame().sendAll(n);
						p.anticheat_receivedThisCreep(type, m.getRoundId());
						return;
					} else {
						pl.remove(p.getClient().getClientID());
					}
				} else {
					logger.error("Random send mode error. Player was null.");
				}
			}
		} else {
			// Normal
			List<PlayerInGame> pl = this.getGame().getPlayers();
			Iterator<PlayerInGame> it = pl.iterator();
			PlayerInGame receiver = null;
			while (it.hasNext()) {
				if (it.next().getClient().getClientID() == senderId) {
					if (it.hasNext()) {
						receiver = it.next();
					} else {
						receiver = pl.get(0);
					}
				}
			}
			BuildCreepRoundMessage n = new BuildCreepRoundMessage();
			n.setRoundId(roundID);
			n.setCreepType(type);
			n.setSenderId(senderId);
			n.setPlayerId(receiver.getClient().getClientID());
			this.getGame().sendAll(n);

			receiver.anticheat_receivedThisCreep(type, m.getRoundId());
		}
	}

	/**
	 * Handles the GameOverMessage.
	 * 
	 * @param m
	 *            the message
	 * @param sender
	 *            the player who sent the message.
	 * @return the new state.
	 */
	private AbstractGameState handle(GameOverMessage m, PlayerInGame sender) {
		sender.gameOver();
		synchronized(this.playerPositionMap) {
			if (!this.playerPositionMap.containsKey(sender)) {
				if (!this.playerPositionMap.containsValue(m.getPosition())) {
					this.playerPositionMap.put(sender, m.getPosition());
				} else {
					int position = this.getGame().getMaxPlayers();
					for (int i = position; i > 0; i--) {
						if (!this.playerPositionMap.containsValue(i)) {
							this.playerPositionMap.put(sender, i);
							break;
						}
					}
				}
			}
		}
		logger.info("game over for " + sender );

		if (this.gameOverForAll()) {
			this.endGame();
			return new EndedGameState(this.getGame());
		}

		return this;
	}

	/**
	 * Returns a string identifying this state.
	 * 
	 * @return "running"
	 */
	@Override
	public String toString() {
		return "running";
	}

	@Override
	public void enter() {
		tickThread.start();
	}

	@Override
	public void leave() {
		tickThread.terminate();
	}

	private AbstractGameState removePlayer(PlayerInGame player) {
		player.gameOver();
		synchronized (this.playerPositionMap) {
			if (!this.playerPositionMap.containsKey(player)) {
				int position = this.getGame().getMaxPlayers();
				for (int i = position; i > 0; i--) {
					if (!this.playerPositionMap.containsValue(i)) {
						this.playerPositionMap.put(player, i);
						break;
					}
				}
				logger.info("game over for " + player + " (position: "
						+ position + ")");
			}
		}
		this.getGame().removePlayer(player);
		this.getGame().sendAll(
				new PlayerQuitMessage(player.getClient().getPlayerModel()
						.getName(), "", player.getClient().getClientID()));
		if (this.gameOverForAll()) {
			this.endGame();
			return new EndedGameState(this.getGame());
		}
		if (this.getGame().getPlayersSize() == 0)
			return new TerminatedGameState(this.getGame());
		return this;
	}

	private boolean gameOverForAll() {
		PlayerInGame pt = null;
		for (PlayerInGame p : this.getGame().getPlayers()) {
			if (!p.getGameOver()) {
				if (pt==null)
					pt=p;
				else
					return false;
			}
		}
		if (pt!=null)
			pt.gameOver();
		return true;
	}

	private void endGame() {
		long endDate = System.currentTimeMillis() / 1000;
		if (endDate - startDate > 60) {
			logger.info("saving scores: " + this.playerPositionMap);
			HighscoreService.createHighscoreEntry(this.playerPositionMap);
		} else {
			HighscoreService.clearOldPoints(this.playerPositionMap.keySet());
			logger.info("duration of the game " + this.getGame() + " was to short");
		}
		// save Game in DB
		this.getGame().saveToJournal(this.playerPositionMap, startDate,	endDate);
	}
}
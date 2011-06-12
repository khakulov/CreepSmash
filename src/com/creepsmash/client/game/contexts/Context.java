package com.creepsmash.client.game.contexts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.Game;
import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.creeps.CreepFactory;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.game.panels.GameBoard;
import com.creepsmash.client.game.strategies.Strategy;
import com.creepsmash.client.game.strategies.StrategyFactory;
import com.creepsmash.client.game.towers.Tower;
import com.creepsmash.client.game.towers.TowerFactory;
import com.creepsmash.client.network.Network;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.CreepType;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.TowerType;
import com.creepsmash.common.messages.client.BuildCreepMessage;
import com.creepsmash.common.messages.client.BuildTowerMessage;
import com.creepsmash.common.messages.client.ChangeStrategyMessage;
import com.creepsmash.common.messages.client.SellTowerMessage;
import com.creepsmash.common.messages.client.UpgradeTowerMessage;
import com.creepsmash.common.messages.server.BuildCreepRoundMessage;
import com.creepsmash.common.messages.server.BuildTowerRoundMessage;
import com.creepsmash.common.messages.server.ChangeStrategyRoundMessage;
import com.creepsmash.common.messages.server.GameMessage;
import com.creepsmash.common.messages.server.SellTowerRoundMessage;
import com.creepsmash.common.messages.server.UpgradeTowerRoundMessage;


/**
 * Abstract class representing the context for one player.
 */
public class Context {

	private Game game;
	private BoardLocation location;
	private GameBoard gameBoard;
	private boolean started;

	private List<Tower> towers;
	private List<Creep> creeps;
	private List<Creep> transfer;

	private ArrayList<ContextListener> contextListeners;

	private int playerId;
	private String playerName;

	private int credits = IConstants.START_CREDITS;
	private int income = IConstants.START_INCOME;
	private int lives = IConstants.START_LIVES;

	private AffineTransform save = null;
	private AffineTransform translation = new AffineTransform();

	private TowerType nextTower = null;
	private Tower selectedTower = null;

	private int winningPosition = 0;

	private long lastCreepSent = 0;
	private long lastWaveSent = 0;
	private long lastWaveDelay = 0;

	/**
	 * Implementation for default init.
	 */
	public Context(Game game, BoardLocation location, int playerId, String playerName) {
		this.game = game;
		this.location = location;
		this.gameBoard = new GameBoard(this);
		this.started = false;
		this.playerId = playerId;
		this.playerName = playerName;

		this.towers = Collections.synchronizedList(new ArrayList<Tower>());
		this.creeps = Collections.synchronizedList(new ArrayList<Creep>());
		this.transfer = Collections.synchronizedList(new ArrayList<Creep>());

		this.contextListeners = new ArrayList<ContextListener>();
	}

	/**
	 * Update the context by one tick.
	 * 
	 * @param roundID
	 *            the current tick
	 */
	public void update(long roundID) {
		ArrayList<Tower> towersCopy = new ArrayList<Tower>(getTowers());
		ArrayList<Creep> creepsCopy = new ArrayList<Creep>(getCreeps());

		// update towers only if the player is not dead
		if (!this.isDead()) {
			for (Tower t : towersCopy) {
				t.update(roundID);
			}
		}

		for (Creep c : creepsCopy) {
			c.update(roundID);
		}

		// has to happen after the creeps received their update,
		// otherwise they are duplicated...
		ArrayList<Creep> transferCopy = new ArrayList<Creep>(getTransfer());

		// remove creeps which are transferred
		for (Creep c : transferCopy) {
			getCreeps().remove(c);
		}
		
		// check all gamemessage and invoke actions...
		ArrayList<GameMessage> queueCopy = new ArrayList<GameMessage>(Network.getQueue());

		for (GameMessage gm : queueCopy) {
			if (gm.getPlayerId() == this.playerId) {
				// take actions
				processMessage(gm, roundID);
				// remove from queue
				Network.getQueue().remove(gm);
			}
		}
		if (roundID == IConstants.INCOME_TIME)
			this.started = true;
		if (roundID >= IConstants.INCOME_TIME) {
			// only do that if player is alive
			if (!this.isDead()) {
				// updates income everu INCOME_TIME millis
				if (roundID % IConstants.INCOME_TIME == 0) {
					this.setCredits(this.getCredits() + this.getIncome());
				}
				// countdown for next income
				if (roundID % (1000 / IConstants.TICK_MS) == 0) {
					int seconds = 15 - (int)((roundID % IConstants.INCOME_TIME) / (1000 / IConstants.TICK_MS));
					this.fireIncomeTimeChangedEvent(seconds);
				}
			}
		}
	}

	/**
	 * Process a message from the server and invoke the action associated with
	 * it.
	 * 
	 * @param gm
	 *            the server message
	 * @param roundId
	 *            the current round id
	 */
	private void processMessage(GameMessage gm, long roundId) {

		int updateRound = (int) (gm.getRoundId() - roundId);
		if (updateRound <= 0) {
			Core.logger.info("processMessage is out of sync!!!");
			SoundManager.playerWonSound();
		}

		if (gm instanceof BuildTowerRoundMessage) {
			BuildTowerRoundMessage btrm = (BuildTowerRoundMessage) gm;
			Grid grid = this.gameBoard.getGrid((int) btrm.getTowerPosition().getX(),
			                                   (int) btrm.getTowerPosition().getY());
			if (grid.isFree()) {
				Tower tower = TowerFactory.createTower(this, TowerType.valueOf(btrm.getTowerType()), grid);
				tower.setId(btrm.getTowerId());
				tower.build(updateRound);
				this.addtower(tower);
			}
		} else if (gm instanceof UpgradeTowerRoundMessage) {
			UpgradeTowerRoundMessage utrm = (UpgradeTowerRoundMessage) gm;
			// find the tower we want to upgrade
			for (Tower tower : towers) {
				if (tower.getId() == utrm.getTowerId() && tower.isReady()) {
					tower.upgrade(updateRound);
					break;
				}
			}
		} else if (gm instanceof SellTowerRoundMessage) {
			SellTowerRoundMessage strm = (SellTowerRoundMessage) gm;
			// find the tower we want to sell
			for (Tower tower : towers) {
				if (tower.getId() == strm.getTowerId() && tower.isReady()) {
					tower.sell(updateRound);
					break;
				}
			}
		} else if (gm instanceof ChangeStrategyRoundMessage) {
			ChangeStrategyRoundMessage csm = (ChangeStrategyRoundMessage) gm;
			// find the tower we want to change
			for (Tower tower : towers) {
				if (tower.getId() == csm.getTowerId() && tower.isReady()) {
					Strategy strategy = StrategyFactory.createStrategy(csm.getStrategyType(), tower);
					if (csm.isLocked())
						strategy.lock();
					if (this.playerId != Core.getPlayerId())
						tower.setNewStrategy(strategy);
					tower.changeStrategy(updateRound);
					break;
				}
			}
		} else if (gm instanceof BuildCreepRoundMessage) {
			BuildCreepRoundMessage bcrm = (BuildCreepRoundMessage) gm;
			Creep c = CreepFactory.createCreep(this, CreepType.valueOf(bcrm.getCreepType()));
			c.setRound(bcrm.getRoundId());
			c.setSenderId(bcrm.getSenderId());

			if (!this.isDead()) {
				this.getCreeps().add(c);
				// play sound if own context
				if (this.playerId == Core.getPlayerId())
					SoundManager.creepStartsSound(c.getType());
			} else {
				this.getTransfer().add(c);
			}
		}
	}

	/**
	 * Paint the the context with its elements.
	 * 
	 * @param g
	 *            the graphics object
	 */
	public void paint(Graphics2D graphics) {
		graphics.setFont(new Font("Verdana", Font.PLAIN, 10));

		save = graphics.getTransform();
		translation.setToIdentity();
		translation.translate(this.getLocation().getX(), this.getLocation().getY());
		graphics.transform(translation);

		this.gameBoard.paint(graphics);

		for (Creep c : getCreeps()) {
			c.paint(graphics);
		}

		for (Tower t : getTowers()) {
			t.paintEffect(graphics);
		}

		if ((this.game.getRound() <= 300) && (this == this.game.getPlayerCotext())) {
			if (this.game.getRound() <= 100) {
				graphics.setColor(Color.GRAY);
				graphics.setFont(new Font("Verdana", Font.BOLD, 30));
				graphics.drawString("loading...", 100, 160);
			} else if (this.game.getRound() <= 180) {
				graphics.setColor(Color.RED);
				graphics.setFont(new Font("Verdana", Font.BOLD, 20));
				graphics.drawString("Game starting in...", 70, 160);
			} else if (this.game.getRound() < 270) {
				graphics.setColor(Color.RED);
				graphics.setFont(new Font("Verdana", Font.BOLD, 200));
				graphics.drawString(String.valueOf((int)((300 - this.game.getRound()) / 30)), 95, 230);
			} else {
				graphics.setColor(Color.RED);
				graphics.setFont(new Font("Verdana", Font.BOLD, 190));
				graphics.drawString("GO", 5, 230);
			}
		}

		paintPlayerInfo(graphics);

		if (this.isDead()) {
			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font("Verdana", Font.BOLD, 45));
			graphics.drawString("Game Over", 25, 140);
			if (this == this.game.getPlayerCotext()) {
				graphics.setFont(new Font("Verdana", Font.BOLD, 15));
				graphics.drawString("sad but true ", 90, 180);
			}
		} else if (!this.isDead() && !this.game.isRunning()) {
			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font("Verdana", Font.BOLD, 45));
			graphics.drawString("Winner", 80, 140);
			if (this == this.game.getPlayerCotext()) {
				graphics.setFont(new Font("Verdana", Font.BOLD, 15));
				graphics.drawString("Press ESC to leave", 90, 180);
			}
		}

		graphics.setTransform(save);
	}

	/**
	 * Method to paint the playernames and other things.
	 * 
	 * @param g
	 *            the graphics context
	 */
	private void paintPlayerInfo(Graphics2D g) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.playerName);
		sb.append(" | Lives: ");
		sb.append(this.lives);

		g.setColor(Color.WHITE);
		if (this == this.game.getPlayerCotext()) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("Verdana", Font.BOLD, 12));
		} else {
			g.setFont(new Font("Verdana", Font.PLAIN, 10));
		}
		switch (location) {
		case TOPLEFT:
			g.drawString(sb.toString(), 10, -10);
			break;
		case TOPRIGHT:
			g.drawString(sb.toString(), 10, -10);
			break;
		case BOTTOMLEFT:
			g.drawString(sb.toString(), 10, 335);
			break;
		case BOTTOMRIGHT:
			g.drawString(sb.toString(), 10, 335);
			break;
		default:
			break;
		}
	}

	public boolean readyForNewWave() {
		if (lastWaveSent + this.lastWaveDelay < System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean readyForNewCreep() {
		if (lastCreepSent + IConstants.CREEP_DELAY < System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sendet Threadsafe neue Creep
	 */
	public synchronized boolean sendCreep(CreepType type) {
		if (this.started && !this.isDead() && this.getCredits() >= type.getPrice()) {

			BuildCreepMessage bcm = new BuildCreepMessage();
			bcm.setClientId(this.getPlayerId());
			bcm.setCreepType(type.toString());
			bcm.setRoundId(this.game.getRound());
			Network.sendMessage(bcm);
			this.setCredits(this.getCredits() - type.getPrice());
			this.setIncome(this.getIncome() + type.getIncome());
			lastCreepSent = System.currentTimeMillis();

			return true;
		}
		return false;
	}

	/**
	 * Sendet eine ganze Welle von Creeps
	 * 
	 * @param context
	 * @param gamepanel
	 * @param type
	 */
	public void sendCreepsWave(final CreepType type) {
		if (this.started && !this.isDead() && this.getCredits() >= type.getPrice()) {
			lastWaveSent = System.currentTimeMillis();
			final Context context = this;
			new Thread() {
				@Override
				public void run() {

					try {
						// get the max size of the wave that could be send
						long maxWaveSize = context.getCredits() / type.getPrice();
						// if size exceeds CREEPS_IN_WAVE, set it to CREEPS_IN_WAVE
						if (maxWaveSize > IConstants.CREEPS_IN_WAVE)
							maxWaveSize = IConstants.CREEPS_IN_WAVE;
						// set delay to send new creeps
						context.lastWaveDelay = maxWaveSize * IConstants.SEND_WAVE_DELAY;
						
						// while loop needed to get the number of completed loops
						long i = 0;
						while (i < maxWaveSize && !isInterrupted()) {
							if (context.sendCreep(type)) {
								sleep(IConstants.SEND_WAVE_DELAY);
							} else {
								interrupt();
							}
							i++;
						}
						// correct lastWaveDelay to prevent short send bug after spending money for
						// tower/upgrades while sending the wave.
						context.lastWaveDelay = i * IConstants.SEND_WAVE_DELAY;
						interrupt();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	/**
	 * sells the selected or highlighted tower. Only Msg-sending.
	 * 
	 * @return true if a tower could be sold.
	 */
	public boolean sellTower() {
		Tower t = this.selectTower();
		if ((t != null) && (!this.isDead()) && (t.isReady())) {
			SellTowerMessage stm = new SellTowerMessage();
			stm.setClientId(getPlayerId());
			stm.setTowerId(t.getId());
			stm.setRoundId(this.game.getRound());
			Network.sendMessage(stm);
			fireSelectedChangedEvent("sell");
			return true;
		}
		return false;
	}

	/**
	 * This methods builds the given tower at the given grid (cell) if possible.
	 * @param tower Tower that should be build
	 * @param grid The grid (cell) in which the tower should be build
	 */
	public boolean buyTower(TowerType tower, Grid grid) {
		if (this.started && !this.isDead() && grid.isFree() && (this.getCredits() >= tower.getPrice())) {
			BuildTowerMessage btm = new BuildTowerMessage();
			btm.setClientId(this.getPlayerId());
			btm.setPosition(new Point(grid.getLocation()[0], grid.getLocation()[1]));
			btm.setTowerType(tower.toString());
			btm.setRoundId(this.getGame().getRound());
			Network.sendMessage(btm);
			this.setCredits(this.getCredits() - tower.getPrice());
			return true;
		}
		return false;
	}

	/**
	 * Upgrades the given tower.
	 * 
	 * @return true if tower is upgraded
	 * @param t tower to upgrade
	 */
	private boolean upgradeTower(Tower t) {
		if ((t != null) && !isDead() && t.isReady() && t.isUpgradable()
				&& (getCredits() >= t.getType().getNext().getPrice())) {
			UpgradeTowerMessage utm = new UpgradeTowerMessage();
			utm.setClientId(getPlayerId());
			utm.setTowerId(t.getId());
			utm.setRoundId(this.game.getRound());
			Network.sendMessage(utm);
			setCredits(getCredits() - t.getType().getNext().getPrice());
			this.fireSelectedChangedEvent("upgrade");
			return true;

		}
		return false;
	}

	/**
	 * Upgrades the selected tower.
	 * 
	 * @return true if tower is upgraded
	 */
	public boolean upgradeTower() {
		if (this.selectTower() != null){
			this.upgradeTower(this.selectTower());
		}
		else
			return false;
		
		return true;
	}
	
	/**
	 * Fires an event when the lives changed.
	 */
	private void fireLivesChangedEvent() {
		for (ContextListener cl : contextListeners) {
			cl.livesChanged(this);
		}
	}

	/**
	 * Fires an event when the income changed.
	 */
	private void fireIncomeChangedEvent() {
		for (ContextListener cl : contextListeners) {
			cl.incomeChanged(this);
		}
	}

	/**
	 * Fires an event when the income time changed.
	 */
	private void fireIncomeTimeChangedEvent(int seconds) {
		for (ContextListener cl : contextListeners) {
			cl.incomeTimeChanged(this, seconds);
		}
	}

	/**
	 * Fires an event when the credits changed.
	 */
	private void fireCreditsChangedEvent() {
		for (ContextListener cl : contextListeners) {
			cl.creditsChanged(this);
		}
	}

	/**
	 * Fires an event when the selected var changed.
	 * 
	 * @param message what changed
	 */
	public void fireSelectedChangedEvent(String message) {
		for (ContextListener cl : contextListeners) {
			cl.selectedChanged(this, message);
		}
	}

	public void setStrategy(Strategy strategy) {
		Tower t = this.selectTower();
		if ((t != null) && !this.isDead() && t.isReady()) {
			ChangeStrategyMessage btm = new ChangeStrategyMessage();
			btm.setClientId(getPlayerId());
			btm.setTowerId(t.getId());
			btm.setStrategyType(strategy.getClass().getSimpleName());
			btm.setLocked(strategy.isLocked());
			Network.sendMessage(btm);
			t.setNewStrategy(strategy);
			fireSelectedChangedEvent("strategy");
		}
	}

	/**
	 * Adds a tower to the context.
	 * 
	 * @param t
	 *            the tower
	 */
	public void addtower(Tower t) {
		synchronized (towers) {
			this.getTowers().add(t);
		}
		this.getGameBoard().clearImage();
	}

	/**
	 * Removes a tower from the context.
	 * 
	 * @param t
	 *            the tower to remove
	 */
	public void removeTower(Tower t) {
		this.getGameBoard().removeTower(t.getId());
		this.getTowers().remove(t);
		this.getGameBoard().clearImage();
	}
	
	/**
	 * This methods returns the selected tower. If no tower
	 * is selected, the highlighted tower will be returned.
	 * Is neither a tower selected or highlighted, null will
	 * be returned
	 * 
	 * @return tower or null
	 */
	public Tower selectTower() {
		Tower t = null;
		if (this.getSelectedTower() != null)
			t = this.getSelectedTower();
		else if (this.getHighlightedGrid().getTower() != null)
			t = this.getHighlightedGrid().getTower();
		
		return t;
	}

	/**
	 * @param towers
	 *            the towers to set
	 */
	public void setTowers(List<Tower> towers) {
		this.towers = towers;
	}

	/**
	 * @return the towers
	 */
	public List<Tower> getTowers() {
		return towers;
	}

	/**
	 * @param creeps
	 *            the creeps to set
	 */
	public void setCreeps(List<Creep> creeps) {
		this.creeps = creeps;
	}

	/**
	 * @return the creeps
	 */
	public List<Creep> getCreeps() {
		return creeps;
	}

	/**
	 * @return the credits
	 */
	public int getCredits() {
		return credits;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(int credits) {
		if (this.isDead())
			return;
		this.credits = credits;
		fireCreditsChangedEvent();
	}

	/**
	 * Getter for the income of the player.
	 * 
	 * @return the income
	 */
	public int getIncome() {
		return income;
	}

	/**
	 * Setter for the income of the player.
	 * 
	 * @param income the new income
	 */
	public void setIncome(int income) {
		if (this.isDead())
			return;
		this.income = income;
		fireIncomeChangedEvent();
	}

	/**
	 * @return the lives
	 */
	public int getLives() {
		return this.lives;
	}

	/**
	 * Tests if this context has no more lives left.
	 * 
	 * @return true if the player with this context is dead
	 */
	public boolean isDead() {
		return (this.lives <= 0);
	}

	/**
	 * Removes one live from the context.
	 */
	public void removeLive() {
		if (this.isDead())
			return;
		this.lives--;
		if (this.isDead())
			SoundManager.playerLooseSound();
		fireLivesChangedEvent();
	}

	/**
	 * kill player.
	 */
	public void kill() {
		if (this.isDead())
			return;
		this.lives = 0;
		fireLivesChangedEvent();
	}

	/**
	 * @return the location
	 */
	public BoardLocation getLocation() {
		return location;
	}

	/**
	 * @return the gameBoard
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	/**
	 * Adds a contextListener to this context.
	 * 
	 * @param contextListener
	 *            the listeners to add
	 */
	public void addContextListener(ContextListener contextListener) {
		this.contextListeners.add(contextListener);
	}

	/**
	 * Removes a contextListener from this context.
	 * 
	 * @param contextListener
	 *            the listeners to remove
	 */
	public void removeContextListener(ContextListener contextListener) {
		this.contextListeners.remove(contextListener);
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param transfer
	 *            the transfer to set
	 */
	public void setTransfer(List<Creep> transfer) {
		this.transfer = transfer;
	}

	/**
	 * @return the transfer
	 */
	public List<Creep> getTransfer() {
		return transfer;
	}

	/**
	 * @return the nextTower
	 */
	public TowerType getNextTower() {
		return nextTower;
	}

	/**
	 * @param nextTower
	 *            the nextTower to set
	 */
	public void setNextTower(TowerType nextTower) {
		this.nextTower = nextTower;
	}

	/**
	 * @return the selectedTower
	 */
	public Tower getSelectedTower() {
		return selectedTower;
	}

	/**
	 * @param selectedTower
	 *            the selectedTower to set
	 */
	public void setSelectedTower(Tower selectedTower) {
		this.selectedTower = selectedTower;
	}

	/**
	 * @return the winningPosition
	 */
	public int getWinningPosition() {
		return this.winningPosition;
	}

	/**
	 * @param winningPosition
	 *            the winningPosition to set
	 */
	public void setWinningPosition(int winningPosition) {
		this.winningPosition = winningPosition;
	}
	
	public Grid getHighlightedGrid() {
		return this.gameBoard.getHighlightedGrid();
	}

	public Game getGame() {
		return this.game;
	}
	
	public void setup() {
		this.fireCreditsChangedEvent();
		this.fireIncomeChangedEvent();
		this.fireLivesChangedEvent();
	}
}
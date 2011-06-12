package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.Game;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.grids.Grid;
import com.creepsmash.client.game.panels.ChatPanel;
import com.creepsmash.client.game.panels.CreepInfoPanel;
import com.creepsmash.client.game.panels.CreepSelPanel;
import com.creepsmash.client.game.panels.GameInfoPanel;
import com.creepsmash.client.game.panels.NoInfoPanel;
import com.creepsmash.client.game.panels.SelectTowerInfoPanel;
import com.creepsmash.client.game.panels.TowerInfoPanel;
import com.creepsmash.client.game.panels.TowerSelPanel;
import com.creepsmash.client.game.strategies.ClosestStrategy;
import com.creepsmash.client.game.strategies.FarthestStrategy;
import com.creepsmash.client.game.strategies.FastestStrategy;
import com.creepsmash.client.game.strategies.Strategy;
import com.creepsmash.client.game.strategies.StrategyFactory;
import com.creepsmash.client.game.strategies.StrongestStrategy;
import com.creepsmash.client.game.strategies.WeakestStrategy;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.CreepType;
import com.creepsmash.common.TowerType;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * The GamePanel class is the container for all other game related panels.
 */
public class GameScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private static final String[] TOWERHOTKEYS = {"G", "H", "J", "B", "N", "M"};
	private static final String[] STRATEGYHOTKEYS = {"0", "O", "P", "K", "L"};
	private static final String[] CREEPHOTKEYS = {"1", "2", "3", "4", "Q", "W", "E", "R", "A", "S", "D", "F", "Y", "X", "C", "V"};
	private static final String UPGRADEHOTKEY = "U";
	private static final String SELLHOTKEY = "I";
	private static final String LOCKHOTKEY = "9";

	private ChatPanel chatPanel;

	private TowerInfoPanel newTowerInfoPanel;
	private TowerInfoPanel towerInfoPanel;
	private SelectTowerInfoPanel selectTowerInfoPanel;
	private CreepInfoPanel creepInfoPanel;
	private NoInfoPanel noInfoPanel;
	private JPanel lastTowerInfoPanel;

	private Game game;


	/**
	 * Creates a new instance of GamePanel.
	 */
	@SuppressWarnings("serial")
	public GameScreen(int gameMod, int mapId, Map<Integer, String> players, Map<Integer, Integer> playersOrder) {
		this.game = new Game(gameMod, mapId, players, playersOrder);

		this.setLayout(null);
		this.setBackground(Color.BLACK);

		// BoardPanel
		this.add(this.game.getBoardPanel());

		// GameInfoPanel
		GameInfoPanel gameInfoPanel = new GameInfoPanel(this);
		this.add(gameInfoPanel);

		// TowerSelPanel
		TowerSelPanel towerSelPanel = new TowerSelPanel(this);
		this.add(towerSelPanel);

		// buildTowerInfoPanel
		this.newTowerInfoPanel = new TowerInfoPanel();
		this.newTowerInfoPanel.setVisible(false);
		this.add(this.newTowerInfoPanel);

		// towerInfoPanel
		this.towerInfoPanel = new TowerInfoPanel();
		this.towerInfoPanel.setVisible(false);
		this.add(towerInfoPanel);

		// buildTowerInfoPanel
		this.selectTowerInfoPanel = new SelectTowerInfoPanel(this);
		this.selectTowerInfoPanel.setVisible(false);
		this.add(selectTowerInfoPanel);

		// NoInfoPanel
		this.noInfoPanel = new NoInfoPanel(this);
		this.noInfoPanel.setVisible(true);
		this.add(noInfoPanel);
		this.setLastTowerInfoPanel(noInfoPanel);

		// CreepSelPanel
		CreepSelPanel creepSelPanel = new CreepSelPanel(this);
		this.add(creepSelPanel);

		// CreepInfoPanel
		this.creepInfoPanel = new CreepInfoPanel();
		this.creepInfoPanel.setVisible(false);
		this.add(creepInfoPanel);

		// ChatPanel
		this.chatPanel = new ChatPanel();
		this.add(this.chatPanel);


		final Context context = this.game.getContext(Core.getPlayerId());
		// Quit
		addShortcut("ESCAPE", new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				leave();
			}
		});

		//Upgrade
		addShortcut(UPGRADEHOTKEY, new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				context.upgradeTower();
			}
		});

		//Sell
		addShortcut(SELLHOTKEY, new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				context.sellTower();
			}
		});

		//Lock Strategie
		addShortcut(LOCKHOTKEY, new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = context.getSelectedTower().getStrategy();
				if (strategy.isLocked())
					strategy.unlock();
				else
					strategy.lock();
				context.setStrategy(strategy);
				
			}
		});

		//Farthest Strategie
		addShortcut(STRATEGYHOTKEYS[0], new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = StrategyFactory.createStrategy(
						FarthestStrategy.class.getSimpleName(), context.getSelectedTower());
				if (context.getSelectedTower().getStrategy().isLocked())
					strategy.lock();
				context.setStrategy(strategy);
			}
		});

		//Weakest Strategie
		addShortcut(STRATEGYHOTKEYS[1], new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = StrategyFactory.createStrategy(
						WeakestStrategy.class.getSimpleName(), context.getSelectedTower());
				if (context.getSelectedTower().getStrategy().isLocked())
					strategy.lock();
				context.setStrategy(strategy);
			}
		});

		//Strongest Strategie
		addShortcut(STRATEGYHOTKEYS[2], new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = StrategyFactory.createStrategy(StrongestStrategy.class.getSimpleName(), context.getSelectedTower());
				if (context.getSelectedTower().getStrategy().isLocked())
					strategy.lock();
				context.setStrategy(strategy);
			}
		});

		//Clostest Strategie
		addShortcut(STRATEGYHOTKEYS[3], new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = StrategyFactory.createStrategy(ClosestStrategy.class.getSimpleName(), context.getSelectedTower());
				if (context.getSelectedTower().getStrategy().isLocked())
					strategy.lock();
				context.setStrategy(strategy);
			}
		});

		//Fastest Strategie
		addShortcut(STRATEGYHOTKEYS[4], new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				Strategy strategy = StrategyFactory.createStrategy(FastestStrategy.class.getSimpleName(), context.getSelectedTower());
				if (context.getSelectedTower().getStrategy().isLocked())
					strategy.lock();
				context.setStrategy(strategy);
			}
		});

		/**
		 * private inner classes to automate send-creep shortcuts
		 */
		class SendAction extends AbstractAction {
			protected CreepType type;
			public SendAction(CreepType type) {
				this.type = type;
			}
			public void actionPerformed(ActionEvent event) {
				if (!context.isDead() && context.readyForNewCreep())
					context.sendCreep(type);
			}
		}
		class SendWaveAction extends AbstractAction {
			protected CreepType type;
			public SendWaveAction(CreepType type) {
				this.type = type;
			}
			public void actionPerformed(ActionEvent event) {
				if (!context.isDead() && context.readyForNewWave())
					context.sendCreepsWave(type);
			}
		}

		//Creeps
		CreepType[] creeps = CreepType.values();

		//Add the shortcuts
		for (int i = 0; i < creeps.length; i++) {
			addShortcut(CREEPHOTKEYS[i], new SendAction(creeps[i]));
			addShortcut("shift " + CREEPHOTKEYS[i], new SendWaveAction(creeps[i]));
		}

		class BuildAction extends AbstractAction {
			protected TowerType type;
			public BuildAction(TowerType type) {
				this.type = type;
			}
			public void actionPerformed(ActionEvent event) {
				Grid g = context.getGameBoard().getHighlightedGrid();
				if (g != null)
					context.buyTower(this.type, g);
						
			}
		}

		// Towers
		TowerType[] towers = TowerType.values();

		//Add the shortcuts
		addShortcut(TOWERHOTKEYS[0], new BuildAction(towers[3]));
		addShortcut(TOWERHOTKEYS[1], new BuildAction(towers[7]));
		addShortcut(TOWERHOTKEYS[2], new BuildAction(towers[11]));
		addShortcut(TOWERHOTKEYS[3], new BuildAction(towers[15]));
		addShortcut(TOWERHOTKEYS[4], new BuildAction(towers[19]));
		addShortcut(TOWERHOTKEYS[5], new BuildAction(towers[21]));
	}

	/**
	 * leaves the game.
	 */
	private void leave() {
		if (!game.isRunning()) {
			// sends a messages to all players that we have left
			SendMessageMessage chatMsg = new SendMessageMessage();
			chatMsg.setClientId(Core.getPlayerId());
			chatMsg.setMessage("has left the game");
			Network.sendMessage(chatMsg);

			Network.sendMessage(new ExitGameMessage());
			Window.switchScreen(new GameResultScreen(game.getPlayers()));
		}

	}

	@Override
	public void end() {
		Network.removeListener(this);
		this.game.stop();
	}

	@Override
	public void start() {
		Network.addListener(this);
		this.game.start();
	}

	/* Have to be overriden to avoid execution of shortcuts in chat */
	@Override
	@SuppressWarnings("serial")
	public void addShortcut(String key, Action action) {
		super.addShortcut(key, action);
		KeyStroke keyStroke = KeyStroke.getKeyStroke(key);
		this.chatPanel.getMessageField().getInputMap().put(keyStroke, new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// do nothing
			}
		});
	}

	@Override
	public void receive(ServerMessage serverMessage) {
		if (serverMessage instanceof MessageMessage) {
			MessageMessage messageMessage = (MessageMessage) serverMessage;
			this.chatPanel.setMessage(messageMessage.getPlayerName(), messageMessage.getMessage());
		}
	}

	public Game getGame() {
		return this.game;
	}

	/**
	 * @return the newTowerInfoPanel
	 */
	public TowerInfoPanel getNewTowerInfoPanel() {
		return this.newTowerInfoPanel;
	}

	/**
	 * @return the towerInfoPanel
	 */
	public TowerInfoPanel getTowerInfoPanel() {
		return this.towerInfoPanel;
	}

	/**
	 * @return the selectTowerInfoPanel
	 */
	public SelectTowerInfoPanel getSelectedTowerInfoPanel() {
		return this.selectTowerInfoPanel;
	}

	/**
	 * @return the creepInfoPanel
	 */
	public CreepInfoPanel getCreepInfoPanel() {
		return this.creepInfoPanel;
	}

	/**
	 * @return the noInfoPanel
	 */
	public NoInfoPanel getNoInfoPanel() {
		return this.noInfoPanel;
	}

	public JPanel getLastTowerInfoPanel() {
		return this.lastTowerInfoPanel;
	}

	public void setLastTowerInfoPanel(JPanel lastTowerInfoPanel) {
		this.lastTowerInfoPanel = lastTowerInfoPanel;
	}
}

package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.contexts.ContextListener;
import com.creepsmash.client.game.strategies.ClosestStrategy;
import com.creepsmash.client.game.strategies.FarthestStrategy;
import com.creepsmash.client.game.strategies.FastestStrategy;
import com.creepsmash.client.game.strategies.Strategy;
import com.creepsmash.client.game.strategies.StrategyFactory;
import com.creepsmash.client.game.strategies.StrongestStrategy;
import com.creepsmash.client.game.strategies.WeakestStrategy;
import com.creepsmash.client.game.towers.Tower;
import com.creepsmash.client.gui.screens.GameScreen;
import com.creepsmash.common.TowerType;


/**
 * Panel that shows the attributes of the tower, the user has selected.
 */
public class SelectTowerInfoPanel extends JPanel implements ContextListener {

	private static final long serialVersionUID = 1L;

	private GameScreen gameScreen;
	private Context context;

	private JPanel info;
	private JPanel upgrade;
	private JPanel towerButtons;
	private JButton sellButton;
	private JButton upgradeButton;
	private JToggleButton weakStrategyButton;
	private JToggleButton hardStrategyButton;
	private JToggleButton closeStrategyButton;
	private JToggleButton fastestStrategyButton;
	private JToggleButton farthestStrategyButton;
	private JToggleButton lockCreepButton;
	private Font buttonFont;
	private Font headFont;
	private Font specialFont;

	private JLabel infoTower;
	private JLabel infoSellPrice;
	private JLabel infoDamage;
	private JLabel infoSpeed;
	private JLabel infoRange;
	private JLabel infoSpecial;
	private JLabel upgradeTower;
	private JLabel upgradePrice;
	private JLabel upgradeDamage;
	private JLabel upgradeSpeed;
	private JLabel upgradeRange;
	private JLabel upgradeSpecial;
	private JLabel strategyLabel;


	/**
	 * creates new instance of SelectTowerInfoPanle.
	 * 
	 * @param gamepanel
	 *            gamepanel
	 */
	public SelectTowerInfoPanel(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.context = this.gameScreen.getGame().getContext(Core.getPlayerId());
		this.context.addContextListener(this);

		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.setBounds(700, 200, 233, 125);

		buttonFont = new Font("Helvetica", Font.PLAIN, 12);
		headFont = new Font("Helvetica", Font.BOLD, 15);
		specialFont = new Font("Helvetica", Font.BOLD, 10);

		initInfoPanel();
		initUpgradePanel();
		initTowerPanel();


		upgradeButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if ((context.getSelectedTower() != null)
						&& (context.getSelectedTower().isUpgradable())) {
					info.setVisible(false);
					upgrade.setVisible(true);
				}
			}

			public void mouseExited(MouseEvent e) {
				upgrade.setVisible(false);
				info.setVisible(true);

			}
		});

		upgradeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				context.upgradeTower();
			}
		});

		sellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				context.sellTower();

			}
		});

		weakStrategyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					if (weakStrategyButton.getModel().isSelected()) {
						Strategy strategy = StrategyFactory.createStrategy(
								WeakestStrategy.class.getSimpleName(), tower);
						if (lockCreepButton.getModel().isSelected())
							strategy.lock();
						context.setStrategy(strategy);
					} else
						weakStrategyButton.setSelected(true);
				} else
					weakStrategyButton.setSelected(false);
			}
		});

		hardStrategyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					if (hardStrategyButton.getModel().isSelected()) {
						Strategy strategy = StrategyFactory.createStrategy(
								StrongestStrategy.class.getSimpleName(), tower);
						if (lockCreepButton.getModel().isSelected())
							strategy.lock();
						context.setStrategy(strategy);
					} else
						hardStrategyButton.setSelected(true);
				} else
					hardStrategyButton.setSelected(false);
			}
		});

		closeStrategyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					if (hardStrategyButton.getModel().isSelected()) {
						Strategy strategy = StrategyFactory.createStrategy(
								ClosestStrategy.class.getSimpleName(), tower);
						if (lockCreepButton.getModel().isSelected())
							strategy.lock();
						context.setStrategy(strategy);
					} else
						hardStrategyButton.setSelected(true);
				} else
					hardStrategyButton.setSelected(false);
			}
		});

		fastestStrategyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					if (hardStrategyButton.getModel().isSelected()) {
						Strategy strategy = StrategyFactory.createStrategy(
								FastestStrategy.class.getSimpleName(), tower);
						if (lockCreepButton.getModel().isSelected())
							strategy.lock();
						context.setStrategy(strategy);
					} else
						hardStrategyButton.setSelected(true);
				} else
					hardStrategyButton.setSelected(false);
			}
		});

		farthestStrategyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					if (hardStrategyButton.getModel().isSelected()) {
						Strategy strategy = StrategyFactory.createStrategy(
								FarthestStrategy.class.getSimpleName(), tower);
						if (lockCreepButton.getModel().isSelected())
							strategy.lock();
						context.setStrategy(strategy);
					} else
						hardStrategyButton.setSelected(true);
				} else
					hardStrategyButton.setSelected(false);
			}
		});

		lockCreepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Tower tower = context.selectTower();
				if (tower.isReady()) {
					Strategy strategy = tower.getStrategy();
					if (lockCreepButton.getModel().isSelected())
						strategy.lock();
					else
						strategy.unlock();
					context.setStrategy(strategy);
				} else {
					lockCreepButton.setSelected(false);
				}
			}
		});

	}

	private void initInfoPanel() {
		java.awt.GridBagConstraints gridBagConstraints;

		this.info = new JPanel();
		this.info.setBounds(0, 0, 130, 125);
		this.info.setVisible(false);
		this.info.setBackground(Color.BLACK);
		this.info.setLayout(new java.awt.GridBagLayout());
		this.add(info);

		// general Tower information

		this.infoTower = new JLabel("Tower Info");
		this.infoSellPrice = new JLabel("Sell for 75%");
		this.infoDamage = new JLabel("Damage Info");
		this.infoSpeed = new JLabel("Speed Info");
		this.infoRange = new JLabel("Range Info");
		this.infoSpecial = new JLabel("Specials Info");

		this.infoTower.setFont(headFont);
		this.infoTower.setBounds(5, 5, 150, 20);

		this.infoSpecial.setFont(specialFont);

		this.infoTower.setForeground(Color.GREEN);
		this.infoSellPrice.setForeground(Color.GREEN);
		this.infoDamage.setForeground(Color.GREEN);
		this.infoSpeed.setForeground(Color.GREEN);
		this.infoRange.setForeground(Color.GREEN);
		this.infoSpecial.setForeground(Color.GREEN);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoTower, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoSellPrice, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoDamage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoSpeed, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.info.add(infoSpecial, gridBagConstraints);
	}

	private void initUpgradePanel() {
		java.awt.GridBagConstraints gridBagConstraints;

		this.upgrade = new JPanel();
		this.upgrade.setBounds(0, 0, 130, 125);
		this.upgrade.setVisible(true);
		this.upgrade.setBackground(Color.BLACK);
		this.upgrade.setLayout(new java.awt.GridBagLayout());
		this.add(upgrade);

		// Tower upgrade informations
		this.upgradeTower = new JLabel("Upgrade tower to");
		this.upgradePrice = new JLabel("Price: ");
		this.upgradeDamage = new JLabel("Damage: ");
		this.upgradeSpeed = new JLabel("Speed: ");
		this.upgradeRange = new JLabel("Range: ");
		this.upgradeSpecial = new JLabel("Specials:  TODO");

		this.upgradeTower.setFont(headFont);
		this.upgradeSpecial.setFont(specialFont);

		this.upgradeTower.setForeground(Color.GREEN);
		this.upgradePrice.setForeground(Color.GREEN);
		this.upgradeDamage.setForeground(Color.GREEN);
		this.upgradeSpeed.setForeground(Color.GREEN);
		this.upgradeRange.setForeground(Color.GREEN);
		this.upgradeSpecial.setForeground(Color.GREEN);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradeTower, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradePrice, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradeDamage, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradeSpeed, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradeRange, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.weightx = 100.0;
		gridBagConstraints.weighty = 100.0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.upgrade.add(upgradeSpecial, gridBagConstraints);
	}

	private void initTowerPanel() {
		java.awt.GridBagConstraints gridBagConstraints;

		this.towerButtons = new JPanel();
		this.towerButtons.setBounds(130, 0, 102, 124);
		this.towerButtons.setVisible(true);
		this.towerButtons.setBackground(Color.BLACK);
		this.towerButtons.setLayout(new java.awt.GridBagLayout());
		this.add(towerButtons);

		this.sellButton = new JButton("Sell");
		this.upgradeButton = new JButton("Upgrade");
		this.strategyLabel = new JLabel("Strategy");
		this.weakStrategyButton = new JToggleButton("Weak");
		this.hardStrategyButton = new JToggleButton("Hard");
		this.closeStrategyButton = new JToggleButton("Close");
		this.fastestStrategyButton = new JToggleButton("Fastest");
		this.farthestStrategyButton = new JToggleButton("Farthest");
		this.lockCreepButton = new JToggleButton("Lock");

		this.sellButton.setFont(buttonFont);
		this.sellButton.setBackground(Color.black);
		this.sellButton.setForeground(Color.GREEN);
		this.sellButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.upgradeButton.setFont(buttonFont);
		this.upgradeButton.setBackground(Color.black);
		this.upgradeButton.setForeground(Color.GREEN);
		this.upgradeButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.strategyLabel.setBackground(Color.black);
		this.strategyLabel.setForeground(Color.GREEN);
		this.strategyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		this.weakStrategyButton.setFont(buttonFont);
		this.weakStrategyButton.setBackground(Color.black);
		this.weakStrategyButton.setForeground(Color.GREEN);
		this.weakStrategyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.hardStrategyButton.setFont(buttonFont);
		this.hardStrategyButton.setBackground(Color.black);
		this.hardStrategyButton.setForeground(Color.GREEN);
		this.hardStrategyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.closeStrategyButton.setFont(buttonFont);
		this.closeStrategyButton.setBackground(Color.black);
		this.closeStrategyButton.setForeground(Color.GREEN);
		this.closeStrategyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.fastestStrategyButton.setFont(buttonFont);
		this.fastestStrategyButton.setBackground(Color.black);
		this.fastestStrategyButton.setForeground(Color.GREEN);
		this.fastestStrategyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		this.farthestStrategyButton.setFont(buttonFont);
		this.farthestStrategyButton.setBackground(Color.black);
		this.farthestStrategyButton.setForeground(Color.GREEN);
		this.farthestStrategyButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
		
		this.lockCreepButton.setFont(buttonFont);
		this.lockCreepButton.setBackground(Color.black);
		this.lockCreepButton.setForeground(Color.GREEN);
		this.lockCreepButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(sellButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(upgradeButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(strategyLabel, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(weakStrategyButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(hardStrategyButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(closeStrategyButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(fastestStrategyButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(lockCreepButton, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.towerButtons.add(farthestStrategyButton, gridBagConstraints);


	}

	public void creditsChanged(Context context) {
		checkUpgradeTower();

	}

	public void incomeChanged(Context context) {
	}

	public void incomeTimeChanged(Context context, int seconds) {
	}

	public void livesChanged(Context context) {
	}

	/**
	 * changes the TowerInfoPanels.
	 * 
	 * @param context
	 *            context
	 * @param message
	 *            which Panel
	 */
	public void selectedChanged(Context context, String message) {
		if (context.getSelectedTower() == null) {
			return;
		}
		// show SelectTowerInfoPanel
		if (message.equals("tower")) {
			// general info panel
			this.infoTower.setText(context.getSelectedTower().getType()
					.getName());
			this.infoSellPrice
					.setText("Sell for: "
							+ (int) (context.getSelectedTower().getTotalPrice() * 0.75));
			this.infoDamage.setText("Damage: "
					+ context.getSelectedTower().getType().getDamage());
			this.infoSpeed.setText("Speed: "
					+ TowerType.translateSpeed(context
							.getSelectedTower().getType().getSpeed()));
			this.infoRange.setText("Range: "
					+ (int) context.getSelectedTower().getType().getRange());
			this.infoSpecial.setText(context.getSelectedTower().getType()
					.getSpecial());

			// upgrade info panel
			if (context.getSelectedTower().isUpgradable()) {
				upgradeTower.setText(context.getSelectedTower().getType().getNext().getName());
				upgradePrice.setText("Price: "
						+ context.getSelectedTower().getType().getNext().getPrice());
				upgradeDamage.setText("Damage: "
						+ context.getSelectedTower().getType().getNext()
								.getDamage());
				upgradeSpeed.setText("Speed: "
						+ TowerType.translateSpeed(context
								.getSelectedTower().getType().getNext()
								.getSpeed()));
				upgradeRange.setText("Range: "
						+ (int) context.getSelectedTower().getType().getNext()
								.getRange());
				upgradeSpecial.setText(context.getSelectedTower().getType()
						.getNext().getSpecial());
			}

			if ((context.getSelectedTower().isUpgradable())
					&& (context.getSelectedTower().isReady())
					&& (context.getCredits() >= context.getSelectedTower()
							.getType().getNext().getPrice())) {
				this.upgradeButton.setEnabled(true);
			} else {
				upgradeButton.setEnabled(false);
			}
			gameScreen.getNewTowerInfoPanel().setVisible(false);
			gameScreen.getNoInfoPanel().setVisible(false);
			gameScreen.setLastTowerInfoPanel(this);
			this.setVisible(true);

		} else if (message.equals("sell")) {
			gameScreen.getLastTowerInfoPanel().setVisible(false);
			gameScreen.setLastTowerInfoPanel(gameScreen.getNoInfoPanel());
			gameScreen.getNoInfoPanel().setVisible(true);
		} else if (message.equalsIgnoreCase("upgrade")) {
			checkUpgradeTower();
		} else if (message.equalsIgnoreCase("strategy")) {
			Strategy fcs;
			fcs = context.getSelectedTower().getNewStrategy();
			if (fcs == null)
				fcs = context.getSelectedTower().getStrategy();
			weakStrategyButton.getModel().setSelected(
					fcs instanceof WeakestStrategy);
			hardStrategyButton.getModel().setSelected(
					fcs instanceof StrongestStrategy);
			closeStrategyButton.getModel().setSelected(
					fcs instanceof ClosestStrategy);
			fastestStrategyButton.getModel().setSelected(
					fcs instanceof FastestStrategy);
			farthestStrategyButton.getModel().setSelected(
					fcs instanceof FarthestStrategy);
			lockCreepButton.getModel().setSelected(fcs.isLocked());
		}
	}

	/**
	 * disables the upgrade button if there is not enough money.
	 */
	private void checkUpgradeTower() {
		if (this.context.getSelectedTower() == null)
			return;
		int credits = context.getCredits();
		if (context.getSelectedTower().isUpgradable()
				&& context.getSelectedTower().isReady()
				&& (credits >= context.getSelectedTower().getType().getNext().getPrice())) {
			this.upgradeButton.setEnabled(true);
		} else {
			this.upgradeButton.setEnabled(false);
		}
	}
}

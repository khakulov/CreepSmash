package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.contexts.ContextListener;
import com.creepsmash.client.gui.screens.GameScreen;
import com.creepsmash.common.TowerType;


/**
 * Panel with Buttons to select Tower.
 */
public class TowerSelPanel extends JPanel implements ContextListener {

	private static final long serialVersionUID = 1L;

	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;

	private Context context;

	/**
	 * sets Layout to Gridlayout. adds Buttons to the Panel
	 * 
	 * @param gamepanel
	 *            for getting object Gamepanel
	 */
	public TowerSelPanel(GameScreen gamepanel) {
		this.context = gamepanel.getGame().getContext(Core.getPlayerId());
		this.context.addContextListener(this);

		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(233, 100));
		this.setSize(233, 100);
		this.setBounds(700, 100, 233, 100);
		this.setLayout(new GridLayout(2, 3));

		button1 = new TowerBuyButton(gamepanel, TowerType.tower1, "towerIconButton1");
		button2 = new TowerBuyButton(gamepanel, TowerType.tower2, "towerIconButton2");
		button3 = new TowerBuyButton(gamepanel, TowerType.tower3, "towerIconButton3");
		button4 = new TowerBuyButton(gamepanel, TowerType.tower4, "towerIconButton4");
		button5 = new TowerBuyButton(gamepanel, TowerType.tower5, "towerIconButton5");
		button6 = new TowerBuyButton(gamepanel, TowerType.tower6, "towerIconButton6");
		add(button1);
		add(button2);
		add(button3);
		add(button4);
		add(button5);
		add(button6);
	}

	/**
	 * Enable/disable Buttons according to income.
	 */
	public void updateButtons() {
		int credits = this.context.getCredits();

		if ((credits >= TowerType.tower1.getPrice())) {
			this.button1.setEnabled(true);
		} else {
			this.button1.setEnabled(false);
		}

		if ((credits >= TowerType.tower2.getPrice())) {
			this.button2.setEnabled(true);
		} else {
			this.button2.setEnabled(false);
		}
		if ((credits >= TowerType.tower3.getPrice())) {
			this.button3.setEnabled(true);
		} else {
			this.button3.setEnabled(false);
		}
		if ((credits >= TowerType.tower4.getPrice())) {
			this.button4.setEnabled(true);
		} else {
			this.button4.setEnabled(false);
		}
		if ((credits >= TowerType.tower5.getPrice())) {
			this.button5.setEnabled(true);
		} else {
			this.button5.setEnabled(false);
		}
		if ((credits >= TowerType.tower6.getPrice())) {
			this.button6.setEnabled(true);
		} else {
			this.button6.setEnabled(false);
		}
	}

	public void creditsChanged(Context context) {
		this.updateButtons();

	}

	public void incomeChanged(Context context) {
	}

	public void incomeTimeChanged(Context context, int seconds) {
	}

	public void livesChanged(Context context) {
	}

	public void selectedChanged(Context context, String message) {
	}

}

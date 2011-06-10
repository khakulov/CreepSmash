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
import com.creepsmash.common.CreepType;


/**
 * Panel with Buttons to select Creeps.
 */
public class CreepSelPanel extends JPanel implements ContextListener {

	private static final long serialVersionUID = 1L;

	private Context context;

	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton button9;
	private JButton button10;
	private JButton button11;
	private JButton button12;
	private JButton button13;
	private JButton button14;
	private JButton button15;
	private JButton button16;

	/**
	 * sets Layout to Gridlayout. adds Buttons to the Panel
	 * 
	 * @param gameScreen
	 *            for getting object Gamepanel
	 */
	public CreepSelPanel(GameScreen gameScreen) {
		this.context = gameScreen.getGame().getContext(Core.getPlayerId());
		this.context.addContextListener(this);

		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(233, 120));
		this.setSize(233, 120);
		this.setBounds(700, 325, 233, 120);
		this.setLayout(new GridLayout( 4, 4));

		button1 = new CreepButton(gameScreen, CreepType.creep1, "1");
		button2 = new CreepButton(gameScreen, CreepType.creep2, "2");
		button3 = new CreepButton(gameScreen, CreepType.creep3, "3");
		button4 = new CreepButton(gameScreen, CreepType.creep4, "4");
		button5 = new CreepButton(gameScreen, CreepType.creep5, "5");
		button6 = new CreepButton(gameScreen, CreepType.creep6, "6");
		button7 = new CreepButton(gameScreen, CreepType.creep7, "7");
		button8 = new CreepButton(gameScreen, CreepType.creep8, "8");
		button9 = new CreepButton(gameScreen, CreepType.creep9, "9");
		button10 = new CreepButton(gameScreen, CreepType.creep10, "10");
		button11 = new CreepButton(gameScreen, CreepType.creep11, "11");
		button12 = new CreepButton(gameScreen, CreepType.creep12, "12");
		button13 = new CreepButton(gameScreen, CreepType.creep13, "13");
		button14 = new CreepButton(gameScreen, CreepType.creep14, "14");
		button15 = new CreepButton(gameScreen, CreepType.creep15, "15");
		button16 = new CreepButton(gameScreen, CreepType.creep16, "16");
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		this.add(button5);
		this.add(button6);
		this.add(button7);
		this.add(button8);
		this.add(button9);
		this.add(button10);
		this.add(button11);
		this.add(button12);
		this.add(button13);
		this.add(button14);
		this.add(button15);
		this.add(button16);
	}

	/**
	 * Enable/disable Buttons according to income.
	 */
	public synchronized void updateButtons() {
		int credits = this.context.getCredits();

		if ((credits >= CreepType.creep1.getPrice())) {
			this.button1.setEnabled(true);
			this.button1.setBackground(Color.BLACK);
		} else {
			this.button1.setEnabled(false);
			this.button1.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep2.getPrice())) {
			this.button2.setEnabled(true);
			this.button2.setBackground(Color.BLACK);
		} else {
			this.button2.setEnabled(false);
			this.button2.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep3.getPrice())) {
			this.button3.setEnabled(true);
			this.button3.setBackground(Color.BLACK);
		} else {
			this.button3.setEnabled(false);
			this.button3.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep4.getPrice())) {
			this.button4.setEnabled(true);
			this.button4.setBackground(Color.BLACK);
		} else {
			this.button4.setEnabled(false);
			this.button4.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep5.getPrice())) {
			this.button5.setEnabled(true);
			this.button5.setBackground(Color.BLACK);
		} else {
			this.button5.setEnabled(false);
			this.button5.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep6.getPrice())) {
			this.button6.setEnabled(true);
			this.button6.setBackground(Color.BLACK);
		} else {
			this.button6.setEnabled(false);
			this.button6.setBackground(Color.GRAY);
		}
		
		if ((credits >= CreepType.creep7.getPrice())) {
			this.button7.setEnabled(true);
			this.button7.setBackground(Color.BLACK);
		} else {
			this.button7.setEnabled(false);
			this.button7.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep8.getPrice())) {
			this.button8.setEnabled(true);
			this.button8.setBackground(Color.BLACK);
		} else {
			this.button8.setEnabled(false);
			this.button8.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep9.getPrice())) {
			this.button9.setEnabled(true);
			this.button9.setBackground(Color.BLACK);
		} else {
			this.button9.setEnabled(false);
			this.button9.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep10.getPrice())) {
			this.button10.setEnabled(true);
			this.button10.setBackground(Color.BLACK);
		} else {
			this.button10.setEnabled(false);
			this.button10.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep11.getPrice())) {
			this.button11.setEnabled(true);
			this.button11.setBackground(Color.BLACK);
		} else {
			this.button11.setEnabled(false);
			this.button11.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep12.getPrice())) {
			this.button12.setEnabled(true);
			this.button12.setBackground(Color.BLACK);
		} else {
			this.button12.setEnabled(false);
			this.button12.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep13.getPrice())) {
			this.button13.setEnabled(true);
			this.button13.setBackground(Color.BLACK);
		} else {
			this.button13.setEnabled(false);
			this.button13.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep14.getPrice())) {
			this.button14.setEnabled(true);
			this.button14.setBackground(Color.BLACK);
		} else {
			this.button14.setEnabled(false);
			this.button14.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep15.getPrice())) {
			this.button15.setEnabled(true);
			this.button15.setBackground(Color.BLACK);
		} else {
			this.button15.setEnabled(false);
			this.button15.setBackground(Color.GRAY);
		}
		if ((credits >= CreepType.creep16.getPrice())) {
			this.button16.setEnabled(true);
			this.button16.setBackground(Color.BLACK);
		} else {
			this.button16.setEnabled(false);
			this.button16.setBackground(Color.GRAY);
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

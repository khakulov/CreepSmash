package com.creepsmash.client.game.panels;

import java.awt.Color;

import javax.swing.JPanel;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.contexts.ContextListener;
import com.creepsmash.client.gui.screens.GameScreen;



/**
 * Panel that shows nothing or maybe a picture later on.
 * If the user deselects a tower or klicks on an empty field
 * this Panel will be shown.
 */
public class NoInfoPanel extends JPanel implements ContextListener {

	private static final long serialVersionUID = 1L;
	private GameScreen gameScreen;
	
	/**
	 * Creates new NoInfoPanel.
	 * @param gamepanel gamepanle
	 * @param width	width of the Panel
	 * @param height height of the Panel
	 */
	public NoInfoPanel(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		this.setBackground(Color.BLACK);
		this.setBounds(700, 200, 233, 125);

		Context context = this.gameScreen.getGame().getContext(Core.getPlayerId());
		context.addContextListener(this);
	}

	public void creditsChanged(Context context) {
	}

	public void incomeChanged(Context context) {
	}

	public void incomeTimeChanged(Context context, int seconds) {
	}

	public void livesChanged(Context context) {
	}

	/**
	 * changes the TowerInfoPanels.
	 * @param context context
	 * @param message which Panel
	 */
	public void selectedChanged(Context context, String message) {
		if (message.equals("empty")) {
			this.gameScreen.getSelectedTowerInfoPanel().setVisible(false);
			this.gameScreen.getNewTowerInfoPanel().setVisible(false);			
			this.gameScreen.setLastTowerInfoPanel(this);
			setVisible(true);
		}
		
	}

}

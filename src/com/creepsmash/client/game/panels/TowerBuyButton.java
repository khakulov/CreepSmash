package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.gui.screens.GameScreen;
import com.creepsmash.common.TowerType;


public class TowerBuyButton extends JButton implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private Context context;
	private GameScreen gameScreen;
	private TowerType type;


	public TowerBuyButton(GameScreen gameScreen, TowerType type, String iconName) {
		this.context = gameScreen.getGame().getContext(Core.getPlayerId());
		this.gameScreen = gameScreen;
		this.type = type;
		Font font = new Font("Helvetica", Font.PLAIN, 9);
		setFont(font);
		setBackground(Color.BLACK);
		setBorderPainted(false);
		setForeground(Color.GREEN);
		setIcon(createIcon(iconName));
		setDisabledIcon(createIcon(iconName + "disable"));
		setEnabled(false);
		addActionListener(this);
		addMouseListener(this);
	}

	private Icon createIcon(String name) {
		//FIXME: This should be a Constant (in IConstants?)
		String path = "com/creepsmash/client/resources/pictures/";
		Icon icon = new ImageIcon(getClass().getClassLoader().getResource(path + name + ".png"));
		return icon;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.context.isDead())
			return;
		this.context.setNextTower(type);
		this.context.getGameBoard().deSelectTowers();
		this.context.setSelectedTower(null);
		this.gameScreen.setLastTowerInfoPanel(gameScreen.getTowerInfoPanel());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.gameScreen.getLastTowerInfoPanel().setVisible(false);
		updateTowerInfo();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.gameScreen.getTowerInfoPanel().setVisible(false);
		this.gameScreen.getLastTowerInfoPanel().setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	private void updateTowerInfo() {
		gameScreen.getTowerInfoPanel().showTowerInfo(this.type);
		gameScreen.getTowerInfoPanel().setVisible(true);
	}

}

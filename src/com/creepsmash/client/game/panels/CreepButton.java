package com.creepsmash.client.game.panels;

import java.awt.Color;
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
import com.creepsmash.common.CreepType;


public class CreepButton extends JButton implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private Context context;
	private GameScreen gameScreen;
	private CreepType type;

	public CreepButton(GameScreen gameScreen, CreepType type, String iconName) {
		this.context = gameScreen.getGame().getContext(Core.getPlayerId());
		this.gameScreen = gameScreen;
		this.type = type;

		this.setBackground(Color.GRAY);
		this.setBorderPainted(false);
		this.setIcon(createIcon(iconName));
		this.setFocusable(false);
		this.setEnabled(false);
		this.addActionListener(this);
		this.addMouseListener(this);
	}

	private Icon createIcon(String name) {
		// FIXME This should be a Constant (in IConstants?)
		String path = "com/creepsmash/client/resources/creeps/";
		return new ImageIcon(getClass().getClassLoader().getResource(path + name + ".png"));
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (!this.context.isDead()) {
			if (((e.getModifiers() & ActionEvent.SHIFT_MASK) > 0) && this.context.readyForNewWave()) {
				this.context.sendCreepsWave(type);
			} else if (this.context.readyForNewCreep()) {
				this.context.sendCreep(type);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.gameScreen.getLastTowerInfoPanel().setVisible(false);
		this.gameScreen.getCreepInfoPanel().showCreepInfo(this.type);
		this.gameScreen.getCreepInfoPanel().setVisible(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.gameScreen.getCreepInfoPanel().setVisible(false);
		this.gameScreen.getLastTowerInfoPanel().setVisible(true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}

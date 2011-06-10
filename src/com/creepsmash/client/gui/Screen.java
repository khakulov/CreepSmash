package com.creepsmash.client.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.grids.EmptyGrid;
import com.creepsmash.client.game.panels.BoardPanel;
import com.creepsmash.client.gui.panels.HelpDialog;
import com.creepsmash.client.utils.SoundManager;



public abstract class Screen extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private boolean focused = false;
	private JComponent focusComponent = null;

	/**
	 * Method gets called when this screen gets displayed.
	 */
	public abstract void start();

	/**
	 * Method gets called when this screen is deactivated.
	 */
	public abstract void end();

	/**
	 * Initializes the screen. Adds EventListeners to the panel.
	 * 
	 * @param core
	 *            the game's core
	 */
	@SuppressWarnings("serial")
	public void initialize() {
		this.setBounds(0, 0, Core.WIDTH, Core.HEIGHT);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		addShortcut("F1", new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				JDialog frame = new HelpDialog((JFrame) getRootPane().getParent());
				frame.setTitle("CreepSmash - Help");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setBounds((screenSize.width - 624) / 2, (screenSize.height - 680) / 2, 624, 680);
				frame.setVisible(true);
			}
		});

		addShortcut("F2", new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				SoundManager.toggleMute();
			}
		});

		addShortcut("F3", new AbstractAction() {
			public void actionPerformed(ActionEvent event) {
				BoardPanel.ANTIALIAS = !BoardPanel.ANTIALIAS;
				EmptyGrid.ALPHA = !EmptyGrid.ALPHA;
			}
		});
	}

	public void addShortcut(String key, Action action) {
		KeyStroke keyStroke = KeyStroke.getKeyStroke(key);
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
		getActionMap().put(key, action);
	}

	/**
	 * Set the Focus to this Component.
	 * 
	 * @param component
	 *            the focusable Component
	 */
	public void setFocus(JComponent component) {
		this.focused = false;
		this.focusComponent = component;
	}

	/**
	 * Overwritten paint method to set focus after an update. Otherwise, some
	 * window managers don't have problems.
	 * 
	 * @param graphics
	 *            Graphic Object
	 */
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if (this.focusComponent != null && !this.focused) {
			this.focused = true;
			this.focusComponent.requestFocus();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}

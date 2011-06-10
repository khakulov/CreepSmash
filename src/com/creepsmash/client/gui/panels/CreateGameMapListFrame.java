package com.creepsmash.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.creepsmash.client.gui.screens.CreateGameScreen;
import com.creepsmash.common.GameMap;


/**
 * The Create GameList Panel
 */
public class CreateGameMapListFrame extends JFrame {

	/**
	 * @param args
	 */
	private static final long serialVersionUID = 4L;

	private JScrollPane jScrollCreateGameListPanel = new JScrollPane();
	private JPanel content = new JPanel();
	private CreateGameScreen creategamePanel;

	public CreateGameMapListFrame(CreateGameScreen createGamePanel, String title) {
		super();
		this.setTitle(title);
		this.creategamePanel = createGamePanel;
		this.init();
	}

	/**
	 * Initialize the Panel.
	 */
	private void init() {

		this.setBackground(Color.BLACK);
		this.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width - 500) / 2, (screenSize.height - 670) / 2, 500, 670);

		int GridX = 4;

		int GridY = Math.round((GameMap.values().length / (float) GridX) + 1f);

		content.setLayout(new GridLayout(GridY, GridX));
		content.setBackground(Color.BLACK);
		content.setDoubleBuffered(true);
		jScrollCreateGameListPanel
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollCreateGameListPanel.setViewportView(content);
		jScrollCreateGameListPanel.getVerticalScrollBar().setUnitIncrement(18);

		this.add(jScrollCreateGameListPanel);

		final CreateGameMapListFrame TR = this;

		new Thread() {
			@Override
			public void run() {
				for (GameMap m : GameMap.values()) {
					content.add(new CreateGameMapListThumailBoxPanel(TR, m));
					content.revalidate();
				}
			}
		}.start();
	}

	/**
	 * Select the map in creategamePanel
	 */
	public void selectMap(int id) {
		this.creategamePanel.selectMap(id);
	}

}

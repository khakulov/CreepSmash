package com.creepsmash.client.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.screens.LobbyScreen;
import com.creepsmash.common.GameMap;
import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.GamesMessage;


public class LobbyGamesPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private LobbyScreen lobbyScreen;
	private JTable gameinfoWaiting;
	private JTable gameinfoRunning;
	private int totalPlayersCount;
	private List<GameDescription> games;

	public LobbyGamesPane(LobbyScreen lobbyScreen) {
		this.lobbyScreen = lobbyScreen;

		this.setBounds(20, 50, 600, 300);
		this.setForeground(Color.black);

		this.gameinfoWaiting = new JTable();
		this.gameinfoWaiting.setBackground(Color.BLACK);
		this.gameinfoWaiting.setForeground(Color.GREEN);
		this.gameinfoWaiting.setSelectionBackground(Color.GREEN);
		this.gameinfoWaiting.setSelectionForeground(Color.BLACK);
		this.gameinfoWaiting.setDoubleBuffered(true);
		this.gameinfoWaiting.setIntercellSpacing(new Dimension(0, 0));
		this.gameinfoWaiting.setShowVerticalLines(false);
		this.gameinfoWaiting.setShowHorizontalLines(false);
		this.gameinfoWaiting.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.gameinfoWaiting.setModel(new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		this.gameinfoWaiting.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int waitingIndex = LobbyGamesPane.this.gameinfoWaiting.getSelectedRow();
				if (waitingIndex != -1) {
					int runningIndex = LobbyGamesPane.this.gameinfoRunning.getSelectedRow();
					if (runningIndex != -1)
						LobbyGamesPane.this.gameinfoRunning.removeRowSelectionInterval(runningIndex, runningIndex);
					int gameId = Integer.parseInt(LobbyGamesPane.this.gameinfoWaiting.getValueAt(waitingIndex, 7).toString());
					for (GameDescription gd : LobbyGamesPane.this.games) {
						if (gd.getGameId() == gameId) {
							LobbyGamesPane.this.lobbyScreen.setGameInfoEditorPaneSelectGame(gd);
							return;
						}
					}
				}
			}
		});
		this.gameinfoWaiting.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int waitingIndex = LobbyGamesPane.this.gameinfoWaiting.getSelectedRow();
					if (LobbyGamesPane.this.gameinfoWaiting.getColumnCount() < 2)
						return;
					int gameId = Integer.parseInt(LobbyGamesPane.this.gameinfoWaiting.getValueAt(waitingIndex, 7).toString());
					for (GameDescription gd : LobbyGamesPane.this.games) {
						if (gd.getGameId() == gameId) {
							LobbyGamesPane.this.lobbyScreen.joinGame(gd);
							return;
						}
					}
				}
			}
		});

		JScrollPane gamesScrollPaneWaiting = new JScrollPane();
		gamesScrollPaneWaiting.setViewportView(this.gameinfoWaiting);
		this.addTab("Games Waiting", null, gamesScrollPaneWaiting, "Waiting Games");


		this.gameinfoRunning = new JTable();
		this.gameinfoRunning.setBackground(Color.BLACK);
		this.gameinfoRunning.setForeground(Color.GREEN);
		this.gameinfoRunning.setSelectionBackground(Color.GREEN);
		this.gameinfoRunning.setSelectionForeground(Color.BLACK);
		this.gameinfoRunning.setDoubleBuffered(true);
		this.gameinfoRunning.setIntercellSpacing(new Dimension(0, 0));
		this.gameinfoRunning.setShowVerticalLines(false);
		this.gameinfoRunning.setShowHorizontalLines(false);
		this.gameinfoRunning.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.gameinfoRunning.setModel(new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		this.gameinfoRunning.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int runningIndex = LobbyGamesPane.this.gameinfoRunning.getSelectedRow();
				if (runningIndex != -1) {
					int waitingIndex = LobbyGamesPane.this.gameinfoWaiting.getSelectedRow();
					if (waitingIndex != -1)
						LobbyGamesPane.this.gameinfoWaiting.removeRowSelectionInterval(waitingIndex, waitingIndex);
					int gameId = Integer.parseInt(LobbyGamesPane.this.gameinfoRunning.getValueAt(runningIndex, 7).toString());
					for (GameDescription gd : LobbyGamesPane.this.games) {
						if (gd.getGameId() == gameId) {
							LobbyGamesPane.this.lobbyScreen.setGameInfoEditorPaneSelectGame(gd);
							return;
						}
					}
				}
			}
		});

		JScrollPane gamesScrollPaneRunning = new JScrollPane();
		gamesScrollPaneRunning.setViewportView(this.gameinfoRunning);
		this.addTab("Games Running", null, gamesScrollPaneRunning, "Running Games");
	}


	/**
	 * Gets all available games and add them to the gameslist textarea.
	 * 
	 * @param gamesMessage
	 *            the gamesMessage
	 */
	public synchronized void setGameList(GamesMessage gamesMessage) {
		int oldSelectionWaiting = this.gameinfoWaiting.getSelectedRow();
		int oldSelectionRunning = this.gameinfoRunning.getSelectedRow();

		Object[] headerNames = new Object[] { "Name", "Players", "Map", "Mod", "Min/Max", "PW", "State", "GameID" };

		Vector<Vector<String>> rowsWaiting = new Vector<Vector<String>>();
		Vector<Vector<String>> rowsRunning = new Vector<Vector<String>>();

		DefaultTableModel modelWaiting = (DefaultTableModel) this.gameinfoWaiting.getModel();
		DefaultTableModel modelRunning = (DefaultTableModel) this.gameinfoRunning.getModel();

		if (gamesMessage != null) {
			this.games = new ArrayList<GameDescription>(gamesMessage.getGames());
			Collections.sort(games, new Comparator<GameDescription>() {
				public int compare(GameDescription a, GameDescription b) {
					return a.getState().compareToIgnoreCase(b.getState()) * -1;
				}
			});
			this.totalPlayersCount = 0;
			for (GameDescription gd : this.games) {
				int fullCount = gd.getNumberOfPlayers();
				int playersCount = gd.getCurrentPlayers();
				this.totalPlayersCount += playersCount;

				Vector<String> rowsData = new Vector<String>();
				rowsData.add(gd.getGameName());
				rowsData.add(String.valueOf(playersCount) + "/" + String.valueOf(fullCount));
				rowsData.add(GameMap.getMapById(gd.getMapId()).toString());

				// Mod
				if (gd.getGameMod() == 0) {
					rowsData.add((String) "<html><b>Normal</b></html>");
				} else if (gd.getGameMod() == 1) {
					rowsData.add((String) "<html><div style=\"color:red;\"><b>ALL vs ALL</b></div></html>");
				} else if (gd.getGameMod() == 2) {
					rowsData.add((String) "<html><div style=\"color:yellow;\"><b>Random send</b></div></html>");
				}

				rowsData.add(((gd.getMinEloPoints() == 0) ? "all" : gd.getMinEloPoints()) + "/"
						+ ((gd.getMaxEloPoints() == 0) ? "all" : gd.getMaxEloPoints()));

				rowsData.add((gd.getPasswort().equals("yes") ? "<html><div style=\"color:red;\"><b>" + gd.getPasswort()
						+ "</div></html>" : gd.getPasswort()));

				String State = gd.getState();
				if (State.compareToIgnoreCase("waiting") == 0) {
					rowsData.add("<html><DIV style=\"color:red;\"><b>" + State + "</DIV></HTML>");
				} else if (State.compareToIgnoreCase("ended") == 0) {
					rowsData.add("<html><DIV style=\"color:yellow;\">" + State + "</DIV></HTML>");
				} else {
					rowsData.add(State);
				}

				rowsData.add("" + gd.getGameId());

				if (State.equals("waiting")) {
					rowsWaiting.add(rowsData);
				} else {
					rowsRunning.add(rowsData);
				}
			}
		}

		TableColumn col = null;
		if (rowsWaiting.size() == 0) {
			int selectedRow = this.gameinfoWaiting.getSelectedRow();
			if (selectedRow != -1) {
				this.gameinfoWaiting.removeRowSelectionInterval(selectedRow, selectedRow);
				oldSelectionWaiting = -1;
			}

			final Object[] Info = new Object[] { "information" };
			final Vector<String> rowsData = new Vector<String>();
			rowsData.add("<html><DIV style='color:red;'><h3>No game waits for players.<br>Please provide a new. (Create Game)</h3></div></html>");
			rowsWaiting.add(rowsData);
			modelWaiting.setDataVector(rowsWaiting, new Vector<Object>(Arrays.asList(Info)));
			modelWaiting.fireTableDataChanged();
			this.gameinfoWaiting.setEnabled(false);

			col = gameinfoWaiting.getColumnModel().getColumn(0);
			col.setPreferredWidth(this.getWidth());
			col.setMinWidth(this.getWidth());
			col.setMaxWidth(this.getWidth());
			gameinfoWaiting.setRowHeight(50);

		} else {

			modelWaiting.setDataVector(rowsWaiting, new Vector<Object>(Arrays.asList(headerNames)));
			modelWaiting.fireTableDataChanged();
			gameinfoWaiting.setRowHeight(gameinfoRunning.getRowHeight());
			this.gameinfoWaiting.setEnabled(true);
			col = gameinfoWaiting.getColumnModel().getColumn(0);
			col.setPreferredWidth(175);
			col.setMinWidth(175);
			col.setMaxWidth(175);

			col = gameinfoWaiting.getColumnModel().getColumn(1);
			col.setPreferredWidth(50);
			col.setMinWidth(50);
			col.setMaxWidth(50);

			col = gameinfoWaiting.getColumnModel().getColumn(2);
			col.setPreferredWidth(100);
			col.setMinWidth(100);
			col.setMaxWidth(100);

			col = gameinfoWaiting.getColumnModel().getColumn(3);
			col.setPreferredWidth(80);
			col.setMinWidth(80);
			col.setMaxWidth(80);

			col = gameinfoWaiting.getColumnModel().getColumn(4);
			col.setPreferredWidth(65);
			col.setMinWidth(65);
			col.setMaxWidth(65);

			col = gameinfoWaiting.getColumnModel().getColumn(5);
			col.setPreferredWidth(30);
			col.setMinWidth(30);
			col.setMaxWidth(30);

			col = gameinfoWaiting.getColumnModel().getColumn(6);
			col.setPreferredWidth(50);
			col.setMinWidth(50);
			col.setMaxWidth(50);

			col = gameinfoWaiting.getColumnModel().getColumn(7);
			col.setPreferredWidth(50);
			col.setMinWidth(50);
			col.setMaxWidth(50);
		}

		modelRunning.setDataVector(rowsRunning, new Vector<Object>(Arrays.asList(headerNames)));
		modelRunning.fireTableDataChanged();

		col = gameinfoRunning.getColumnModel().getColumn(0);
		col.setPreferredWidth(225);
		col.setMinWidth(225);
		col.setMaxWidth(225);

		col = gameinfoRunning.getColumnModel().getColumn(1);
		col.setPreferredWidth(50);
		col.setMinWidth(50);
		col.setMaxWidth(50);

		col = gameinfoRunning.getColumnModel().getColumn(2);
		col.setPreferredWidth(100);
		col.setMinWidth(100);
		col.setMaxWidth(100);

		col = gameinfoRunning.getColumnModel().getColumn(3);
		col.setPreferredWidth(80);
		col.setMinWidth(80);
		col.setMaxWidth(80);

		col = gameinfoRunning.getColumnModel().getColumn(4);
		col.setPreferredWidth(65);
		col.setMinWidth(65);
		col.setMaxWidth(65);

		col = gameinfoRunning.getColumnModel().getColumn(5);
		col.setPreferredWidth(30);
		col.setMinWidth(30);
		col.setMaxWidth(30);

		col = gameinfoRunning.getColumnModel().getColumn(6);
		col.setPreferredWidth(50);
		col.setMinWidth(50);
		col.setMaxWidth(50);

		col = gameinfoRunning.getColumnModel().getColumn(7);
		col.setPreferredWidth(0);
		col.setMinWidth(0);
		col.setMaxWidth(0);
		col.setWidth(0);

		if (oldSelectionWaiting != -1) {
			try {
				this.gameinfoWaiting.setRowSelectionInterval(oldSelectionWaiting, oldSelectionWaiting);
			} catch (IllegalArgumentException e) {
				Core.logger.info("setRowSelectionInterval illegal argument");
				this.lobbyScreen.setGameInfoEditorPaneHTML(null);
			}
		} else if (oldSelectionRunning != -1) {
			try {
				this.gameinfoRunning.setRowSelectionInterval(oldSelectionRunning, oldSelectionRunning);
			} catch (IllegalArgumentException e) {
				Core.logger.info("setRowSelectionInterval illegal argument");
				this.lobbyScreen.setGameInfoEditorPaneHTML(null);
			}
		} else {
			this.lobbyScreen.setGameInfoEditorPaneHTML(null);
		}
	}


	/**
	 * @return the totalPlayersCount
	 */
	public int getTotalPlayersCount() {
		return totalPlayersCount;
	}
}

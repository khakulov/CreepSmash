package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.messages.client.ScoreRequestMessage;
import com.creepsmash.common.messages.server.HighscoreEntry;
import com.creepsmash.common.messages.server.HighscoreResponseMessage;
import com.creepsmash.common.messages.server.ScoreResponseMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * Panel for the highscore.
 */
public class GameResultScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 4925493108175118730L;

	private final JLabel title;
	private JTable highscoreTable;
	private JScrollPane highscoreScrollPane;
	private final JButton back;
	
	private Map<Integer, String> players;
	
	private Vector<Vector<String>> tableData = new Vector<Vector<String>>();
	
	/**
	 * Creates a new HighscorePanel.
	 */
	public GameResultScreen(Map<Integer, String> players) {
		
		this.players = players;
		
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		this.title = new JLabel("Game Results");
		this.title.setBounds(400, 50, 400, 30);
		this.title.setForeground(Color.green);
		this.title.setFont(new Font("Arial", Font.BOLD, 20));
		
		this.back = new JButton("Back");
		this.back.setBounds(310, 640, 120, 25);
		this.back.setBackground(Color.BLACK);
		this.back.setForeground(Color.GREEN);		
		
		this.initHighscoreTable();
		
		
		this.add(this.title);
		this.add(this.highscoreScrollPane);
		this.add(this.back);
		
		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.popScreen();
			}
		};
		this.back.addActionListener(a2);
		
		this.processHighscoreMessage(null);
	}
	
	/**
	 * Initializes the highscore tabel.
	 */
	private void initHighscoreTable() {
		if (this.highscoreScrollPane == null) {
			this.highscoreScrollPane = new JScrollPane();

			this.highscoreScrollPane.setBounds(240, 120, 410, 500);
		}

		if (this.highscoreTable == null) {
			this.highscoreTable = new JTable();
			this.highscoreTable.setDoubleBuffered(true);
			this.highscoreTable.setBackground(Color.BLACK);
			this.highscoreTable.setForeground(Color.GREEN);

			this.highscoreTable.setIntercellSpacing(new Dimension(0, 0));
			this.highscoreTable.setShowVerticalLines(false);
			this.highscoreTable.setShowHorizontalLines(false);			
		}

		this.highscoreScrollPane.setViewportView(this.highscoreTable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void receive(ServerMessage m) {
		if (m instanceof ScoreResponseMessage) {
			this.processScoreMessage((ScoreResponseMessage) m);
		}
	}

	/**
	 * process the score response message.
	 * @param srm the message
	 */
	private void processScoreMessage(ScoreResponseMessage srm) {
		Vector<String> row = new Vector<String>();
		
		row.add(srm.getPlayerName());
		row.add(String.valueOf(srm.getPoints()));
		row.add(String.valueOf(srm.getOldPoints()));
		
		tableData.add(row);
		
		updateTable();
	}
	
	/**
	 * update the table.
	 * 
	 */
	private void updateTable() {
		final Object[] headerNames = new Object[] {
				"Name", "Points Total", "Score"};
		
		final DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 6115678865194002026L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		Collections.sort(tableData, new Comparator<Vector<String>>() {
			public int compare(Vector<String> a1, Vector<String> a2) {
				return (Integer.valueOf(a2.get(2))
						.compareTo(Integer.valueOf(a1.get(2))));
			}
		});
		
		model.setDataVector(tableData, new Vector<Object>(Arrays.asList(headerNames)));
		
		this.highscoreTable.setModel(model);
	}
	/**
	 * take actions needed for highscore.
	 * @param hrm the highscoreResponseMessage
	 */
	private void processHighscoreMessage(HighscoreResponseMessage hrm) {
		final Object[] headerNames = new Object[] {
				"Nr", "Name", "Score"};

		final Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		final DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 6115678865194002026L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		if (hrm != null) {
	
			ArrayList<HighscoreEntry> listA = new ArrayList<HighscoreEntry>(
					hrm.getHighscoreEntries());
			
			ArrayList<HighscoreEntry> list = new ArrayList<HighscoreEntry>();
			
			for (HighscoreEntry s : listA)  {
				if (players.containsValue(s.getPlayerName())) {
					list.add(s);
				}
			}
			Collections.sort(list, new Comparator<HighscoreEntry>() {
				public int compare(HighscoreEntry a, HighscoreEntry b) {
					Integer aPoints = a.getPoints() - a.getOldPoints();
					Integer bPoints = b.getPoints() - b.getOldPoints();
					if (aPoints.compareTo(bPoints) != 0) {
						return aPoints.compareTo(bPoints);
					} else {
						return a.getPlayerName().compareTo(b.getPlayerName());
					}
				}
			});
			
			int nr = 1;
			for (HighscoreEntry he : list) {
				final Vector<String> rowsData = new Vector<String>();
				
				rowsData.add(String.valueOf(nr));
				rowsData.add(he.getPlayerName().toString());

				if (he.getOldPoints() != null) {
					if (he.getOldPoints() >= 0) { 
						rowsData.add("+" + he.getOldPoints().toString());
					} else {
						rowsData.add(he.getOldPoints().toString());
					}
				}
				rows.add(rowsData);
				
				nr++;
			}
		}
		
		model.setDataVector(rows, new Vector<Object>(Arrays.asList(headerNames)));
		
		this.highscoreTable.setModel(model);
	}
	
	@Override
	public void end() {
		Network.removeListener(this);
	}

	@Override
	public void start() {
		Network.addListener(this);
		for (Integer i : this.players.keySet()) {
			ScoreRequestMessage srm = new ScoreRequestMessage(this.players.get(i));
			Network.sendMessage(srm);
		}
	}
	
}

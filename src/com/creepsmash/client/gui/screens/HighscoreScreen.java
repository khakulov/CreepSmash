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
import com.creepsmash.common.messages.client.HighscoreRequestMessage;
import com.creepsmash.common.messages.server.HighscoreEntry;
import com.creepsmash.common.messages.server.HighscoreResponseMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * Panel for the highscore.
 */
public class HighscoreScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private final JLabel title;
	private JTable highscoreTable;
	private JScrollPane highscoreScrollPane;
	private final JButton back;
	private final JButton refresh;
	private final JButton plus;
	private final JButton minus;
	
	// the actual highscore position 
	private int position = 0;
	private static int STEP = 30;
	/**
	 * Creates a new HighscorePanel.
	 */
	public HighscoreScreen() {
		
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		this.title = new JLabel("Highscore");
		this.title.setBounds(400, 50, 400, 30);
		this.title.setForeground(Color.green);
		this.title.setFont(new Font("Arial", Font.BOLD, 20));	
		
		this.plus = new JButton("+" + STEP);
		this.plus.setBounds(590, 640, 60, 25);
		this.plus.setBackground(Color.BLACK);
		this.plus.setForeground(Color.GREEN);	
		
		this.minus = new JButton("-" + STEP);
		this.minus.setBounds(240, 640, 60, 25);
		this.minus.setBackground(Color.BLACK);
		this.minus.setForeground(Color.GREEN);	
		this.minus.setEnabled(false);	
		
		this.back = new JButton("Back");
		this.back.setBounds(320, 640, 120, 25);
		this.back.setBackground(Color.BLACK);
		this.back.setForeground(Color.GREEN);	
		
		this.refresh = new JButton("Refresh");
		this.refresh.setBounds(450, 640, 120, 25);
		this.refresh.setBackground(Color.BLACK);
		this.refresh.setForeground(Color.GREEN);	
		
		this.initHighscoreTable();
		
		
		this.add(this.title);
		this.add(this.highscoreScrollPane);
		this.add(this.back);
		this.add(this.refresh);
		this.add(this.plus);
		this.add(this.minus);
		
		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Network.sendMessage(
						new HighscoreRequestMessage(position));
			}
		};
		this.refresh.addActionListener(a1);
		
		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.popScreen();
			}
		};
		this.back.addActionListener(a2);
		
		ActionListener a3 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				position += STEP;
				sendRequest();
			}
		};
		this.plus.addActionListener(a3);
		
		ActionListener a4 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (position > 0) {
					position -= STEP;
				}
				sendRequest();
			}
		};
		this.minus.addActionListener(a4);
		
		this.processHighscoreMessage(null);
	}
	/**
	 * sends Request.
	 */
	private void sendRequest() {
		if (position == 0) {
			minus.setEnabled(false);
		} else {
			minus.setEnabled(true);
		}
		Network.sendMessage(
				new HighscoreRequestMessage(position));
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
		if (m instanceof HighscoreResponseMessage) {
			this.processHighscoreMessage((HighscoreResponseMessage) m);
		}
	}

	/**
	 * take actions needed for highscore.
	 * @param hrm the highscoreResponseMessage
	 */
	private void processHighscoreMessage(HighscoreResponseMessage hrm) {
		final Object[] headerNames = new Object[] {
				"Nr", "Name", "Score", "Last Game" };

		final Vector<Vector<String>> rows = new Vector<Vector<String>>();
		
		final DefaultTableModel model = new DefaultTableModel() {
			private static final long serialVersionUID = 6115678865194002026L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		if (hrm != null) {
	
			ArrayList<HighscoreEntry> list = 
				new ArrayList<HighscoreEntry>(hrm.getHighscoreEntries());
			
			Collections.sort(list, new Comparator<HighscoreEntry>() {
				public int compare(HighscoreEntry a, HighscoreEntry b) {
					if (b.getPoints().compareTo(a.getPoints()) != 0) {
						return b.getPoints().compareTo(a.getPoints());
					} else {
						return a.getPlayerName().compareTo(b.getPlayerName());
					}
				}
			});
			
			int nr = 1;
			for (HighscoreEntry he : list) {
				final Vector<String> rowsData = new Vector<String>();
				
				rowsData.add(String.valueOf(position + nr));
				rowsData.add(he.getPlayerName().toString());
				rowsData.add(he.getPoints().toString());
				if (he.getOldPoints() != null) {
					if (he.getOldPoints() > 0) { 
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
	public void start() {
		Network.addListener(this);
		Network.sendMessage(new HighscoreRequestMessage(position));
	}

	@Override
	public void end() {
		Network.removeListener(this);
	}

}

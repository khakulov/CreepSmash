package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.gui.panels.ChatPane;
import com.creepsmash.client.gui.panels.WaitingGameCountdownThread;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.GameMap;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.ExitGameMessage;
import com.creepsmash.common.messages.client.KickPlayerRequestMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.KickPlayerResponseMessage;
import com.creepsmash.common.messages.server.KickedMessage;
import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.common.messages.server.PlayerJoinedMessage;
import com.creepsmash.common.messages.server.PlayerQuitMessage;
import com.creepsmash.common.messages.server.ServerMessage;
import com.creepsmash.common.messages.server.StartGameMessage;
import com.creepsmash.common.messages.server.StartGameResponseMessage;


/**
 * The WaitingGamePanel is coming directly to the CreateGamePanel. Also with
 * JOIN from the Lobby. There are waiting the players before the Game can start.
 * Only the Owner (player who creates game) can start the game, he can also kick
 * players.
 */
public class WaitingGameScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JButton kick = new JButton();
	private JButton start = new JButton();
	private JButton quit = new JButton();
	private JButton send = new JButton();

	private JTextArea contextinfodialog = new JTextArea("");
	private ChatPane chatdialog = new ChatPane();

	private JList playerlist;

	private JScrollPane jScrollPanejcontextinfodialog = new JScrollPane();
	private JScrollPane jScrollPanejwPlayersdialog = new JScrollPane();
	private JScrollPane jScrollPanejchatdialog = null;

	private JLabel headline = new JLabel();
	private JLabel description;
	private JTextField message = new JTextField();

	private ImageIcon preview;
	private JLabel previewLabel;

	private boolean ownPlayerIdMessageReceived = false;

	// key PlayerID, value position
	private Map<Integer, Integer> playersOrder = new TreeMap<Integer, Integer>();
	private Map<Integer, String> players = new TreeMap<Integer, String>();

	private GameDescription gameDescription;

	private boolean creator;

	/**
	 * Creates a new instance of CreateGamePanel.
	 * 
	 * @param minimumPlayerNumber
	 * 
	 */
	public WaitingGameScreen(GameDescription gameDescription, boolean creator) {
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.gameDescription = gameDescription;
		this.creator = creator;

		headline = new JLabel("Waiting for players ...");
		headline.setBounds(350, 50, 400, 30);
		headline.setForeground(Color.green);
		headline.setFont(new Font("Arial", Font.BOLD, 28));

		description = new JLabel();
		description.setBounds(256, 110, 417, 100);
		description.setBackground(Color.WHITE);
		description.setForeground(Color.green);

		kick.setBackground(Color.BLACK);
		kick.setForeground(Color.GREEN);
		kick.setBounds(688, 246, 75, 25);
		kick.setText("Kick");
		kick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				kickButtonActionPerformed(evt);
			}
		});

		start.setBackground(Color.BLACK);
		start.setForeground(Color.GREEN);
		start.setBounds(688, 222, 75, 25);
		start.setText("Start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				startButtonActionPerformed();
			}
		});

		start.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				startButtonActionPerformed();
			}
		});

		quit.setBackground(Color.BLACK);
		quit.setForeground(Color.GREEN);
		quit.setBounds(688, 270, 75, 25);
		quit.setText("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ExitGameMessage egm = new ExitGameMessage();
				Network.sendMessage(egm);
				Window.popScreen();
			}
		});

		quit.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				ExitGameMessage egm = new ExitGameMessage();
				Network.sendMessage(egm);
				Window.popScreen();
			}
		});

		send.setBackground(Color.BLACK);
		send.setForeground(Color.GREEN);
		send.setBounds(688, 580, 75, 25);
		send.setText("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});
		this.message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					send.doClick();
				}
			}
		});

		jScrollPanejcontextinfodialog.setBounds(256, 190, 417, 33);
		contextinfodialog.setText("");
		contextinfodialog.setBackground(Color.BLACK);
		contextinfodialog.setForeground(Color.WHITE);
		contextinfodialog.setEditable(false);
		jScrollPanejcontextinfodialog.setViewportView(contextinfodialog);

		jScrollPanejwPlayersdialog.setBounds(256, 222, 417, 80);
		playerlist = new JList();
		playerlist.setModel(new DefaultListModel());
		playerlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerlist.setLayoutOrientation(JList.VERTICAL);
		playerlist.setVisibleRowCount(-1);
		jScrollPanejwPlayersdialog.setViewportView(playerlist);

		this.jScrollPanejchatdialog = new JScrollPane(this.chatdialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPanejchatdialog.setBounds(256, 302, 417, 278);

		message.setBounds(256, 581, 417, 24);
		message.setEditable(true);
		message.setText("");
		this.setFocus(message);

		previewLabel = new JLabel();
		previewLabel.setBounds(573, 115, 100, 100);

		add(kick);
		add(start);
		add(quit);
		add(send);
		add(description);
		add(headline);
		add(previewLabel);
		add(jScrollPanejwPlayersdialog);
		add(jScrollPanejchatdialog);
		add(message);
	}

	/**
	 * displays the screen.
	 */
	@Override
	public void start() {
		Network.addListener(this);
		if (!this.isCreator()) {
			kick.setEnabled(false);
			start.setEnabled(false);
		}
		// Mod
		String gameMode = "";
		if (gameDescription.getGameMod() == 0) {
			gameMode = "<b>Normal</b>";
		} else if (gameDescription.getGameMod() == 1) {
			gameMode = "<span style=\"color:red;\"><b>ALL vs ALL</b></span>";
		} else if (gameDescription.getGameMod() == 2) {
			gameMode = "<span style=\"color:yellow;\"><b>Random send</b></span>";
		}
		description.setText("<html><table width='250px'><tr><td>"
				+ "Name: " + this.gameDescription.getGameName()
				+ "<br>Mode: " + gameMode
				+ "<br>Map: " + GameMap.getMapById(this.gameDescription.getMapId()).toString()
				+ "<br>Minimum Players: " + this.gameDescription.getNumberOfPlayers()
				+ "<br>Min/Max: "
				+ ((this.gameDescription.getMinEloPoints() == 0) ? "all" : this.gameDescription.getMinEloPoints())
				+ "/"
				+ ((this.gameDescription.getMaxEloPoints() == 0) ? "all" : this.gameDescription.getMaxEloPoints())
				+ "<br>Password: "
				+ (this.gameDescription.getPasswort().equals("") ? "(not set)" : this.gameDescription.getPasswort())
				+ "</td></tr></table></html>");

		try {
			preview = new ImageIcon(ImageIO.read(this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(GameMap.getMapById(this.gameDescription.getMapId()).getPicturePath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		preview.setImage(preview.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
		previewLabel.setIcon(preview);

	}

	/**
	 * method for disappearing the screen.
	 */
	@Override
	public void end() {
		Network.removeListener(this);
	}

	/**
	 * method for ActionEvent on startButton to start the game. with the given
	 * number of players
	 */
	public void startButtonActionPerformed() {

		start.setEnabled(false);
		WaitingGameCountdownThread wgct = new WaitingGameCountdownThread();
		wgct.start();
	}

	/**
	 * method for ActionEvent on kickButton to kick a Player. Only the owner can
	 * kick.
	 * 
	 * @param evt
	 *            for ActionEvent
	 */
	public void kickButtonActionPerformed(ActionEvent evt) {
		KickPlayerRequestMessage kprm = new KickPlayerRequestMessage();
		kprm.setPlayerName((String) playerlist.getSelectedValue());

		// can't kick the player himself
		if (((String) playerlist.getSelectedValue()).equals(Core.getPlayerName())) {
			return;
		}
		Network.sendMessage(kprm);
	}

	/**
	 * method for ActionEvent on sendButton to sen a message to chatdialog.
	 * 
	 * @param evt
	 *            for ActionEvent
	 */
	public void sendButtonActionPerformed(ActionEvent evt) {
		if (!message.getText().equals("")) {
			SendMessageMessage m = new SendMessageMessage();
			m.setMessage(message.getText());
			Network.sendMessage(m);
			message.setText("");
		}
	}

	/**
	 * method for update.
	 * 
	 * @param m
	 *            for ServerMessage
	 */
	public void receive(ServerMessage m) {

		if (m instanceof PlayerJoinedMessage) {
			PlayerJoinedMessage pjm = (PlayerJoinedMessage) m;
			DefaultListModel dl = (DefaultListModel) this.playerlist.getModel();

			dl.addElement(pjm.getPlayerName());

			if (!ownPlayerIdMessageReceived) {
				players.put(pjm.getPlayerId(), pjm.getPlayerName());
				Core.setPlayerId(pjm.getPlayerId());
				Core.setPlayerName(pjm.getPlayerName());
				ownPlayerIdMessageReceived = true;
			} else {
				players.put(pjm.getPlayerId(), pjm.getPlayerName());
			}

			SoundManager.hornbeepSound();
			this.chatdialog.sendChatText("System", pjm.getPlayerName() + "[" + pjm.getPlayerEloScore() + "]"
					+ " has joined!");

			changeButton();

		}

		if (m instanceof StartGameResponseMessage) {

			StartGameResponseMessage sgrm = (StartGameResponseMessage) m;
			if (sgrm.getResponseType() == IConstants.ResponseType.failed) {
				UIManager.put("OptionPane.background", Color.BLACK);
				UIManager.put("OptionPane.JButton.setForground", Color.BLACK);
				UIManager.put("Panel.background", Color.BLACK);
				UIManager.put("OptionPane.messageForeground", Color.GREEN);
				JOptionPane.showMessageDialog(this, "Could not start game!", "error", JOptionPane.ERROR_MESSAGE);
			}
		}

		if (m instanceof KickPlayerResponseMessage) {
			KickPlayerResponseMessage kprm = (KickPlayerResponseMessage) m;
			if (kprm.getResponseType().equals(IConstants.ResponseType.ok)) {
				DefaultListModel dl = (DefaultListModel) this.playerlist.getModel();

				removePlayer((String) this.playerlist.getSelectedValue());
				dl.removeElement(this.playerlist.getSelectedValue());
				changeButton();
			}
		}

		if (m instanceof KickedMessage) {
			UIManager.put("OptionPane.background", Color.BLACK);
			UIManager.put("OptionPane.JButton.setForground", Color.BLACK);
			UIManager.put("Panel.background", Color.BLACK);
			UIManager.put("OptionPane.messageForeground", Color.GREEN);
			JOptionPane.showMessageDialog(this, "You were kicked by the creator of the game", "Sorry",
					JOptionPane.ERROR_MESSAGE);
			Window.popScreen();
		}

		if (m instanceof StartGameMessage) {
			StartGameMessage sgm = (StartGameMessage) m;
			List<Integer> list = sgm.getPlayers();

			for (int i = 0; i < list.size(); i++) {
				this.playersOrder.put(list.get(i), i);
			}

			GameScreen gameScreen = new GameScreen(this.gameDescription.getGameMod(),
					sgm.getMapID(), this.players, this.playersOrder);
			Window.switchScreen(gameScreen);
		}

		if (m instanceof PlayerQuitMessage) {
			PlayerQuitMessage pqm = (PlayerQuitMessage) m;
			DefaultListModel dl = (DefaultListModel) this.playerlist.getModel();

			SoundManager.hornbeepSound();
			dl.removeElement(pqm.getPlayerName());
			removePlayer(pqm.getPlayerName());
			this.chatdialog.sendChatText("System", pqm.getPlayerName() + " has left...");

		}

		if (m instanceof MessageMessage) {
			MessageMessage mm = (MessageMessage) m;
			this.chatdialog.sendChatText(mm.getPlayerName(), mm.getMessage());
			SoundManager.clapSound();
		}
	}

	/**
	 * Removes a player from the players list.
	 * 
	 * @param name
	 *            the playername
	 */
	private void removePlayer(String name) {
		int kickId = -1;
		for (Integer i : players.keySet()) {
			if (players.get(i).equals(name)) {
				kickId = i.intValue();
			}
		}
		players.remove(kickId);
	}

	/**
	 * changes state of button when only 1 player is in waitingpanel.
	 */
	public void changeButton() {
		if (this.isCreator()) {
			if (this.playerlist.getModel().getSize() <= 1) {
				kick.setEnabled(false);
			} else if (this.playerlist.getModel().getSize() > 1) {
				kick.setEnabled(true);
			}
			if (this.playerlist.getModel().getSize() == this.gameDescription.getNumberOfPlayers()) {
				start.setEnabled(true);
			} else {
				start.setEnabled(false);
			}
		}
	}

	private boolean isCreator() {
		return this.creator;
	}
}

package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.html.HTMLEditorKit;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.gui.panels.ChatPane;
import com.creepsmash.client.gui.panels.LobbyGamesPane;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.GameMap;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.JoinGameRequestMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.RefreshMessage;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.GamesMessage;
import com.creepsmash.common.messages.server.JoinGameResponseMessage;
import com.creepsmash.common.messages.server.MessageMessage;
import com.creepsmash.common.messages.server.PlayersMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * The GameLobby screen.
 */
public class LobbyScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JLabel playersCountLabel;
	private JEditorPane GameInfoEditorPane;
	private ChatPane chatDialog;
	private JList playersList;
	private LobbyGamesPane gamesTabbedPane;
	private int totalPlayersInLobbyCount;
	private GameDescription joinGame;

	/**
	 * The GameLobby screen.
	 */
	public LobbyScreen() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);

		UIManager.put("ToolTip.background", Color.BLACK);
		UIManager.put("ToolTip.foreground", Color.green);

		final JButton lobbyButton = new JButton("CreepSmash.de Game Lobby");
		lobbyButton.setBounds(100, 10, 500, 30);
		lobbyButton.setBorder(null);
		lobbyButton.setForeground(Color.green);
		lobbyButton.setBackground(Color.BLACK);
		lobbyButton.setFont(new Font("Arial", Font.BOLD, 28));
		this.add(lobbyButton);
		final JLabel helpLabel = new JLabel("Press F1 for help...");
		helpLabel.setBounds(833, 0, 100, 10);
		helpLabel.setForeground(Color.green);
		helpLabel.setFont(new Font("Arial", Font.BOLD, 9));
		this.add(helpLabel);

		this.playersCountLabel = new JLabel("0 Player Online", SwingConstants.RIGHT);
		this.playersCountLabel.setBounds(640, 39, 270, 10);
		this.playersCountLabel.setForeground(Color.green);
		this.playersCountLabel.setFont(new Font("Arial", Font.BOLD, 9));

		this.GameInfoEditorPane = new JEditorPane();
		this.GameInfoEditorPane.setBounds(640, 50, 270, 200);
		this.GameInfoEditorPane.setEditable(false);
		this.GameInfoEditorPane.setFont(new Font("Arial", Font.PLAIN, 10));
		this.GameInfoEditorPane.setEditorKit(new HTMLEditorKit());
		this.GameInfoEditorPane.setAutoscrolls(true);
		this.GameInfoEditorPane.setFocusable(false);
		this.GameInfoEditorPane.setDoubleBuffered(true);
		this.GameInfoEditorPane.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(this.GameInfoEditorPane);

		this.playersList = new JList();
		this.playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.playersList.setLayoutOrientation(JList.VERTICAL);
		this.playersList.setVisibleRowCount(-1);
		this.playersList.setBackground(Color.BLACK);
		this.playersList.setForeground(Color.GREEN);

		JScrollPane playersScrollPane = new JScrollPane(this.playersList);
		playersScrollPane.setBounds(640, 254, 270, 342);
		this.add(playersScrollPane);

		this.gamesTabbedPane = new LobbyGamesPane(this);
		this.add(this.gamesTabbedPane);

		UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));
		UIManager.put("TabbedPane.background", Color.lightGray);
		UIManager.put("TabbedPane.darkShadow", Color.lightGray);
		UIManager.put("TabbedPane.focus", Color.lightGray);
		UIManager.put("TabbedPane.highlight", Color.lightGray);
		UIManager.put("TabbedPane.shadow", Color.lightGray);
		UIManager.put("TabbedPane.selected", Color.white);
		UIManager.put("TabbedPane.tabAreaBackground", Color.black);
		SwingUtilities.updateComponentTreeUI(this.gamesTabbedPane);

		this.gamesTabbedPane.setOpaque(true);

		this.chatDialog = new ChatPane();

		JScrollPane chatScrollPane = new JScrollPane(this.chatDialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setPreferredSize(new Dimension(500, 180));
		chatScrollPane.setBounds(20, 370, 600, 200);
		this.add(chatScrollPane);

		// Welcome Chat MSG
		chatDialog.sendChatText("System", "Welcome to CreepSmash Chat. :)");
		chatDialog.sendChatText("System", "Please no Flaming, Abusing or Spam.");

		final JTextField messageTextField = new JTextField();
		messageTextField.setBounds(20, 570, 550, 25);
		messageTextField.setEditable(true);
		this.setFocus(messageTextField);
		this.add(messageTextField);

		this.add(this.playersCountLabel);

		JButton createGameButton = new JButton("Create game");
		createGameButton.setBounds(20, 620, 110, 25);
		createGameButton.setBackground(Color.BLACK);
		createGameButton.setForeground(Color.GREEN);
		this.add(createGameButton);

		JButton highscoreButton = new JButton("Highscores");
		highscoreButton.setBounds(365, 620, 110, 25);
		highscoreButton.setBackground(Color.BLACK);
		highscoreButton.setForeground(Color.GREEN);
		this.add(highscoreButton);

		JButton editProfileButton = new JButton("Profile");
		editProfileButton.setBounds(480, 620, 110, 25);
		editProfileButton.setBackground(Color.BLACK);
		editProfileButton.setForeground(Color.GREEN);
		this.add(editProfileButton);

		JButton visitForumButton = new JButton("Forum");
		visitForumButton.setBounds(20, 655, 110, 25);
		visitForumButton.setBackground(Color.BLACK);
		visitForumButton.setForeground(Color.getHSBColor(255, 255, 255));
		this.add(visitForumButton);

		JButton visitWikiButton = new JButton("Wiki");
		visitWikiButton.setBounds(135, 655, 110, 25);
		visitWikiButton.setBackground(Color.BLACK);
		visitWikiButton.setForeground(Color.getHSBColor(255, 255, 255));
		this.add(visitWikiButton);

		JButton visitSiteButton = new JButton("Blog");
		visitSiteButton.setBounds(250, 655, 110, 25);
		visitSiteButton.setBackground(Color.BLACK);
		visitSiteButton.setForeground(Color.getHSBColor(255, 255, 255));
		this.add(visitSiteButton);

		JButton quitButton = new JButton("Quit");
		quitButton.setBounds(800, 620, 110, 25);
		quitButton.setBackground(Color.BLACK);
		quitButton.setForeground(Color.GREEN);
		this.add(quitButton);

		JButton chatButton = new JButton(">");
		chatButton.setBounds(570, 570, 50, 25);
		chatButton.setBackground(Color.BLACK);
		chatButton.setForeground(Color.GREEN);
		this.add(chatButton);

		createGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.pushScreen(new CreateGameScreen());
			}
		});

		createGameButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Window.pushScreen(new CreateGameScreen());
			}
		});

		highscoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.pushScreen(new HighscoreScreen());
			}
		});

		highscoreButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Window.pushScreen(new HighscoreScreen());
			}
		});

		ActionListener sendMessageAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((messageTextField.getText() != null) && (!messageTextField.getText().equals(""))) {
					if (messageTextField.getText().length() > 180) {
						LobbyScreen.this.errorDialog("Please don't enter more then 180 keystrokes.");
						return;
					} else {
						SendMessageMessage m = new SendMessageMessage();
						m.setMessage(messageTextField.getText());
						messageTextField.setText("");
						Network.sendMessage(m);
					}
				}
			}
		};
		chatButton.addActionListener(sendMessageAction);
		messageTextField.addActionListener(sendMessageAction);

		messageTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					messageTextField.postActionEvent();
				}
			}
		});

		// Quit Button
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogoutMessage gm = new LogoutMessage();
				Network.sendMessage(gm);
				Network.disconnect();
				Window.popScreen();
			}
		});

		quitButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				LogoutMessage gm = new LogoutMessage();
				Network.sendMessage(gm);
				Network.disconnect();
				Window.popScreen();
			}
		});

		editProfileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.pushScreen(new ProfileScreen());
			}
		});

		editProfileButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Window.pushScreen(new ProfileScreen());
			}
		});

		lobbyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openURL("http://www.creepsmash.de/");
			}
		});

		lobbyButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				openURL("http://www.creepsmash.de/");
			}
		});

		// Button Site
		visitSiteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				openURL("http://www.creepsmash.de/");
			}
		});

		visitSiteButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				openURL("http://www.creepsmash.de/");
			}
		});

		// Button Forum
		visitForumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openURL("http://www.creepsmash.de/forum/");
			}
		});
		visitForumButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				openURL("http://www.creepsmash.de/forum/");
			}
		});
	
		// Button WIKI
		visitWikiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openURL("http://wiki.creepsmash.de/");
			}
		});
		visitWikiButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				openURL("http://wiki.creepsmash.de/");
			}
		});

	}

	/**
	 * Open URL
	 * 
	 */
	private void openURL(String URL) {

		try {
			java.net.URI url = new java.net.URI(URL);
			Desktop.getDesktop().browse(url);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}

	/**
	 * Gets the playerlist and adds the players to the textarea.
	 * 
	 * @param p
	 *            the playersMessage
	 */
	private void setPlayersList(PlayersMessage p) {
		if (p != null) {

			totalPlayersInLobbyCount = 0;
			DefaultListModel listModel = new DefaultListModel();
			Enumeration<String> e = Collections.enumeration(new TreeSet<String>(Collections.list(p.getPlayerNames()
					.keys())));
			;

			while (e.hasMoreElements()) {
				totalPlayersInLobbyCount++;
				String key = (String) e.nextElement();
				listModel.addElement(key + " [" + p.getPlayerNames().get(key) + "] " + "");
			}

			this.playersList.setModel(listModel);
		}
	}

	/**
	 * Set the Game Info Label, NULL = Start Screen
	 * 
	 * @param txt
	 *            Set the Label info
	 * 
	 */
	public void setGameInfoEditorPaneHTML(String HTML) {
		if (HTML == null)
			HTML = "<center><br><h1>Welcome to <br><b>CreepSmash Lobby</b></h1>"
					+ "Multiplayer TowerDefence<br><br><small>Press F1 for Help...</small><br>" + "</center>";
		HTML = "<html><body text='#00FF00' bgcolor='#000000'>" + HTML + "</body></html>";
		if (!HTML.equals(this.GameInfoEditorPane.getText()))
			this.GameInfoEditorPane.setText(HTML);
	}

	/**
	 * Set the LabelGameInfo with Slected Game Info
	 * 
	 * @param selectedRow
	 * 
	 * 
	 */
	public synchronized void setGameInfoEditorPaneSelectGame(GameDescription gameDescription) {
		java.net.URL imageURL = getClass().getClassLoader().getResource(
				GameMap.getMapById(gameDescription.getMapId()).getPictureThumbnailPath());

		// Mod
		String Mod = "";
		if (gameDescription.getGameMod() == 0) {
			Mod = "<b>Normal</b>";
		} else if (gameDescription.getGameMod() == 1) {
			Mod = "<span style=\"color:red;\"><b>ALL vs ALL</b></span>";
		} else if (gameDescription.getGameMod() == 2) {
			Mod = "<span style=\"color:yellow;\"><b>Random send</b></span>";
		}

		String StateMSG = "";

		if (gameDescription.getState().compareToIgnoreCase("waiting") == 0) {
			StateMSG = "<span style=\"color:red;\"><b>" + gameDescription.getState() + "</span>";
		} else if (gameDescription.getState().compareToIgnoreCase("ended") == 0) {
			StateMSG = "<span style=\"color:yellow;\">" + gameDescription.getState() + "</span>";
		} else {
			StateMSG = gameDescription.getState();
		}

		String txt = String.format("<div align='center'><small>%s<br>(%s)</small><hr>"
				+ "<table border='0' style='border-collapse: collapse' width='270' height='102' cellpadding='3'>"
				+ "<tr><td width='122' valign='top' height='102'><center>"
				+ "<img border='1' src='%s' width='100' height='100'></center></td>"
				+ "<td valign='top' width='150' height='102'>Player: %s of %s<br>Mod: %s<br>Status: %s<br>"
				+ "Min/Max: %s / %s<br>PW: %s</td></tr></table></div>"
				+ "<hr><b>Player in Game:</b> %s %s %s %s",
				gameDescription.getGameName(), GameMap.getMapById(gameDescription.getMapId()).toString(),
				imageURL, gameDescription.getCurrentPlayers(), gameDescription.getNumberOfPlayers(),
				Mod, StateMSG, gameDescription.getMinEloPoints(), gameDescription.getMaxEloPoints(),
				gameDescription.getPasswort(), gameDescription.getPlayer1(), gameDescription.getPlayer2(),
				gameDescription.getPlayer3(), gameDescription.getPlayer4());

		this.setGameInfoEditorPaneHTML(txt);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end() {
		Network.removeListener(this);
	}

	@Override
	public void start() {
		Network.addListener(this);
		this.repaint();
		// refresh gamelist
		Network.sendMessage(new RefreshMessage());
		this.setGameInfoEditorPaneHTML(null);
	}

	public void receive(ServerMessage m) {
		if (m instanceof GamesMessage) {
			this.gamesTabbedPane.setGameList((GamesMessage) m);
			this.playersCountLabel.setText(this.gamesTabbedPane.getTotalPlayersCount()
					+ totalPlayersInLobbyCount + " player online");
		}

		if (m instanceof PlayersMessage) {
			PlayersMessage pm = (PlayersMessage) m;
			int size = this.playersList.getModel().getSize();
			ArrayList<String> alist = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				alist.add((String) playersList.getModel().getElementAt(i));
			}
			ArrayList<String> newPlayers = new ArrayList<String>();
			Enumeration<String> e = pm.getPlayerNames().keys();
			while (e.hasMoreElements()) {
				String nick = (String) e.nextElement().toString();
				nick += " [" + (String) pm.getPlayerNames().get(nick).toString() + "]";
				newPlayers.add(nick);
			}
			this.setPlayersList((PlayersMessage) m);
			this.playersCountLabel.setText(this.gamesTabbedPane.getTotalPlayersCount()
					+ totalPlayersInLobbyCount + " player online");
		}

		if (m instanceof MessageMessage) {
			MessageMessage mm = (MessageMessage) m;
			this.chatDialog.sendChatText(mm.getPlayerName(), mm.getMessage());
			SoundManager.clapSound();
		}

		if (m instanceof JoinGameResponseMessage) {
			if (((JoinGameResponseMessage) m).getResponseType().equals(IConstants.ResponseType.ok)) {
				WaitingGameScreen wgp = new WaitingGameScreen(this.joinGame, false);
				Window.pushScreen(wgp);
			} else {
				errorDialog("Cannot join the game!");
			}
		}
	}

	/**
	 * @param msg
	 *            msg
	 */
	public void errorDialog(String msg) {
		UIManager.put("OptionPane.background", Color.BLACK);
		UIManager.put("Panel.background", Color.BLACK);
		UIManager.put("OptionPane.messageForeground", Color.GREEN);
		JOptionPane.showMessageDialog(this, msg, "error", JOptionPane.ERROR_MESSAGE);
	}

	public void joinGame(GameDescription game) {
		JoinGameRequestMessage grm = new JoinGameRequestMessage();
		String pw = null;
		if (game == null) {
			Core.logger.info("Could not select the game");
			return;
		}
		if (!game.getState().equals("waiting")) {
			errorDialog("they can join only waiting game...");
			return;
		}
		if (game.getPasswort().equals("yes")) {
			UIManager.put("OptionPane.background", Color.BLACK);
			UIManager.put("Panel.background", Color.BLACK);
			UIManager.put("OptionPane.messageForeground", Color.GREEN);
			pw = JOptionPane.showInputDialog(null, "please enter a valid password for this game:");
			if (pw != null) {
				if (pw.equals(""))
					return;
			} else {
				return;
			}
		}
		grm.setGameId(game.getGameId());
		grm.setPasswort(pw);
		Network.sendMessage(grm);
		this.joinGame = game;
	}
}

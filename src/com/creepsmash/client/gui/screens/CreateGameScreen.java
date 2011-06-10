package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.gui.panels.CreateGameMapListFrame;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.GameMap;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.messages.client.CreateGameMessage;
import com.creepsmash.common.messages.server.CreateGameResponseMessage;
import com.creepsmash.common.messages.server.GameDescription;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * That JPanel class contains methods to create a new game.
 */
public class CreateGameScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JLabel playerLabel;
	private JLabel mapLabel;
	private JLabel passwortLabel;
	private JLabel MaxEloPointsLabel;
	private JLabel lGamemode;

	private JTextField tName;
	private JTextField tPasswort;
	private JComboBox tMaxEloPoints;
	private JComboBox tMinEloPoints;
	private JComboBox tPlayer;
	private JComboBox tGamemode;
	private JButton tMap;

	private JButton create;
	private JButton quit;

	private JLabel previewDescription;
	private ImageIcon preview;
	private JLabel previewLabel;
	
	private JFrame openCreateGameDialog;
	
	private int selectMap = 0;

	/**
	 * Creates a new instance of CreateGamePanel.
	 */
	public CreateGameScreen() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.openCreateGameDialog = new CreateGameMapListFrame(this, "CreepSmash - Pleace select a map...");
		this.openCreateGameDialog.setVisible(false);

		JLabel gameLabel = new JLabel("Create Game");
		gameLabel.setBounds(350, 50, 400, 30);
		gameLabel.setForeground(Color.green);
		gameLabel.setFont(new Font("Arial", Font.BOLD, 28));
		this.add(gameLabel);

		JLabel nameLabel = new JLabel("Name: ");
		nameLabel.setBounds(200, 200, 200, 30);
		nameLabel.setForeground(Color.GREEN);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		this.add(nameLabel);

		tName = new JTextField();
		tName.setText("Game of");
		tName.setBounds(300, 200, 200, 25);
		tName.setFont(new Font("Arial", Font.PLAIN, 12));
		this.setFocus(tName);

		playerLabel = new JLabel("Players: ");
		playerLabel.setBounds(200, 250, 200, 25);
		playerLabel.setForeground(Color.green);
		playerLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		tPlayer = new JComboBox();
		tPlayer.setBackground(Color.BLACK);
		tPlayer.setForeground(Color.GREEN);
		tPlayer.setBounds(300, 250, 200, 25);
		tPlayer.setFont(new Font("Arial", Font.PLAIN, 12));

		lGamemode = new JLabel("Game mode: ");
		lGamemode.setBounds(200, 450, 200, 25);
		lGamemode.setForeground(Color.green);
		lGamemode.setFont(new Font("Arial", Font.PLAIN, 12));

		tGamemode = new JComboBox();
		tGamemode.setBackground(Color.BLACK);
		tGamemode.setForeground(Color.GREEN);
		tGamemode.setBounds(300, 450, 200, 25);
		tGamemode.setFont(new Font("Arial", Font.PLAIN, 12));

		mapLabel = new JLabel("Map: ");
		mapLabel.setBounds(200, 300, 200, 25);
		mapLabel.setForeground(Color.green);
		mapLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		tMap = new JButton("Select Map");
		tMap.setBackground(Color.BLACK);
		tMap.setForeground(Color.GREEN);
		tMap.setBounds(300, 300, 200, 25);

		passwortLabel = new JLabel("Password: ");
		passwortLabel.setBounds(200, 350, 200, 25);
		passwortLabel.setForeground(Color.green);
		passwortLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		tPasswort = new JPasswordField();
		tPasswort.setBounds(300, 350, 200, 25);
		tPasswort.setFont(new Font("Arial", Font.PLAIN, 12));

		MaxEloPointsLabel = new JLabel("min/max Points: ");
		MaxEloPointsLabel.setBounds(200, 400, 200, 25);
		MaxEloPointsLabel.setForeground(Color.green);
		MaxEloPointsLabel.setFont(new Font("Arial", Font.PLAIN, 12));

		tMinEloPoints = new JComboBox();
		tMinEloPoints.setBackground(Color.BLACK);
		tMinEloPoints.setForeground(Color.GREEN);
		tMinEloPoints.setBounds(300, 400, 80, 25);
		tMinEloPoints.setFont(new Font("Arial", Font.PLAIN, 12));

		tMaxEloPoints = new JComboBox();
		tMaxEloPoints.setBackground(Color.BLACK);
		tMaxEloPoints.setForeground(Color.GREEN);
		tMaxEloPoints.setBounds(420, 400, 80, 25);
		tMaxEloPoints.setFont(new Font("Arial", Font.PLAIN, 12));

		create = new JButton("Create game");
		create.setBounds(200, 550, 200, 25);
		create.setBackground(Color.BLACK);
		create.setForeground(Color.GREEN);

		create = new JButton("Create game");
		create.setBounds(200, 550, 200, 25);
		create.setBackground(Color.BLACK);
		create.setForeground(Color.GREEN);

		quit = new JButton("Back");
		quit.setBounds(500, 550, 200, 25);
		quit.setBackground(Color.BLACK);
		quit.setForeground(Color.GREEN);

		previewDescription = new JLabel("preview");
		previewDescription.setBounds(600, 200, 250, 25);
		previewDescription.setHorizontalAlignment(SwingConstants.CENTER);
		previewDescription.setText("Random Map");
		previewDescription.setBackground(Color.BLACK);
		previewDescription.setForeground(Color.GREEN);

		String[] players = { "2 Players", "3 Players", "4 Players" };
		for (String s : players) {
			tPlayer.addItem(s);
		}
		//don't change the index
		tGamemode.addItem("Normal");
		tGamemode.addItem("All vs All");
		tGamemode.addItem("Random send");
		
		try {
			preview = new ImageIcon(ImageIO.read(this.getClass()
					.getClassLoader().getResourceAsStream(
							"com/creepsmash/client/resources/maps/zufall.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		preview.setImage(preview.getImage().getScaledInstance(250, 250,
				Image.SCALE_SMOOTH));
		previewLabel = new JLabel(preview);
		previewLabel.setBounds(600, 240, 250, 250);
		
		this.selectMap = 0;
		
		this.add(MaxEloPointsLabel);
		this.add(tName);
		this.add(passwortLabel);
		this.add(tPasswort);
		this.add(tMaxEloPoints);
		this.add(tMinEloPoints);
		this.add(tName);
		this.add(playerLabel);
		this.add(tPlayer);
		this.add(mapLabel);
		this.add(tMap);
		this.add(create);
		this.add(quit);
		this.add(previewDescription);
		this.add(previewLabel);
		this.add(lGamemode);
		this.add(tGamemode);

		ActionListener a3 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openCreateGameDialog();
			}
		};
		
		this.tMap.addActionListener(a3);

		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.popScreen();
				if (openCreateGameDialog.isVisible()) openCreateGameDialog.dispose();
			}
		};
		quit.addActionListener(a1);

		quit.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Window.popScreen();
				if (openCreateGameDialog.isVisible()) openCreateGameDialog.dispose();
			}
		});

		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				StartButton();
			}
		};
		create.addActionListener(a2);

		KeyAdapter createKeyAdapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() != KeyEvent.VK_ENTER) {
					return;
				}

				StartButton();
			}
		};

		create.addKeyListener(createKeyAdapter);
		tName.addKeyListener(createKeyAdapter);
		tPlayer.addKeyListener(createKeyAdapter);
		tMap.addKeyListener(createKeyAdapter);
		
	}

	private void openCreateGameDialog(){
		openCreateGameDialog.setVisible(true);
		openCreateGameDialog.requestFocus();
	}

	private void StartButton() {

		boolean StartGame = false;

		if (tName.getText().length() != 0) {
			StartGame = true;
		} else {
			errorDialog("Please enter a name for the game!");
			StartGame = false;
		}
		if (tName.getText().length() > 12) {
			errorDialog("Maximum length of Gamename is 12");
			StartGame = false;
		}
		if (tMaxEloPoints.getSelectedIndex() == 0)
			tMaxEloPoints.setSelectedItem(0);
		if (tMinEloPoints.getSelectedIndex() == 0)
			tMinEloPoints.setSelectedItem(0);

		if (StartGame == true
				&& (tMaxEloPoints.getSelectedIndex() != 0 || tMinEloPoints
						.getSelectedIndex() != 0)) {

			if ((tMaxEloPoints.getSelectedIndex() < tMinEloPoints
					.getSelectedIndex() && tMaxEloPoints.getSelectedIndex() != 0)
					|| (tMaxEloPoints.getSelectedIndex() == tMinEloPoints
							.getSelectedIndex() && tMaxEloPoints
							.getSelectedIndex() != 0)) {

				errorDialog("'Max Elo Points' must be larger than 'min Elo Points'.!");
				StartGame = false;

			} else {
				StartGame = true;
			}
		}
		if (tGamemode.getSelectedIndex() == 1 || tGamemode.getSelectedIndex() == 2){
			if (tPlayer.getSelectedIndex() == 0){
				errorDialog("This game mod works only with 3 ore 4 players!");
				StartGame = false;
			}
		}
		if (StartGame == true) {
			if (openCreateGameDialog.isVisible()) openCreateGameDialog.dispose();
			sendCreateGameMessage();
		}
	}

	/**
	 * Sends the Message to create a GameMessage.
	 */
	private void sendCreateGameMessage() {

		CreateGameMessage gM = new CreateGameMessage();
		gM.setGameName(tName.getText() + " (" + Core.getPlayerName() + ")");
		gM.setMapId(this.selectMap);
		gM.setMaxPlayers(tPlayer.getSelectedIndex() + 2);
		gM.setMaxEloPoints(Integer
				.valueOf(tMaxEloPoints.getSelectedIndex() * 100));
		gM.setMinEloPoints(Integer
				.valueOf(tMinEloPoints.getSelectedIndex() * 100));
		gM.setPasswort(tPasswort.getText());
		gM.setGameMode(tGamemode.getSelectedIndex());

		Network.sendMessage(gM);
	}

	/**
	 * 
	 * @param g
	 *            Creates a new Game
	 */
	private void createGame(CreateGameResponseMessage g) {
		if (g.getResponseType().equals(IConstants.ResponseType.failed)) {
			errorDialog("Game already exists!");

		} else if (g.getResponseType().equals(IConstants.ResponseType.ok)) {
			GameDescription gd = new GameDescription();
			gd.setNumberOfPlayers(tPlayer.getSelectedIndex() + 2);
			gd.setMapId(this.selectMap);
			gd.setGameName(tName.getText());
			gd.setMaxEloPoints(Integer
					.valueOf(tMaxEloPoints.getSelectedIndex()) * 100);
			gd.setMinEloPoints(Integer
					.valueOf(tMinEloPoints.getSelectedIndex()) * 100);
			gd.setGameMod(tGamemode.getSelectedIndex());
			gd.setPasswort(tPasswort.getText());

			WaitingGameScreen wgp = new WaitingGameScreen(gd, true);
			Window.switchScreen(wgp);
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
		JOptionPane.showMessageDialog(this, msg, "error",
				JOptionPane.ERROR_MESSAGE);
	}

	public void selectMap(int id) {
		
		this.selectMap = id;
		
		try {
			preview = new ImageIcon(ImageIO.read(this.getClass()
					.getClassLoader().getResourceAsStream(
							GameMap.getMapById(id).getPicturePath())));
			previewDescription.setText("Preview "
					+ GameMap.getMapById(id).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	preview.setImage(preview.getImage().getScaledInstance(250, 250,
			Image.SCALE_SMOOTH));
	previewLabel.setIcon(preview);
	
	openCreateGameDialog.setVisible(false);

	}

	/**
	 * displays the screen.
	 */
	@Override
	public void start() {
		Network.addListener(this);

		int playerScore = 0;

		tMinEloPoints.addItem("all");
		tMaxEloPoints.addItem("all");

		for (int faktor = 100; faktor <= playerScore; faktor += 100) {

			tMinEloPoints.addItem(faktor);

		}

		if (playerScore == 0)
			playerScore = 5;
		for (int faktor = 100; faktor <= 10000; faktor += 100) {

			tMaxEloPoints.addItem(faktor);

		}

		this.repaint();
		
		

	}

	/**
	 * method for disappearing the screen.
	 */
	@Override
	public void end() {
		Network.removeListener(this);
	}

	/**
	 * method for update.
	 * 
	 * @param m
	 *            for ServerMessage
	 */
	public void receive(ServerMessage m) {
		if (m instanceof CreateGameResponseMessage) {
			createGame((CreateGameResponseMessage) m);
		}
	}

}

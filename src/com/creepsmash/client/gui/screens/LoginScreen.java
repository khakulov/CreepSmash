package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.creepsmash.client.Core;
import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.Version;
import com.creepsmash.common.messages.client.LoginRequestMessage;
import com.creepsmash.common.messages.server.LoginResponseMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * LoginPanel at the beginning of the game.
 */
public class LoginScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JButton loginButton;
	private String username = null;

	/**
	 * constructor for LoginPanel.
	 */
	public LoginScreen() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);

		JLabel loginLabel = new JLabel("Login");
		loginLabel.setBounds(350, 50, 400, 30);
		loginLabel.setForeground(Color.green);
		loginLabel.setFont(new Font("Arial", Font.BOLD, 28));
		this.add(loginLabel);

		JLabel nameLabel = new JLabel("Username: ");
		nameLabel.setBounds(200, 200, 200, 30);
		nameLabel.setForeground(Color.GREEN);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		this.add(nameLabel);

		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(200, 300, 200, 25);
		passwordLabel.setForeground(Color.green);
		passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		this.add(passwordLabel);

		this.usernameTextField = new JTextField();
		this.usernameTextField.setBounds(400, 200, 200, 25);
		this.usernameTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		this.add(this.usernameTextField);

		this.passwordTextField = new JPasswordField();
		this.passwordTextField.setBounds(400, 300, 200, 25);
		this.passwordTextField.setEchoChar('*');
		this.passwordTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		this.add(this.passwordTextField);

		this.loginButton = new JButton("Login");
		this.loginButton.setBounds(120, 550, 200, 25);
		this.loginButton.setBackground(Color.BLACK);
		this.loginButton.setForeground(Color.GREEN);
		this.add(this.loginButton);

		JButton registerButton = new JButton("Register");
		registerButton.setBounds(360, 550, 200, 25);
		registerButton.setBackground(Color.BLACK);
		registerButton.setForeground(Color.GREEN);
		this.add(registerButton);

		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(600, 550, 200, 25);
		exitButton.setBackground(Color.BLACK);
		exitButton.setForeground(Color.GREEN);
		this.add(exitButton);

		this.loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginProcess();
			}
		});

		KeyAdapter loginKeyAdapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				loginProcess();
			}
		};
		this.usernameTextField.addKeyListener(loginKeyAdapter);
		this.passwordTextField.addKeyListener(loginKeyAdapter);
		this.loginButton.addKeyListener(loginKeyAdapter);

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.pushScreen(new RegisterScreen());
			}
		});

		registerButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				Window.pushScreen(new RegisterScreen());
			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		exitButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				System.exit(0);
			}
		});
	}

	@Override
	public void end() {
		Network.removeListener(this);
	}

	@Override
	public void start() {
		Network.addListener(this);
		this.loginButton.setEnabled(true);
		this.passwordTextField.setText("");
		if (this.username != null)
			this.usernameTextField.setText(Core.getPlayerName());
		this.setFocus(this.usernameTextField);
	}

	public void receive(ServerMessage m) {
		if (m instanceof LoginResponseMessage) {
			LoginResponseMessage response = (LoginResponseMessage) m;
			if (response.getResponseType() == IConstants.ResponseType.ok) {
				Core.setPlayerName(this.username);
				Window.pushScreen(new LobbyScreen());
			} else if (response.getResponseType() == IConstants.ResponseType.version) {
				errorDialog("Wrong version \u2013 please download the latest version." + "\n\n"
						+ "If that doesn't work, you may need to clear the Java WebStart cache.");
				loginButton.setEnabled(true);
			} else {
				errorDialog("Login failed");
				loginButton.setEnabled(true);
			}
		}

	}

	/**
	 * Dialog to show errors in the same colours than GUI.
	 * 
	 * @param msg
	 *            msg
	 */
	public void errorDialog(String msg) {
		UIManager.put("OptionPane.background", Color.BLACK);
		UIManager.put("OptionPane.JButton.setForground", Color.BLACK);
		UIManager.put("Panel.background", Color.BLACK);
		UIManager.put("OptionPane.messageForeground", Color.GREEN);
		JOptionPane.showMessageDialog(this, msg, "login error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * login.
	 */
	public void loginProcess() {
		try {
			Network.connect();
		} catch (UnknownHostException e) {
			errorDialog("Couldn't find specified host!");
		} catch (IOException e) {
			errorDialog("Couldn't connect to host, please check your internet connection!");
		}
		if (this.usernameTextField.getText().length() == 0
				|| String.valueOf(this.passwordTextField.getPassword()).length() == 0) {
			errorDialog("Login failed");
			return;
		}
		this.username = this.usernameTextField.getText();
		LoginRequestMessage loginMessage = new LoginRequestMessage();
		loginMessage.setVersion(Version.getVersion());
		loginMessage.setUsername(this.username);
		loginMessage.setPassword(String.valueOf(this.passwordTextField.getPassword()));
		loginMessage.setMacaddress(Network.getMACAddress());
		Network.sendMessage(loginMessage);
		loginButton.setEnabled(false);
	}
}

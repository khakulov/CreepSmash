package com.creepsmash.client.gui.screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.creepsmash.client.gui.Screen;
import com.creepsmash.client.gui.Window;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.IConstants;
import com.creepsmash.common.Version;
import com.creepsmash.common.messages.client.LoginRequestMessage;
import com.creepsmash.common.messages.client.RegistrationRequestMessage;
import com.creepsmash.common.messages.server.RegistrationResponseMessage;
import com.creepsmash.common.messages.server.ServerMessage;


/**
 * RegisterPanel to register users in the database.
 */
public class RegisterScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JLabel name;
	private JLabel password;
	private JLabel info2;
	private JLabel registering;
	private JLabel password2;
	private JLabel email;

	private JTextField lName;
	private JTextField lEmail;
	private JPasswordField lPassword;
	private JPasswordField lPassword2;

	private JButton register;
	private JButton back;
	
	private String[] dictionary = 
			{"arsch", "nigga", "hitler", "fucker", "sex", "penis", "vagina",
			"suck", "hoden", "eichel", "fuck", "schlampe", "hure", "flittchen",
			"bitch", "slut", "nigga", "neger", "negar", "porno", "p0rno", "porn0",
			"p0rn0", "muschi", "titte", "boobs", "busen", "mudda", "mutter",
			"mother", "vadda", "vater", "father", "pussy"};

	/**
	 * constructor to initialize elements.
	 */
	public RegisterScreen() {

		this.setLayout(null);
		this.setBackground(Color.BLACK);

		registering = new JLabel("Register");
		registering.setBounds(350, 50, 400, 30);
		registering.setForeground(Color.green);
		registering.setFont(new Font("Arial", Font.BOLD, 28));

		email = new JLabel("Email: ");
		email.setBounds(200, 350, 200, 30);
		email.setForeground(Color.GREEN);
		email.setFont(new Font("Arial", Font.PLAIN, 12));

		name = new JLabel("*Username: ");
		name.setBounds(200, 200, 200, 30);
		name.setForeground(Color.GREEN);
		name.setFont(new Font("Arial", Font.PLAIN, 12));

		password = new JLabel("*Password: ");
		password.setBounds(200, 250, 200, 25);
		password.setForeground(Color.green);
		password.setFont(new Font("Arial", Font.PLAIN, 12));

		password2 = new JLabel("*Password: ");
		password2.setBounds(200, 300, 200, 25);
		password2.setForeground(Color.green);
		password2.setFont(new Font("Arial", Font.PLAIN, 12));

		info2 = new JLabel("*Fields with \" * \" mustn't be empty");
		info2.setBounds(200, 400, 200, 25);
		info2.setForeground(Color.green);
		info2.setFont(new Font("Arial", Font.PLAIN, 11));

		lName = new JTextField();
		lName.setBounds(400, 200, 200, 25);
		lName.setFont(new Font("Arial", Font.PLAIN, 12));
		this.setFocus(lName);

		lEmail = new JTextField();
		lEmail.setBounds(400, 350, 200, 25);
		lEmail.setFont(new Font("Arial", Font.PLAIN, 12));

		lPassword = new JPasswordField(20);
		lPassword.setBounds(400, 250, 200, 25);
		lPassword.setEchoChar('*');
		lPassword.setFont(new Font("Arial", Font.PLAIN, 12));

		lPassword2 = new JPasswordField(20);
		lPassword2.setBounds(400, 300, 200, 25);
		lPassword2.setEchoChar('*');
		lPassword2.setFont(new Font("Arial", Font.PLAIN, 12));

		register = new JButton("Register");
		register.setBounds(200, 550, 200, 25);
		register.setBackground(Color.BLACK);
		register.setForeground(Color.GREEN);

		back = new JButton("Back");
		back.setBounds(500, 550, 200, 25);
		back.setBackground(Color.BLACK);
		back.setForeground(Color.GREEN);

		this.add(password);
		this.add(name);
		this.add(lPassword);
		this.add(lName);
		this.add(info2);
		this.add(register);
		this.add(lPassword2);
		this.add(password2);
		this.add(registering);
		this.add(lEmail);
		this.add(email);
		this.add(back);

		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrationProcess();
			}
		};
		register.addActionListener(a1);
		
		KeyAdapter registerKeyAdapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) {
					return;
				}
				registrationProcess();
			}	
		};
		lName.addKeyListener(registerKeyAdapter);
		lPassword.addKeyListener(registerKeyAdapter);
		lPassword2.addKeyListener(registerKeyAdapter);
		lEmail.addKeyListener(registerKeyAdapter);
		register.addKeyListener(registerKeyAdapter);

		ActionListener a3 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Window.popScreen();
			}
		};
		back.addActionListener(a3);

		back.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				Window.popScreen();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end() {
		Network.removeListener(this);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		Network.addListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void receive(ServerMessage m) {

		if (m instanceof RegistrationResponseMessage) {
			RegistrationResponseMessage response 
			= (RegistrationResponseMessage) m;
			if (response.getResponseType() 
					== IConstants.ResponseType.username) {

				errorDialog("Username already exists!");
				register.setEnabled(true);
				lName.requestFocus();
			}
			if (response.getResponseType() == IConstants.ResponseType.ok) {
				UIManager.put("OptionPane.background", Color.BLACK);
				UIManager.put("Panel.background", Color.BLACK);
				UIManager.put("OptionPane.messageForeground", Color.GREEN);
				JOptionPane.showMessageDialog(this, "Registration Successful",
						"Thank You", 2);
				Window.popScreen();
				// login
				LoginRequestMessage loginMessage = new LoginRequestMessage();
				loginMessage.setVersion(Version.getVersion());
				loginMessage.setUsername(lName.getText());
				loginMessage.setPassword(String
						.valueOf(lPassword.getPassword()));
				loginMessage.setMacaddress(Network.getMACAddress());
				Network.sendMessage(loginMessage);
			}
			if (response.getResponseType() == IConstants.ResponseType.failed) {
				errorDialog("Unknown error");
				register.setEnabled(true);
				lName.requestFocus();
			}

		}

	}
	
	/**
	 * process which sends registrationrequest to server.
	 */
	public void registrationProcess() {
		try {
			Network.connect();
		} catch (UnknownHostException e) {
			errorDialog("Couldn't find specified host!");
		} catch (IOException e) {
			errorDialog("Couldn't connect to host, please check your internet connection!");
		}
		RegistrationRequestMessage request = new RegistrationRequestMessage();

		Pattern pWord = Pattern.compile("[a-zA-Z_0-9]+");
		Pattern pEmail = Pattern.compile("^\\S+@\\S+$");
		Matcher mName = pWord.matcher(lName.getText());
		Matcher mPassword = pWord.matcher(String.valueOf(lPassword
				.getPassword()));
		Matcher mEmail = pEmail.matcher(lEmail.getText());
		boolean pMatchesName = mName.matches();
		boolean pMatchesPwd = mPassword.matches();
		boolean pMatchesEmail = mEmail.matches();

		if (!this.isInDictionary(lName.getText().toLowerCase())) {
			if (lName.getText().equals("")
					|| String.valueOf(lPassword.getPassword()).equals("")
					|| String.valueOf(lPassword2.getPassword()).equals("")) {
				errorDialog("fields marked with * have to be filled out");
				lName.requestFocus();
			} else if (lName.getText().length() > 12) {
				errorDialog("Maximum length of Username is 12");
				lName.requestFocus();
			}else if (!pMatchesPwd) {
				errorDialog("only a-zA-Z and 0-9 is allowed");
				lPassword.requestFocus();
			} else if (!String.valueOf(lPassword.getPassword()).equals(
					String.valueOf(lPassword2.getPassword()))) {
				errorDialog("The passwords you entered weren't identical");
				lPassword.requestFocus();
			} else if (!pMatchesName) {
				errorDialog("only a-zA-Z and 0-9 is allowed");
				lName.requestFocus();
			} else if (!pMatchesEmail && !lEmail.getText().equals("")) {
				errorDialog("Not a valid email address!");
				lEmail.requestFocus();
			} else {
	
				request.setPassword(String.valueOf(lPassword.getPassword()));
				request.setEmail(lEmail.getText());
				request.setUsername(lName.getText());
				Network.sendMessage(request);
				register.setEnabled(false);
			}
		} else {
			errorDialog("Your Nickname includes prohibited Words!");
			lName.requestFocus();
		}
	}
	
	private boolean isInDictionary(String word) {
		if (this.dictionary.length > 0) {
			for (String s : this.dictionary) {
				if (word.indexOf(s, 0) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Dialog to show errors in the same colors than GUI.
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

}


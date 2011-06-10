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
import com.creepsmash.common.messages.client.DeleteRequestMessage;
import com.creepsmash.common.messages.client.LogoutMessage;
import com.creepsmash.common.messages.client.UpdateDataRequestMessage;
import com.creepsmash.common.messages.server.DeleteResponseMessage;
import com.creepsmash.common.messages.server.ServerMessage;
import com.creepsmash.common.messages.server.UpdateDataResponseMessage;


public class ProfileScreen extends Screen implements MessageListener {

	private static final long serialVersionUID = 1L;

	private JLabel name;
	private JLabel password;
	private JLabel titleProfil;
	private JLabel password2;
	private JLabel email;
	private JLabel oldPassword;

	private JTextField lName;
	private JTextField lEmail;
	private JPasswordField lPassword;
	private JPasswordField lPassword2;
	private JPasswordField loldPassword;

	private JButton confirm;
	private JButton back;
	private JButton delete;

	/**
	 * constructor to initialize elements.
	 */
	public ProfileScreen() {

		this.setLayout(null);
		this.setBackground(Color.BLACK);

		titleProfil = new JLabel("Profile");
		titleProfil.setBounds(350, 50, 400, 30);
		titleProfil.setForeground(Color.green);
		titleProfil.setFont(new Font("Arial", Font.BOLD, 28));

		email = new JLabel("New Email: ");
		email.setBounds(200, 350, 200, 30);
		email.setForeground(Color.GREEN);
		email.setFont(new Font("Arial", Font.PLAIN, 12));

		name = new JLabel("Username: ");
		name.setBounds(200, 150, 200, 30);
		name.setForeground(Color.GREEN);
		name.setFont(new Font("Arial", Font.PLAIN, 12));

		password = new JLabel("New Password: ");
		password.setBounds(200, 250, 200, 25);
		password.setForeground(Color.green);
		password.setFont(new Font("Arial", Font.PLAIN, 12));

		password2 = new JLabel("Confirm Password: ");
		password2.setBounds(200, 300, 200, 25);
		password2.setForeground(Color.green);
		password2.setFont(new Font("Arial", Font.PLAIN, 12));

		oldPassword = new JLabel("Actual Password: ");
		oldPassword.setBounds(200, 200, 200, 25);
		oldPassword.setForeground(Color.green);
		oldPassword.setFont(new Font("Arial", Font.PLAIN, 12));

		lName = new JTextField();
		lName.setBounds(400, 150, 200, 25);
		lName.setFont(new Font("Arial", Font.PLAIN, 12));
		lName.setEditable(false);

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

		loldPassword = new JPasswordField(20);
		loldPassword.setBounds(400, 200, 200, 25);
		loldPassword.setEchoChar('*');
		loldPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		this.setFocus(loldPassword);

		confirm = new JButton("Confirm");
		confirm.setBounds(200, 550, 200, 25);
		confirm.setBackground(Color.BLACK);
		confirm.setForeground(Color.GREEN);

		back = new JButton("Back");
		back.setBounds(500, 550, 200, 25);
		back.setBackground(Color.BLACK);
		back.setForeground(Color.GREEN);

		delete = new JButton("Delete Account");
		delete.setBounds(350, 600, 200, 25);
		delete.setBackground(Color.BLACK);
		delete.setForeground(Color.GREEN);

		this.add(password);
		this.add(name);
		this.add(lPassword);
		this.add(loldPassword);
		this.add(oldPassword);
		this.add(lName);
		this.add(confirm);
		this.add(lPassword2);
		this.add(password2);
		this.add(titleProfil);
		this.add(lEmail);
		this.add(email);
		this.add(back);
		this.add(delete);

		ActionListener a1 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmProcess();
			}
		};
		confirm.addActionListener(a1);

		KeyAdapter confirmKeyAdapter = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) {
					return;
				}
				confirmProcess();
			}
		};
		lPassword.addKeyListener(confirmKeyAdapter);
		lPassword2.addKeyListener(confirmKeyAdapter);
		loldPassword.addKeyListener(confirmKeyAdapter);
		lEmail.addKeyListener(confirmKeyAdapter);
		confirm.addKeyListener(confirmKeyAdapter);

		ActionListener a2 = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteProcess();
			}
		};
		delete.addActionListener(a2);

		delete.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				deleteProcess();
			}
		});

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

	@Override
	public void end() {
		Network.removeListener(this);

	}

	@Override
	public void start() {
		Network.addListener(this);
	}

	public void receive(ServerMessage m) {

		if (m instanceof UpdateDataResponseMessage) {
			UpdateDataResponseMessage udResponsem = (UpdateDataResponseMessage) m;

			if (udResponsem.getResponseType() == IConstants.ResponseType.ok) {
				UIManager.put("OptionPane.background", Color.BLACK);
				UIManager.put("Panel.background", Color.BLACK);
				UIManager.put("OptionPane.messageForeground", Color.GREEN);
				JOptionPane.showMessageDialog(this, "Change Successful",
						"Thank You", 2);
				Window.popScreen();
			}
			if (udResponsem.getResponseType() == IConstants.ResponseType.failed) {
				errorDialog("Unknown error");
				confirm.setEnabled(true);
				lPassword.requestFocus();
			}

		}

		if (m instanceof DeleteResponseMessage) {
			DeleteResponseMessage drm = (DeleteResponseMessage) m;
			if (drm.getResponseType() == IConstants.ResponseType.ok) {
				LogoutMessage gm = new LogoutMessage();
				Network.sendMessage(gm);
				Network.disconnect();
				Window.clearScreen();
				Window.pushScreen(new LoginScreen());
			}
		}

	}

	/**
	 * process which sends changerequest to server.
	 */
	public void confirmProcess() {
		try {
			Network.connect();
		} catch (UnknownHostException e) {
			errorDialog("Couldn't find specified host!");
		} catch (IOException e) {
			errorDialog("Couldn't connect to host, please check your internet connection!");
		}
		UpdateDataRequestMessage udrm = new UpdateDataRequestMessage();

		Pattern pWord = Pattern.compile("[a-zA-Z_0-9]*");
		Pattern pEmail = Pattern.compile("^\\S+@\\S+$");
		Matcher mPassword = pWord.matcher(String.valueOf(lPassword
				.getPassword()));
		Matcher mEmail = pEmail.matcher(lEmail.getText());
		boolean pMatchesPwd = mPassword.matches();
		boolean pMatchesEmail = mEmail.matches();

		if (!pMatchesPwd) {
			errorDialog("only a-zA-Z and 0-9 is allowed");
			lPassword.requestFocus();
		} else if (!String.valueOf(lPassword.getPassword()).equals(
				String.valueOf(lPassword2.getPassword()))) {
			errorDialog("The passwords you entered weren't identical");
			lPassword.requestFocus();
		} else if (!pMatchesEmail && !lEmail.getText().equals("")) {
			errorDialog("Not a valid email address!");
			lEmail.requestFocus();
		} else if (String.valueOf(loldPassword.getPassword()).equals(String.valueOf(lPassword.getPassword()))) {
			errorDialog("Your actual password and your "
					+ "new password are the same!");
			lEmail.requestFocus();

		} else {
			if (lEmail.getText() == null) {
				lEmail.setText("");
				udrm.setEmail(lEmail.getText());
			} else {
				udrm.setEmail(lEmail.getText());
			}
			if (String.valueOf(lPassword.getPassword()) == null) {
				udrm.setPassword("");
			} else {
				udrm.setPassword(String.valueOf(lPassword.getPassword()));
			}
			if (String.valueOf(loldPassword.getPassword()) == null) {
				udrm.setOldPassword("");
			} else {
				udrm.setOldPassword(String.valueOf(loldPassword.getPassword()));
			}
			Network.sendMessage(udrm);
			confirm.setEnabled(false);

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
	/**
	 * delete the process.
	 * 
	 */
	public void deleteProcess() {

		String[] options = { "Yes", "No" };
		UIManager.put("OptionPane.background", Color.BLACK);
		UIManager.put("Panel.background", Color.BLACK);
		UIManager.put("OptionPane.messageForeground", Color.GREEN);
		int n = JOptionPane.showOptionDialog(this, "Are you sure?",
				"Account Deletion", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (n == JOptionPane.YES_OPTION) {
			DeleteRequestMessage drm = new DeleteRequestMessage();
			Network.sendMessage(drm);
		}
		System.out.println(n);

	}
}

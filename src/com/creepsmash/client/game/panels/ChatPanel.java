package com.creepsmash.client.game.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.gui.panels.ChatPane;
import com.creepsmash.client.network.Network;
import com.creepsmash.client.utils.SoundManager;
import com.creepsmash.common.messages.client.SendMessageMessage;


/**
 * ChatPanel to send and receive messages
 * to the other players in the game.
 */

public class ChatPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField message;
	private ChatPane chatdialog;
	
	private Context context;
	private JScrollPane jScrollPanejchatdialog = null;


	/**
	 * 
	 */
	public ChatPanel() {
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(233, 255));
		this.setSize(233, 255);
		this.setBounds(700, 445, 233, 255);
		this.setLayout(null);

		this.chatdialog = new ChatPane();
		this.chatdialog.setShowDatum(false);
		this.jScrollPanejchatdialog = new JScrollPane(
				this.chatdialog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPanejchatdialog.setBounds(256, 302, 417, 278);
		this.setSize(233, 230);
		this.jScrollPanejchatdialog.setBounds(0, 0, 233, 230);
		

		this.message = new JTextField();
		this.message.setEditable(true);
		this.message.setFont(new Font("Arial", 0, 16));
		this.message.setBounds(0, 230, 233, 25);

		this.add(jScrollPanejchatdialog);
		this.add(message);

		this.message.requestFocus(); // cursor on the message-textfield

		message.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				sendText(evt);
			}
		});
		
		setVisible(true);

	}

	/**
	 * 
	 * 
	 * @param evt
	 *            gets ActionEvent
	 */

	public void sendText(final KeyEvent evt) {

		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			// send Message
			if (context != null) {
				SendMessageMessage chatMsg = new SendMessageMessage();
				chatMsg.setClientId(context.getPlayerId());
				chatMsg.setMessage(getText());
				Network.sendMessage(chatMsg);
				this.message.setText("");
			}

		}

	}

	/**
	 * getter for textinput.
	 * 
	 * @return message
	 */
	public final String getText() {
		return this.message.getText();
	}

	/**
	 * 
	 * @param nickname
	 *            of player
	 * @param newMessage
	 *            message of player
	 */
	public final void setMessage(String nickname, String newMessage) {
		this.chatdialog.sendChatText(nickname, newMessage);
		SoundManager.clapSound();
	}

	/**
	 * Setter to set Context.
	 * 
	 * @param context
	 *            to specify which context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	public JTextField getMessageField() {
		return message;
	}
}

package com.creepsmash.client.gui.panels;

import com.creepsmash.client.network.Network;
import com.creepsmash.common.messages.client.SendMessageMessage;
import com.creepsmash.common.messages.client.StartGameRequestMessage;


/**
 * Countdown when starting game.
 */
public class WaitingGameCountdownThread extends Thread {
	public void run() {
		SendMessageMessage m = new SendMessageMessage();

		m.setMessage("GAME START COUNTDOWN 3");
		Network.sendMessage(m);

		for (int i = 2; i > 0; i--) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m.setMessage("in " + i);
			Network.sendMessage(m);
		}
		Network.sendMessage(new StartGameRequestMessage());
	}
}

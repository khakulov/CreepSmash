package com.creepsmash.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.creepsmash.client.Core;
import com.creepsmash.common.messages.client.PongMessage;
import com.creepsmash.common.messages.server.GameMessage;
import com.creepsmash.common.messages.server.PingMessage;
import com.creepsmash.common.messages.server.ServerMessage;



/**
 * Watches for incomming messages form the Server.
 */
public class InThread extends Thread {
	private BufferedReader bufferedReader;
	private volatile boolean terminate;

	/**
	 * Creates a new instance of ClientWatcher.
	 * 
	 * @param n
	 *            Client
	 * @param in
	 *            BufferedReader
	 */
	public InThread(InputStream inputStream) {
		super();
		this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
		this.setName("InThread");
	}

	/**
	 * run method.
	 */
	public void run() {
		this.terminate = false;
		while (!this.terminate) {
			try {
				String messageString = this.bufferedReader.readLine();
				//Core.logger.info("RECEIVED: " + messageString);
				ServerMessage message = ServerMessage.renderMessageString(messageString);

				if (message == null) {
					Core.logger.warning("received invalid message " + messageString);
				} else if (message instanceof GameMessage) {
					Network.addGameMessage((GameMessage) message);
				} else if (message instanceof PingMessage) {
					Network.sendMessage(new PongMessage());
				} else {
					Network.notifyListeners(message);
				}

			} catch (IOException e) {
				Network.disconnect();
				Core.logger.warning(e.getMessage());
			}
		}
	}

	/**
	 * Stops reading from the server.
	 */
	public synchronized void terminate() {
		this.terminate = true;
		this.interrupt();
	}
}

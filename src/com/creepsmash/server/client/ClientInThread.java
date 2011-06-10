package com.creepsmash.server.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;


import org.apache.log4j.Logger;

import com.creepsmash.common.Permission;
import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.common.messages.client.InvalidMessage;
import com.creepsmash.common.messages.client.PongMessage;
import com.creepsmash.common.messages.server.PingMessage;

/**
 * Thread for process incoming messages from clients socket
 */
public class ClientInThread extends Thread {
	private static Logger logger = Logger.getLogger(ClientInThread.class
			.getName());
	private BufferedReader bufferedReader;
	private Client client;
	private volatile boolean terminate = false;
	private int inactivityCount = 0;

	/**
	 * Create Thread for Clients input stream.
	 * 
	 * @param inputStream
	 *            Input stream from clients socket
	 * @param client
	 *            Client object
	 */
	public ClientInThread(InputStream inputStream, Client client) {
		super();
		this.bufferedReader = new BufferedReader(new InputStreamReader(
				inputStream));
		this.client = client;

		this.setName("Client " + client.getClientID() + ": InThread");
	}

	/**
	 * Start the thread.
	 */
	@Override
	public void run() {
		boolean timeout = false;
		while (!this.terminate) {
			try {
				String messageString = this.bufferedReader.readLine();
				ClientMessage message = ClientMessage
						.renderMessageString(messageString);
				if (message instanceof InvalidMessage) {
					logger.warn("Client " + this.client.getClientID() + ": "
							+ "received invalid message " + messageString);
					continue;
				}
				if (message instanceof PongMessage) {
					// logger.info("Client " + this.client.getClientID() +
					// ": timeout -- received PONG");
					timeout = false;
					continue;
				}
				this.inactivityCount = 0;
				this.client.receive(message);
			} catch (SocketException e) {
				logger.warn("Client " + this.client.getClientID()
						+ ": disconnected... SocketException");
				this.client.disconnect();
			} catch (NullPointerException e) {
				logger.warn("Client " + this.client.getClientID()
						+ ": disconnected... NullPointerException");
				this.client.disconnect();
			} catch (SocketTimeoutException e) {
				if (this.inactivityCount > 60) {
					// Wenn der Client mehr als halbe Stunde nichts anders als
					// Pingpong spielt...
					// 30sek * 60 = 0.5 h
					logger.warn("Client " + this.client.getClientID()
							+ ": more than half our inactive");
					if ((this.client.getPlayerModel() == null)
							|| (!this.client.getPlayerModel().hasPermission(
									Permission.NO_TIMEOUT))) {
						this.client.disconnect();
					}
				}
				if (!timeout) {
					// logger.info("Client " + this.client.getClientID() +
					// ": timeout -- sending PING");
					timeout = true;
					this.client.send(new PingMessage());
					this.inactivityCount++;
					continue;
				}
				logger.warn("Client " + this.client.getClientID()
						+ ": timeout -- got no response to PING");
				this.client.disconnect();
			} catch (IOException e) {
				logger.warn("IO-error: " + e);
			}
		}
	}

	/**
	 * Terminate the thread.
	 */
	public synchronized void terminate() {
		this.terminate = true;
		this.interrupt();
	}
}
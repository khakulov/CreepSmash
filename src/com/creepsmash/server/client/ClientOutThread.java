package com.creepsmash.server.client;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.creepsmash.common.messages.server.ServerMessage;


public class ClientOutThread extends Thread {
	private static Logger logger = Logger.getLogger(ClientOutThread.class.getName());
	private PrintWriter printWriter;
	private Client client;
	private volatile boolean terminate = false;
	private BlockingQueue<ServerMessage> queue;

	public ClientOutThread(OutputStream outputStream, Client client) {
		super();
		this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")),true);
		this.client = client;
		this.queue = new LinkedBlockingQueue<ServerMessage>();

		this.setName("Client " + client.getClientID() + ": OutThread");
	}

	@Override
	public void run() {
		while (!this.terminate) {
			try {
				ServerMessage message = queue.take();
				if (message == null) {
					logger.warn("Client " + this.client.getClientID() + ": disconnected... null");
					this.client.disconnect();
				} else {
					logger.debug(message.toString());
					this.printWriter.println(message.toString());
					this.printWriter.flush();
				}
			} catch (InterruptedException e) {
				//do nothing
			}
		}
	}
	
	public void send(ServerMessage message) {
		this.queue.add(message);
	}

	public synchronized void terminate() {
		this.terminate = true;
		this.interrupt();
	}
}


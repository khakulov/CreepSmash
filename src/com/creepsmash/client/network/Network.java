package com.creepsmash.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.creepsmash.client.Core;
import com.creepsmash.common.messages.client.ClientMessage;
import com.creepsmash.common.messages.server.GameMessage;
import com.creepsmash.common.messages.server.ServerMessage;



/**
 * Network class that handles the network communication on client side.
 */
public class Network {

	private String host;
	private int port;
	private Socket socket;
	private String macaddress = null;

	private InThread inThread = null;
	private OutThread outThread = null;

	private List<GameMessage> queue;

	private List<MessageListener> listeners;

	private boolean connected = false;
	
	private static final Network INSTANCE = new Network();

	/**
	 * Initialize a network instance.
	 * 
	 * @param host the Host Address
	 * @param port the Port to connect
	 */
	public static void init(String host, int port) {
		INSTANCE.host = host;
		INSTANCE.port = port;
		INSTANCE.queue = Collections.synchronizedList(new LinkedList<GameMessage>());
		INSTANCE.listeners = new LinkedList<MessageListener>();
	}

	/**
	 * contact the server.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void connect() throws UnknownHostException, IOException {
			if (INSTANCE.connected)
				return;
			Core.logger.info("Connecting to " + INSTANCE.host + ":" + INSTANCE.port);
			INSTANCE.socket = new Socket(INSTANCE.host, INSTANCE.port);

			INSTANCE.inThread = new InThread(INSTANCE.socket.getInputStream());
			INSTANCE.inThread.start();
			INSTANCE.outThread = new OutThread(INSTANCE.socket.getOutputStream());
			INSTANCE.outThread.start();

			INSTANCE.connected = true;		
			
			//this.macaddress = byteArrayToHexString(NetworkInterface
			//		.getByInetAddress(this.socket.getLocalAddress())
			//		.getHardwareAddress());

			
			if (INSTANCE.macaddress == null){
				INSTANCE.macaddress = String.valueOf((int)Math.round( Math.random() * 10000000 )+1000000); 
			}
			
			Core.logger.info("MAC: " + INSTANCE.macaddress);
	}

	/**
	 * Closes the connection to the server.
	 */
	public static void disconnect() {
		if (!INSTANCE.connected)
			return;
		INSTANCE.connected = false;
		INSTANCE.inThread.terminate();
		INSTANCE.outThread.terminate();
		try {
			INSTANCE.socket.close();
		} catch (IOException e) {
			Core.logger.warning("Network disconnect failed. Socket is "
					+ (INSTANCE.socket.isClosed()? "closed.": "open."));
		}
		
	}

	/**
	 * Method to send ClientMessages to the server.
	 * 
	 * @param message
	 *            ClientMessage
	 */
	public static void sendMessage(ClientMessage message) {
		INSTANCE.outThread.send(message);
	}

	/**
	 * @return the queue
	 */
	public static List<GameMessage> getQueue() {
		return INSTANCE.queue;
	}

	/**
	 * add a GameMessage to the queue.
	 * 
	 * @param gm the GameMessage to add to the queue
	 */
	public static void addGameMessage(GameMessage gm) {
		INSTANCE.queue.add(gm);
	}

	/**
	 * add listener for messages.
	 * 
	 * @param messageListener message listener
	 */
	public static void addListener(MessageListener messageListener) {
		synchronized (INSTANCE.listeners) {
			if (INSTANCE.listeners.contains(messageListener))
				return;
			INSTANCE.listeners.add(messageListener);
		}
	}

	/**
	 * removes a listener.
	 * 
	 * @param messageListener message listener
	 */
	public static void removeListener(MessageListener messageListener) {
		synchronized (INSTANCE.listeners) {
			if (INSTANCE.listeners.contains(messageListener))
				INSTANCE.listeners.remove(messageListener);
		}
	}

	/**
	 * sends the message to all listeners.
	 * 
	 * @param message the message object to be notified of
	 */
	public static void notifyListeners(ServerMessage message) {
		List<MessageListener> list = new LinkedList<MessageListener>();
		synchronized (INSTANCE.listeners) {
			for (MessageListener messageListener : INSTANCE.listeners) {
				list.add(messageListener);
			}
		}
		for (MessageListener messageListener : list) {
			messageListener.receive(message);
		}
	}

	/**
	 * Convert a byte[] array to readable string format.
	 * This makes the "hex" readable!
	 * 
	 * @return result String buffer in String format
	 * @param in byte[] buffer to convert to string format
	 */
	/*
	private static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;
		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);

		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F); // must do this is high order bit is on!
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			out.append("-"); // add "-" for separating bytes
			i++;
		}
		out.deleteCharAt(out.length() - 1); // remove last "-"
		String rslt = new String(out);
		return rslt;
	}
	*/

	/**
	 * @return the MAC-Address
	 */
	public static String getMACAddress() {
		return INSTANCE.macaddress;
	}
}

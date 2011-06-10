package com.creepsmash.client.network;

import com.creepsmash.common.messages.server.ServerMessage;

public interface MessageListener {
	
	/**
	 * Update method for listeners.
	 * @param serverMessage the server message object.
	 */
	void receive(ServerMessage serverMessage);

}

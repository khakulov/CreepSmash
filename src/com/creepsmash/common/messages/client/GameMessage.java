package com.creepsmash.common.messages.client;


/**
 * Messages from client to server during a game.
 */
public interface GameMessage {

	/**
	 * Sets the values of a message with the given arguments from
	 * message-string.
	 *
	 * @param messageString the message to initialize with
	 */
	void initWithMessage(String messageString);

	/**
	 * @return the message-string to transfer to the client
	 * @throws Exception
	 */
	String toString();

	/**
	 * Return the clientId.
	 * @return the clientId
	 */
	Integer getClientId();

	/**
	 * Set the clientId.
	 * @param clientId the clientId to set
	 */
	void setClientId(Integer clientId);

}

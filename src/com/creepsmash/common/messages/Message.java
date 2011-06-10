package com.creepsmash.common.messages;

/**
 * super-class for all messages.
 */
public abstract class Message {
	/**
	 * Sets the values of a message with the given arguments from
	 * message-string. 
	 *
	 * @param messageString the message to initialize with
	 */
	public abstract void initWithMessage(String messageString);

	/**
	 * @return the message-string to transfer to the client
	 * @throws Exception 
	 */
	public abstract String toString();

	/**
	 * Prepares a string for a message.
	 * 
	 * @param string the string to send. Must not be null.
	 * @return the prepared string.
	 */
	public static String prepareToSend(String string) {
		if (string == null) {
			return  null;
		}
		return string.replace("\"", "");
	}
}

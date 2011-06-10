package com.creepsmash.common.messages.server;

/**
 * Interface for types that are game relevant.
 * BuildTowerMessage etc..
 */
public interface GameMessage {

	/**
	 * Gets the round id out of the message.
	 * @return the round id
	 */
	Long getRoundId();
	
	/**
	 * Gets the playerId out of the message.
	 * @return the player id
	 */
	Integer getPlayerId();
}

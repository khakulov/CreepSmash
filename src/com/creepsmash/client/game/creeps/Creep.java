package com.creepsmash.client.game.creeps;

import java.awt.Graphics2D;

import com.creepsmash.common.CreepType;


/**
 * Interface for all creeps.
 */
public interface Creep {

	/**
	 * Updates the creep.
	 * @param tickNumber the current tick number
	 */
	void update(long tickNumber);
	
	/**
	 * paints the creep.
	 * @param g the graphics object
	 */
	void paint(Graphics2D g);

	/**
	 * @return the id from the player who initially sent the creep
	 */
	int getSenderId();
	
	/**
	 * @param senderId the id from the player who sent the creep
	 */
	void setSenderId(int senderId);

	/**
	 * Getter for the speed of the creep.
	 * @return the current creep speed
	 */
	double getSpeed();

	/**
	 * Setter for the speed value.
	 * @param speed speed of creep
	 * @param slowTime slow time
	 */
	void slow(double speed, int slowTime);

	/**
	 * Getter the health of the creep.
	 * @return the health
	 */
	int getHealth();

	/**
	 * Setter for the health value.
	 * @param h health of creep
	 */
	void setHealth(int h);

	/**
	 * Sets the build time for the creep. Used to correct the time
	 * if the server sends a message.
	 * @param buildTime the remaining build time in rounds
	 */
	void setRound(long round);

	/**
	 * Getter for the x position.
	 * @return the x pos
	 */
	double getX();

	/**
	 * Getter for the y position.
	 * @return the y pos
	 */
	double getY();

	/**
	 * Getter for the rotation angle.
	 * @return the rotation angle
	 */
	double getAngle();

	/**
	 * Getter for the creep type.
	 * @return the type of the creep
	 */
	CreepType getType();

	/**
	 * Tests if the Creep is a valid target.
	 * @return true if the creep is activ and not dead, else false
	 * @return
	 */
	boolean isActive();

	/**
	 * @return the total segment steps moved
	 */
	int getTotalSegmentSteps();
}

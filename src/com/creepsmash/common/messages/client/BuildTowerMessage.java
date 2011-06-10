package com.creepsmash.common.messages.client;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creepsmash.common.messages.Message;


/**
 * Message from client for building a tower.
 */
public class BuildTowerMessage extends ClientMessage implements GameMessage {

	
	/**
	 * regular expression for message-parsing.
	 */ 
	private static final String REGEXP_BUILD_TOWER = 
		"BUILD_TOWER\\s\"([^\"]+)\"\\sAT\\s([0-9]+),([0-9]+)\\s([0-9]+)";
	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REGEXP_BUILD_TOWER);
	
	
	private String towerType;
	
	private Point position;

	private long roundId;

	/**
	 * @return the tower type
	 */
	public String getTowerType() {
		return this.towerType;
	}

	/**
	 * @param towerType the tower type
	 */
	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}

	/**
	 * @return the postion
	 */
	public Point getPosition() {
		return this.position;
	}

	/**
	 * @param position the postion
	 */
	public void setPosition(Point position) {
		this.position = position;
	}

	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setTowerType(matcher.group(1));
			this.position = new Point();
			this.position.x = Integer.valueOf(matcher.group(2));
			this.position.y = Integer.valueOf(matcher.group(3));
			this.setPosition(this.position);
			this.setRoundId(Long.valueOf(matcher.group(4)));
		}		
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "BUILD_TOWER \"" 
		+ Message.prepareToSend(this.getTowerType()) 
		+ "\" AT " + this.getPosition().x + "," + this.getPosition().y + " " + this.getRoundId();
	}

	/**
	 * @param incomeRoundId the incomeRoundId to set
	 */
	public void setRoundId(long roundId) {
		this.roundId = roundId;
	}

	/**
	 * @return the incomeRoundId
	 */
	public long getRoundId() {
		return roundId;
	}
	
	
}

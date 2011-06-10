package com.creepsmash.common.messages.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creepsmash.common.messages.Message;


/**
 * Message from client, building a creep.
 */
public class BuildCreepMessage extends ClientMessage implements GameMessage {

	/**
	 * Regular expression for message-parsing.
	 */
	private static final String REGEXP_BUILD_CREEP = "BUILD_CREEP\\s\"([^\"]+)\"\\s([0-9]+)";

	/**
	 * Pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REGEXP_BUILD_CREEP);

	private String creepType;

	private long roundId;

	/**
	 * @return the type of the creep
	 */
	public String getCreepType() {
		return this.creepType;
	}

	/**
	 * @param creepType
	 *            the type of the creep
	 */
	public void setCreepType(String creepType) {
		this.creepType = creepType;
	}

	/**
	 * @param messageString
	 *            the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setCreepType(matcher.group(1));
			this.setRoundId(Long.valueOf(matcher.group(2)));
		}
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "BUILD_CREEP \""
				+ Message.prepareToSend(this.getCreepType()) + "\" "
				+ this.getRoundId();
	}

	/**
	 * @param incomeRoundId
	 *            the incomeRoundId to set
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

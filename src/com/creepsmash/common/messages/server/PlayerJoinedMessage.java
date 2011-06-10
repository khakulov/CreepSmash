
/**
   Creep Smash, a multiplayer towerdefence game
   created as a project at the Hochschule fuer
   Technik Stuttgart (University of Applied Science)
   http://www.hft-stuttgart.de 
   
   Copyright (C) 2008 by      
    * Andreas Wittig
    * Bernd Hietler
    * Christoph Fritz
    * Fabian Kessel
    * Levin Fritz
    * Nikolaj Langner
    * Philipp Schulte-Hubbert
    * Robert Rapczynski
    * Ron Trautsch
    * Sven Supper
    http://creepsmash.sf.net/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/


package com.creepsmash.common.messages.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creepsmash.common.messages.Message;



/**
 * Message send from server to client, if a new player joined.
 * 
 * @author andreas
 *
 */
public class PlayerJoinedMessage extends ServerMessage {

	
	private static final String REG_EXP = 
		"PLAYER_JOINED\\s([^\\s]+)\\s([0-9]+)\\s([0-9]+)";

	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REG_EXP);
	
	
	private String playerName;
	private Integer playerEloScore = 0;
	private Integer playerId;
	
	/**
	 * Default constructor.
	 */
	public PlayerJoinedMessage() {
		super();
	}

	/**
	 * @param playerName the name of the player
	 * @param playerId the id of the player
	 */
	public PlayerJoinedMessage(String playerName, Integer playerId, Integer playerEloScore) {
		this.playerName = playerName;
		this.playerId = playerId;
		this.playerEloScore = playerEloScore;
	}

	
	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	
	/**
	 * @return the playerId
	 */
	public Integer getPlayerId() {
		return this.playerId;
	}


	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the playerId
	 */
	public Integer getPlayerEloScore() {
		return this.playerEloScore;
	}


	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerEloScore(Integer playerEloScore) {
		this.playerEloScore = playerEloScore;
	}
	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "PLAYER_JOINED \"" + Message.prepareToSend(this.playerName) 
		+ "\" " + this.playerId
		+ " " + this.playerEloScore;
	}
	
	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setPlayerName(matcher.group(1).replace("\"", ""));
			this.setPlayerId(Integer.parseInt(matcher.group(2)));
			this.setPlayerEloScore(Integer.parseInt(matcher.group(3)));		
			
		}
		
	}

	/**
	 * Returns true if o is a PlayerJoinedMessage with the same contents
	 * as this one.
	 * @param o the object to compare to.
	 * @return true if o is equal to this object.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PlayerJoinedMessage)) {
			return false;
		}
		PlayerJoinedMessage m = (PlayerJoinedMessage) o;
		return this.playerName.equals(m.getPlayerName())
			&& this.playerId == m.getPlayerId()
			&& this.playerEloScore == m.getPlayerEloScore();
	}

	/**
	 * Returns a hash code for this object.
	 * @return a hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.playerName.hashCode() ^ this.playerId;
	}
		
}

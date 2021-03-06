
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
 * Message send from server to client, if a player quits a game.
 * 
 * @author andreas
 *
 */
public class PlayerQuitMessage extends ServerMessage {

	
	private static final String REG_EXP = 
		"PLAYER_QUIT\\s\"([^\"]+)\"\\s([0-9]+)\\s\"([^\"]+)\"";

	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REG_EXP);
	
	
	private String playerName;
	private int playerID = 0;
	private String reason;


	/**
	 * Returns a hash code for this object.
	 * @return a hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.playerName.hashCode() ^ this.reason.hashCode();
	}

	/**
	 * default constructor.
	 */
	public PlayerQuitMessage() {
		super();
	}

	/**
	 * @param playerName the name of the player
	 * @param reason the reason for quitting
	 */
	public PlayerQuitMessage(String playerName, String reason, int playerID) {
		this.playerName = playerName;
		this.reason = reason;
		this.playerID = playerID;
	}

	
	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
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
	 * @return the reason
	 */
	public String getReason() {
		return this.reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "PLAYER_QUIT \"" + Message.prepareToSend(this.playerName)
		+ "\" " + this.playerID
		+ " \"" + Message.prepareToSend(this.reason) + "\"";
	}
	
	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setPlayerName(matcher.group(1));
			this.setPlayerID(Integer.parseInt(matcher.group(2)));
			this.setReason(matcher.group(3));
		}		
	}
	
	/**
	 * Returns true if o is a PlayerQuitMessage with the same attributes
	 * as this one.
	 * @param o the object to compare to.
	 * @return true if o is equal to this object.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PlayerQuitMessage)) {
			return false;
		}
		PlayerQuitMessage m = (PlayerQuitMessage) o;
		return this.playerName.equals(m.getPlayerName())
			&& this.playerID == m.getPlayerID()
			&& this.reason.equals(m.getReason());
	}

}

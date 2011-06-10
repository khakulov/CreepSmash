
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


package com.creepsmash.common.messages.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.creepsmash.common.messages.Message;


/**
 * Message from client to server to request the score for one player.
 */
public class ScoreRequestMessage extends ClientMessage 
								implements LobbyMessage {

	/**
	 * regular expression for message parsing.
	 */
	private static final String REGEXP_SCORE_REQUEST = "SCORE_REQUEST\\s\"([^\"]+)\"";
	
	private String playerName;
	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN =
		Pattern.compile(REGEXP_SCORE_REQUEST);
	
	/**
	 * Default no-parameter constructor.
	 */
	public ScoreRequestMessage() {
		
	}
	/**
	 * Creates a new ScoreRequestMessage.
	 * @param playerName the playername
	 */
	public ScoreRequestMessage(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "SCORE_REQUEST \"" + Message.prepareToSend(playerName) + "\"";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setPlayerName(matcher.group(1));
		}
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}


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


/**
 * Message from client, requesting the highscores.
 * 
 * @author andreas
 *
 */
public class HighscoreRequestMessage extends ClientMessage 
										implements LobbyMessage {

	/**
	 * regular expression for message parsing.
	 */
	private static final String REGEXP_HIGHSCORE_REQUEST = "HIGHSCORE_REQUEST\\s([0-9]+)";

	private int start;
	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = 
		Pattern.compile(REGEXP_HIGHSCORE_REQUEST);
	
	/**
	 * Default no-parameter constructor.
	 */
	public HighscoreRequestMessage() {
		
	}
	
	/**
	 * Creates a new HighScoreRequestMessage.
	 * @param start the start position
	 */
	public HighscoreRequestMessage(int start) {
		this.start = start;
	}
	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setStart(Integer.valueOf(matcher.group(1)));
		}
	}


	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "HIGHSCORE_REQUEST " + start;
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}	
}

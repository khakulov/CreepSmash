
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

import java.util.regex.Pattern;


/**
 * Message from client, requesting the start of a waiting game.
 * 
 * @author andreas
 *
 */
public class StartGameRequestMessage extends ClientMessage 
											implements GameMessage {

	/**
	 * regular expression for message-parsing.
	 */ 
	private static final String REGEXP_START_GAME_REQUEST = 
		"START_GAME_REQUEST";

	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = 
		Pattern.compile(REGEXP_START_GAME_REQUEST);
	
	/**
	 * @param messageString the message
	 */
	@Override
	public void initWithMessage(String messageString) {
		//do nothing!		
	}


	/**
	 * @return the message
	 */
	@Override
	public String toString() {
		return "START_GAME_REQUEST";
	}
		
}

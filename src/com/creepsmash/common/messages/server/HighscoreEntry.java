
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


/**
 * Entry of highscore-table.
 * 
 * @author andreas
 *
 */
public class HighscoreEntry {
	
	
	private static final String REG_EXP = 
		"[\\s]?\"([^\"]+)\"\\s([0-9]+)\\s((-)?[0-9]+)[\\s]?";

	public static final Pattern PATTERN = Pattern.compile(REG_EXP);
	
	
	private String playerName;
	
	private Integer oldPoints;
	
	private Integer points;

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
	 * @return the oldPoints
	 */
	public Integer getOldPoints() {
		return this.oldPoints;
	}

	/**
	 * @param oldPoints the oldPoints to set
	 */
	public void setOldPoints(Integer oldPoints) {
		this.oldPoints = oldPoints;
	}

	/**
	 * @return the points
	 */
	public Integer getPoints() {
		return this.points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	
	/**
	 * @return the String representation of the message.
	 */
	@Override
	public String toString() {
		return "\"" + this.getPlayerName() + "\" " + this.getPoints() + " " + this.getOldPoints();
	}
	
	
	/**
	 * @param messageString the messageString
	 */
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setPlayerName(matcher.group(1));
			this.setPoints(Integer.valueOf(matcher.group(2)));
			this.setOldPoints(Integer.valueOf(matcher.group(3)));
		}
	}	
	
}

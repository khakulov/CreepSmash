
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
 * Message reply for one player.
 * @author philipp
 *
 */
public class ScoreResponseMessage extends ServerMessage {

	
	/**
	 * Regular expression for this message.
	 */
	private static final String REG_EXP = 
		"SCORE\\s\"([^\"]+)\"\\s([0-9]+)\\s((-)?[0-9]+)[\\s]?";

	
	private String playerName;
	
	private Integer oldPoints;
	
	private Integer points;
	/**
	 * Pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REG_EXP);
	
	/**
	 * Default no-parameter constructor.
	 */
	public ScoreResponseMessage() {
		
	}
	/**
	 * Creates a new ScoreResponseMessage.
	 * @param playerName the players name
	 * @param oldPoints the points for the last game
	 * @param points the actual points in the highscore
	 */
	public ScoreResponseMessage(String playerName, Integer oldPoints, Integer points) {
		this.playerName = playerName;
		this.oldPoints = oldPoints;
		this.points = points;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "SCORE \"" + this.playerName 
					+ "\" " + this.points + " " 
					+ this.oldPoints; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setPlayerName(matcher.group(1));
			this.setPoints(Integer.valueOf(matcher.group(2)));
			this.setOldPoints(Integer.valueOf(matcher.group(3)));
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

	/**
	 * @return the oldPoints
	 */
	public Integer getOldPoints() {
		return oldPoints;
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
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

}

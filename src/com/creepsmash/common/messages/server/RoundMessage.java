
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
 * Round message from server to client.
 * 
 * @author andreas
 *
 */
public class RoundMessage extends ServerMessage {

	
	private static final String REG_EXP = "ROUND\\s([0-9]+)\\sOK";

	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REG_EXP);
	
	
	private Long roundId;
	
	
	/**
	 * @return the roundId
	 */
	public Long getRoundId() {
		return this.roundId;
	}

	/**
	 * @param roundId the roundId to set
	 */
	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "ROUND " + this.roundId + " OK";
	}
	
	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setRoundId(Long.parseLong(matcher.group(1)));
		}
	}

	/**
	 * Returns true if o is a RoundMessage with the same roundId as this one.
	 * @param o the object to compare to.
	 * @return true if o is equal to this object.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RoundMessage)) {
			return false;
		}
		RoundMessage m = (RoundMessage) o;
		return this.roundId.equals(m.getRoundId());
	}

	/**
	 * Returns a hash code for this object.
	 * @return a hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (long) this.roundId;
	}

}

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

import com.creepsmash.common.IConstants.ErrorType;
import com.creepsmash.common.messages.Message;


/**
 * Message from client, indicating that an error has
 * occured.
 * 
 * @author andreas
 *
 */
public class ErrorMessage extends ClientMessage {

	/**
	 * 	regular expression for message-parsing.
	 */
	private static final String REGEXP_ERROR = 
		"ERROR\\s([^\\s]+)\\s\"([^\"]+)\"";
	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REGEXP_ERROR);
	
	
	private ErrorType errorType;
	
	private String errorMessage;

	
	
	/**
	 * @return the errorType
	 */
	public ErrorType getErrorType() {
		return this.errorType;
	}

	/**
	 * @param errorType the errorType to set
	 */
	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	/**
	 * @return the message
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @param errorMessage the message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setErrorType(ErrorType.valueOf(matcher.group(1)));
			this.setErrorMessage(matcher.group(2));
		}
		
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "ERROR " + this.getErrorType() 
			+ " \"" + Message.prepareToSend(this.getErrorMessage()) + "\"";
	}
	
	
	
}


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
 * Message from client. Requests a login.
 * 
 * @author andreas
 *
 */
public class LoginRequestMessage extends ClientMessage {

	/**
	* regular expression for message parsing.
	*/
	private static final String REGEXP_LOGIN_REQUEST = 
		"LOGIN_REQUEST\\s\"([^\"]+)\"\\s\"([^\"]+)\"\\s\"([^\"]+)\"\\s\"([^\"]+)\"";
	
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN = Pattern.compile(REGEXP_LOGIN_REQUEST);
	
	
	private String version;
	private String username;
	private String password;
	private String macaddress;

	/**
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @param version the version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * @return the name of the user
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * 
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the macaddress
	 */
	public String getMacaddress() {
		return macaddress;
	}

	/**
	 * @param macaddress the macaddress to set
	 */
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	/**
	 * Initializes Message with data from String.
	 * 
	 * @param messageString the message as String
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setVersion(matcher.group(1));
			this.setUsername(matcher.group(2));
			this.setPassword(matcher.group(3));
			this.setMacaddress(matcher.group(4));
		}		
	}

	/**
	 * @return the String-representation of the message
	 */
	@Override
	public String toString() {
		return "LOGIN_REQUEST \"" 
			+ Message.prepareToSend(this.getVersion()) 
			+ "\" \"" + Message.prepareToSend(this.getUsername()) 
			+ "\" \"" + Message.prepareToSend(this.getPassword())
		    + "\" \"" + Message.prepareToSend(this.getMacaddress()) + "\"";
	}
	
}

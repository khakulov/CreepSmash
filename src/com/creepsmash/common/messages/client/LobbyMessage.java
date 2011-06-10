
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

/**
 * Messages from client to server, while client is in lobby.
 *
 * @author andreas
 *
 */
public interface LobbyMessage {

	/**
	 * Sets the values of a message with the given arguments from
	 * message-string.
	 *
	 * @param messageString the message to initialize with
	 */
	void initWithMessage(String messageString);

	/**
	 * @return the message-string to transfer to the client
	 * @throws Exception
	 */
	String toString();

	/**
	 * Return the clientId.
	 * @return the clientId
	 */
	Integer getClientId();

	/**
	 * Set the clientId.
	 * @param clientId the clientId to set
	 */
	void setClientId(Integer clientId);


}

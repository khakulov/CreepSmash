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

import com.creepsmash.common.messages.Message;

/**
 * Message from client to server.
 * 
 * @author andreas
 * 
 */
public abstract class ClientMessage extends Message {

	private Integer clientId;

	/**
	 * @return the clientId
	 */
	public Integer getClientId() {
		return this.clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public static ClientMessage renderMessageString(String messageString) {
		ClientMessage message = null;
		if (BuildCreepMessage.PATTERN.matcher(messageString).matches()) {
			message = new BuildCreepMessage();
		} else if (BuildTowerMessage.PATTERN.matcher(messageString).matches()) {
			message = new BuildTowerMessage();
		} else if (ChangeStrategyMessage.PATTERN.matcher(messageString).matches()) {
			message = new ChangeStrategyMessage();
		} else if (LiveTakedMessage.PATTERN.matcher(messageString).matches()) {
			message = new LiveTakedMessage();
		} else if (CreateGameMessage.PATTERN.matcher(messageString).matches()) {
			message = new CreateGameMessage();
		} else if (DeleteRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new DeleteRequestMessage();
		} else if (ErrorMessage.PATTERN.matcher(messageString).matches()) {
			message = new ErrorMessage();
		} else if (ExitGameMessage.PATTERN.matcher(messageString).matches()) {
			message = new ExitGameMessage();
		} else if (GameOverMessage.PATTERN.matcher(messageString).matches()) {
			message = new GameOverMessage();
		} else if (HighscoreRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new HighscoreRequestMessage();
		} else if (JoinGameRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new JoinGameRequestMessage();
		} else if (KickPlayerRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new KickPlayerRequestMessage();
		} else if (LoginRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new LoginRequestMessage();
		} else if (LogoutMessage.PATTERN.matcher(messageString).matches()) {
			message = new LogoutMessage();
		} else if (PongMessage.PATTERN.matcher(messageString).matches()) {
			message = new PongMessage();
		} else if (RefreshMessage.PATTERN.matcher(messageString).matches()) {
			message = new RefreshMessage();
		} else if (RegistrationRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new RegistrationRequestMessage();
		} else if (ScoreRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new ScoreRequestMessage();
		} else if (SellTowerMessage.PATTERN.matcher(messageString).matches()) {
			message = new SellTowerMessage();
		} else if (SendMessageMessage.PATTERN.matcher(messageString).matches()) {
			message = new SendMessageMessage();
		} else if (StartGameRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new StartGameRequestMessage();
		} else if (UpdateDataRequestMessage.PATTERN.matcher(messageString).matches()) {
			message = new UpdateDataRequestMessage();
		} else if (UpgradeTowerMessage.PATTERN.matcher(messageString).matches()) {
			message = new UpgradeTowerMessage();
		} else {
			message = new InvalidMessage();
			return message;
		}
		message.initWithMessage(messageString);
		return message;
	}

}

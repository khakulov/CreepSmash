
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
 * Message from client. Client creates a new game.
 * 
 * @author andreas
 *
 */
public class CreateGameMessage extends ClientMessage implements LobbyMessage {

	/**
	 * regular expression for message-parsing.
	 */
	private static final String REGEXP_CREATE_GAME_REQUEST = 
	"CREATE_GAME_REQUEST\\s\"([^\"]+)\"\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s([0-9]+)\\s\"(.*)\"\\s([0-9]+)";
		
	/**
	 * pattern for regular expression.
	 */
	public static final Pattern PATTERN =
		Pattern.compile(REGEXP_CREATE_GAME_REQUEST);
	
	private String gameName;
	
	private Integer mapId;
	
	private Integer maxPlayers;
	
	private String Passwort;
	private Integer MaxEloPoints;
	private Integer MinEloPoints;
	
	private Integer gameMode;

	/**
	 * No-arg constructor.
	 */
	public CreateGameMessage() {
		super();
	}

	/**
	 * Constructor that initializes the fields.
	 * @param gameName initial gameName
	 * @param mapId initial mapId
	 * @param maxPlayers initial maxPlayers
	 */
	public CreateGameMessage(String gameName, Integer mapId,Integer maxPlayers, Integer MaxEloPoints, Integer MinEloPoints, String Passwort) {
		this.gameName = gameName;
		this.mapId = mapId;
		this.maxPlayers = maxPlayers;
		this.Passwort = Passwort;
		this.MaxEloPoints = MaxEloPoints;
		this.MinEloPoints = MinEloPoints;
		
	}
	
	/**
	 * @return the name of the game
	 */
	public String getGameName() {
		return this.gameName;
	}

	/**
	 * @param gameName the name of the game
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the map id
	 */
	public Integer getMapId() {
		return this.mapId;
	}

	/**
	 * @param mapId the map id
	 */
	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	/**
	 * @return the number of max players
	 */
	public Integer getMaxPlayers() {
		return this.maxPlayers;
	}

	/**
	 * @param maxPlayers the number of max players
	 */
	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	/**
	 * @param Passwort the Passwort of this game
	 */
	public void setPasswort(String Passwort) {
		this.Passwort = Passwort;		
	}
	/**
	 * @return Game Passwort
	 */
	public String getPasswort() {
		return this.Passwort;
	}
	/**
	 * @param MaxEloPoints of this game
	 */
	public void setMaxEloPoints(Integer MaxEloPoints) {
		this.MaxEloPoints = MaxEloPoints;		
	}
	
	/**
	 * @return the number of MaxEloPoints
	 */
	public Integer getMaxEloPoints() {
		return this.MaxEloPoints;
	}
	
	/**
	 * @param maxPlayers MinEloPoints of this game
	 */
	public void setMinEloPoints(Integer MinEloPoints) {
		this.MinEloPoints = MinEloPoints;		
	}
	
	/**
	 * @return the number of MinEloPoints
	 */
	public Integer getMinEloPoints() {
		return this.MinEloPoints;
	}
	
	/**
	 * @param gameMode Mode number of game
	 */
	public void setGameMode(Integer gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * @return the Gamemode number
	 */
	public Integer getGameMode() {
		return gameMode;
	}
	
	/**
	 * @param messageString the message as String.
	 */
	@Override
	public void initWithMessage(String messageString) {
		Matcher matcher = PATTERN.matcher(messageString);
		if (matcher.matches()) {
			this.setGameName(matcher.group(1));
			this.setMapId(Integer.valueOf(matcher.group(2)));
			this.setMaxPlayers(Integer.valueOf(matcher.group(3)));
			this.setMaxEloPoints(Integer.valueOf(matcher.group(4)));
			this.setMinEloPoints(Integer.valueOf(matcher.group(5)));
			this.setPasswort(matcher.group(6));
			this.setGameMode(Integer.valueOf(matcher.group(7)));
		}		
	}

	/**
	 * @return the message as String.
	 */
	@Override
	public String toString() {
		return "CREATE_GAME_REQUEST \"" 
		+ Message.prepareToSend(this.getGameName()) + "\" " 
		+ this.getMapId() + " " + this.getMaxPlayers()
		+ " " + this.getMaxEloPoints() + " " + this.getMinEloPoints() + " "
		+ "\"" +Message.prepareToSend(this.getPasswort()) + "\" "
		+ this.getGameMode();
	}
}

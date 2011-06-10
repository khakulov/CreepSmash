package com.creepsmash.server.game;

import java.util.concurrent.atomic.AtomicInteger;

import com.creepsmash.common.GameMap;
import com.creepsmash.common.messages.client.CreateGameMessage;


public abstract class AbstractGame extends Thread {
	private static AtomicInteger gameCount = new AtomicInteger(0);

	private int gameId;
	private String gameName;
	private int mode = 0; // 0 = Normal, 1 = All vs. All, 2 = Random
	private int mapId;
	private int maxPlayers;
	private String passwort;
	private Integer maxPoints;
	private Integer minPoints;

	public AbstractGame(CreateGameMessage message) {
		this.gameId = gameCount.incrementAndGet();
		this.gameName = message.getGameName();
		this.mode = message.getGameMode();
		this.mapId = message.getMapId();
		this.maxPlayers = message.getMaxPlayers();
		this.passwort = message.getPasswort();
		this.maxPoints = message.getMaxEloPoints();
		this.minPoints = message.getMinEloPoints();
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public int getMode() {
		return mode;
	}

	public int getMapId() {
		return mapId;
	}

	public void setRandomMap() {
		this.mapId = (int) ((Math.random() * GameMap.values().length) + 1);
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public String getPasswort() {
		return passwort;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	public Integer getMinPoints() {
		return minPoints;
	}

	public String toString() {
		return this.getGameId() + "/" + this.getGameName();
	}
}
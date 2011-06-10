package com.creepsmash.client.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creepsmash.client.Core;
import com.creepsmash.client.game.contexts.BoardLocation;
import com.creepsmash.client.game.contexts.Context;
import com.creepsmash.client.game.creeps.Creep;
import com.creepsmash.client.game.panels.BoardPanel;
import com.creepsmash.client.network.MessageListener;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.GameMap;
import com.creepsmash.common.messages.client.GameOverMessage;
import com.creepsmash.common.messages.client.LiveTakedMessage;
import com.creepsmash.common.messages.server.PlayerQuitMessage;
import com.creepsmash.common.messages.server.RoundMessage;
import com.creepsmash.common.messages.server.ServerMessage;


public class Game implements MessageListener {

	private Context playerContext;
	private List<Context> contexts;
	private BoardPanel boardPanel;

	private long round = 0;
	private int mode;
	private GameMap map;
	private GameLoop gameLoop;
	private boolean running;

	public Game(int mode, int mapId, Map<Integer, String> players, Map<Integer, Integer> playersOrder) {
		this.mode = mode;
		this.map = GameMap.getMapById(mapId);
		this.gameLoop = new GameLoop(this);
		this.boardPanel = new BoardPanel();

		this.contextsSetup(players, playersOrder);
	}

	/**
	 * Instatiates the game context for all players.
	 */
	private void contextsSetup(Map<Integer, String> players, Map<Integer, Integer> playersOrder) {
		this.contexts = new ArrayList<Context>();
		BoardLocation loc = null;
		Context context = null;
		for (Map.Entry<Integer, String> entry : players.entrySet()) {
			switch (playersOrder.get(entry.getKey())) {
				case 0:
					loc = BoardLocation.TOPLEFT;
					break;
				case 1:
					loc = BoardLocation.TOPRIGHT;
					break;
				case 2:
					loc = BoardLocation.BOTTOMRIGHT;
					break;
				case 3:
					loc = BoardLocation.BOTTOMLEFT;
					break;
				default:
					Core.logger.warning("Creating context without location");
			}
			context = new Context(this, loc, entry.getKey(), entry.getValue());
			if (entry.getKey().equals(Core.getPlayerId())) {
				this.playerContext = context;
				this.boardPanel.addMouseMotionListener(context.getGameBoard());
				this.boardPanel.addMouseListener(context.getGameBoard());
			}
			context.setup();
			this.contexts.add(context);
		}
	}

	public void start() {
		Network.addListener(this);
		this.gameLoop.start();
		this.running = true;
	}

	public void stop() {
		this.running = false;
		this.gameLoop.terminate();
		Network.removeListener(this);
	}

	public void receive(ServerMessage serverMessage) {
		if (serverMessage instanceof PlayerQuitMessage) {
			PlayerQuitMessage playerQuitMessage = (PlayerQuitMessage) serverMessage;
			this.getContext(playerQuitMessage.getPlayerID()).kill();
		}
		if (serverMessage instanceof RoundMessage) {
			RoundMessage roundMessage = (RoundMessage) serverMessage;
			this.gameLoop.setMaxRound(roundMessage.getRoundId());
		}
	}

	public void update() {
		if (!this.isRunning())
			return;
		this.round++;

		transferCreeps();

		int deadCount = 0;
		for (Context context : contexts) {
			context.update(this.round);
			if (context.isDead()) {
				deadCount++;
			}
		}
		// Game is finished at this point stop the thread.
		if (deadCount >= contexts.size() - 1) {
			if (!this.playerContext.isDead()) {
				// send gameover Message to the server
				GameOverMessage gom = new GameOverMessage();
				gom.setPosition(this.playerContext.getWinningPosition());
				Network.sendMessage(gom);
			}
			this.stop();
		}
	}

	public void render() {
		Graphics2D graphics = this.boardPanel.getGraphics();
		for (Context gc : this.contexts) {
			gc.paint(graphics);
		}
		// draw buffer to screen
		this.boardPanel.getBufferStrategy().show();
	}

	/**
	 * Transfers the creeps from one Context to the next.
	 */
	private void transferCreeps() {
		for (Context context : contexts) {
			ArrayList<Creep> transCopy = new ArrayList<Creep>(context.getTransfer());

			Context copyToContext = findNextContext(context);

			for (Creep creep : transCopy) {
				// If sender and receiver the same Player
				if (creep.getSenderId() == copyToContext.getPlayerId()) { 
					copyToContext.getTransfer().add(creep);
				} else {
					copyToContext.getCreeps().add(creep);

					/********************************
					 * Here is calculating of taked lives from each Player *
					 ********************************/
					if (playerContext.getPlayerId() == creep.getSenderId()) {
						LiveTakedMessage ltm = new LiveTakedMessage();
						ltm.setCreepType(creep.getType().name());
						ltm.setFromPlayerId(context.getPlayerId());
						ltm.setToPlayerId(copyToContext.getPlayerId());
						ltm.setSenderId(creep.getSenderId());
						ltm.setRoundId(this.getRound());
						Network.sendMessage(ltm);
					}
				}
				context.getTransfer().remove(creep);
			}
		}
	}

	private Context findNextContext(Context start) {
		Context found = null;
		BoardLocation loc = start.getLocation();
		while (found == null) {
			loc = BoardLocation.getSuccessor(loc);
			found = findContextByLocation(loc);
			if (found != null && found.isDead())
				found = null;
		}
		return found;
	}

	private Context findContextByLocation(BoardLocation loc) {
		for (Context gc : contexts) {
			if (gc.getLocation().equals(loc)) {
				return gc;
			}
		}
		return null;
	}

	public Context getContext(int playerId) {
		for (Context gc : contexts)
			if (gc.getPlayerId() == playerId)
				return gc;
		return null;
	}

	public Map<Integer, String> getPlayers() {
		Map<Integer, String> result = new HashMap<Integer, String>();
		for (Context gc : contexts)
			result.put(gc.getPlayerId(), gc.getPlayerName());
		return result;
	}

	public boolean isRunning() {
		return this.running;
	}

	public long getRound() {
		return round;
	}

	public Context getPlayerCotext() {
		return this.playerContext;
	}

	public BoardPanel getBoardPanel() {
		return boardPanel;
	}

	public int getMode() {
		return mode;
	}

	public GameMap getMap() {
		return map;
	}
}

package com.creepsmash.client;

import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.creepsmash.client.gui.Window;
import com.creepsmash.client.gui.screens.StartScreen;
import com.creepsmash.client.network.Network;
import com.creepsmash.common.IConstants;


/**
 * The core of the game.
 */
public class Core {
	public static final Logger logger = Logger.getLogger("com.creepsmash.client");
	public static final int WIDTH = 940;
	public static final int HEIGHT = 700;
	public static final Dimension SCREENSIZE = new Dimension(WIDTH, HEIGHT);

	private static int playerId = 0;
	private static String playerName = null;

	/**
	 * Main method for the application.
	 * 
	 * @param args
	 *            the start parameters
	 */
	public static void main(String[] args) {
		String host = IConstants.DEFAULT_SERVER_HOST;
		int port = IConstants.DEFAULT_SERVER_PORT;
		if (args.length == 1) {
			host = args[0];
			port = IConstants.DEFAULT_SERVER_PORT;
		} else if (args.length == 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			System.err.println("wrong arguments");
			System.out.println("using default configuration...");
		}

		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		Network.init(host, port);
		Window.pushScreen(new StartScreen());
	}

	public static void setPlayerId(int playerId) {
		Core.playerId = playerId;
	}

	public static int getPlayerId() {
		return playerId;
	}

	public static void setPlayerName(String playerName) {
		Core.playerName = playerName;
	}

	public static String getPlayerName() {
		return playerName;
	}

}

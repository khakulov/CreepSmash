package com.creepsmash.common;

/**
 * Constants for client and server.
 */
public interface IConstants {

	/**
	 * Default hostname for server (used for webstart).
	 */
	String DEFAULT_HOSTNAME = "creepsmash.de";

	/**
	 * Default Servername.
	 */
	String DEFAULT_SERVER_HOST = "localhost";

	/**
	 * Default port for server-socket.
	 */
	int DEFAULT_SERVER_PORT = 4747;

	/**
	 * Default hostname for server (used for webstart).
	 */
	int DEFAULT_MAX_CLIENTS = 500;

	/**
	 * How long is one tick in milliseconds.
	 */
	int TICK_MS = 50;

	/**
	 * How many ticks are user actions (building/upgrading/selling a tower or
	 * sending a creep) delayed?
	 */
	int USER_ACTION_DELAY = 50;

	/**
	 * Interval of income in Ticks.
	 */
	int INCOME_TIME = 300; // 300*50/1000 = 15 seconds

	/**
	 * Income at the beginning.
	 */
	int START_INCOME = 200;

	/**
	 * Credits at the beginning.
	 */
	int START_CREDITS = 200;

	/**
	 * Lives at the beginning.
	 */
	int START_LIVES = 20;

	/**
	 * MAX 2 same Ips in game.
	 */
	boolean MUTIACCOUNT_IP_CHECK = false;

	/**
	 * MAX 1 same MAC adress in game.
	 */
	boolean MUTIACCOUNT_MAC_CHECK = true;

	double EAST = 0;
	double WEST = Math.PI;
	double WEST_MINUS = Math.PI * (-1);
	double SOUTH = (Math.PI / 2);
	double NORTH = (Math.PI / 2) * (-1);

	String SIMLEY_URL = "com/creepsmash/client/resources/smilies/";

	/**
	 * Maps Download Server
	 */
	String MAP_DOWNLOAD_URL = "http://static.creepsmash.de/";

	/**
	 * Timeout used in the server. If the server receives no messages from a
	 * client for TIMEOUT milliseconds, it sends PING. If it doesn't receive a
	 * message for another TIMEOUT milliseconds after that, it disconnects the
	 * client.
	 */
	int TIMEOUT = 30 * 1000;

	/**
	 * Creeps in einer Welle
	 */
	long CREEPS_IN_WAVE = 20;
	/**
	 * Zeitlicher Abstand zwischen 2 Creeps beim Senden einer Welle
	 */
	long SEND_WAVE_DELAY = 130;
	/**
	 * Zeitlicher Abstand zwischen 2 Creeps
	 */
	long CREEP_DELAY = 130;
	/**
	 * Zeitlicher Abstand zwischen 2 Wellen
	 */
	long WAVE_DELAY = SEND_WAVE_DELAY * (CREEPS_IN_WAVE);

	/**
	 * Indicates the type of an error.
	 */
	enum ErrorType {
		/**
		 * Close Game/Client/Server after a fatal error.
		 */
		Fatal,
		/**
		 * Error, that can be handeld.
		 */
		Error,
		/**
		 * Just a warning, not really an error.
		 */
		Warning
	}

	/**
	 * Indicates the type of a response-message.
	 */
	enum ResponseType {
		/**
		 * Request was successful.
		 */
		ok,
		/**
		 * Request was unsuccessful.
		 */
		failed,
		/**
		 * Request failed because of the given username.
		 */
		username,
		/**
		 * Request failed because the client and server have different version.
		 */
		version
	}
}
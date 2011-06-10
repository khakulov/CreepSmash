package com.creepsmash.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

import com.creepsmash.common.IConstants;
import com.creepsmash.common.Version;
import com.creepsmash.server.client.Client;


/**
 * The class Server creates a ServerSocket for the Server. It also contains the
 * Method used to instantiate new Clients.
 */
public class Server {

	private static Logger logger = Logger.getLogger(Server.class
			.getName());
	
	private static String configFile;
	private static Map<String, String> configDB;
	
	/**
	 * It is possible to start the server with a different portnumber then the
	 * defaultport.
	 *
	 * @param args
	 *            args[] = port
	 */
	public static void main(String[] args) {
		
		//actice client-sockets
		Set<Socket> activeSockets = new HashSet<Socket>();
		
		// init the log4J logger
		initLogger();		

		logger.info("Starting server version " + Version.getVersion() + "...");

		// check if working directory is writeable
		if (!new File("./").canWrite()) {
			logger.error("working directory was not writeable.");
			System.exit(1);
		}

		// load entity manager, this could take some time :-)
		setConfigFile("configSQL.xml");
		readConfigDB();

		int gameServerPort = IConstants.DEFAULT_SERVER_PORT;	
		int maxClients = IConstants.DEFAULT_MAX_CLIENTS;
		
		if (args.length >= 1) {
			try {
				gameServerPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				logger.error("parameter for gameserver-port was nut a number");
				System.exit(1);
			}
		}
		if (args.length >= 2) {
			try {
				maxClients = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				logger.error("parameter for max clients was nut a number");
				System.exit(1);
			}
		}

		// Instantiate sockets + clients
		try {
			ServerSocket serverSocket = new ServerSocket(gameServerPort);
			logger.info("server started");

			while (true) {
				for (Socket socket : new HashSet<Socket>(activeSockets)) {
					if (socket.isClosed() || !socket.isConnected()) {
						activeSockets.remove(socket);
					}
				}
				Socket socket = serverSocket.accept();
				socket.setSoTimeout(IConstants.TIMEOUT);
				activeSockets.add(socket);
				if (activeSockets.size() >= maxClients) {
					socket.close();
					logger.error("number of max clients reached.");
				} else {
					new Client(socket);
				}
			}

		} catch (BindException e) {
			logger.error("port " + gameServerPort + "already in use.");
			System.exit(1);
		} catch (IOException e) {
			logger.error("undefined error.");
		}
	}

	
	/**
	 * initialize logger.
	 */
	private static void initLogger() {
		// load log4j configuration
		try {
			InputStream is = Server.class
					.getClassLoader()
					.getResourceAsStream(
							"com/creepsmash/server/server_log4j.xml");
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(is);
			DOMConfigurator.configure(doc.getDocumentElement());
		} catch (ParserConfigurationException e) {
			System.err.println("could not read log4j configuration: " 
					+ e.getLocalizedMessage());
		} catch (org.xml.sax.SAXException e) {
			System.err.println("could not read log4j configuration: " 
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			System.err.println("could not read log4j configuration: " 
					+ e.getLocalizedMessage());
		}
	}
	
	 public static void setConfigFile(String configFile) {
		Server.configFile = configFile;
	}
	public static void setConfigDB(Map<String, String> ConfigDB) {
			Server.configDB = ConfigDB;
	}
	public static Map<String, String> getConfigFileDB (){
		
		return Server.configDB;
		
	}
	public static void readConfigDB() {

		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			
			Map<String, String> newConfig = new HashMap<String, String>();

			while (eventReader.hasNext()) {

				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					
					String ConfigSetting = "";
					if (event.asStartElement().getName().getLocalPart() == ("config") || event.asStartElement().getName().getLocalPart() == ("root")) {
						event = eventReader.nextTag();

					}
					
						String ConfigKey = event.asStartElement().getName().getLocalPart();
						event = eventReader.nextEvent();
						
						
					    ConfigSetting = event.asCharacters().getData();
						
						newConfig.put(ConfigKey,ConfigSetting);
					
				}
			}
			
			setConfigDB(newConfig);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
}

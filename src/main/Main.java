package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import server.TCPServer;

public class Main {
	static {
		InputStream stream = Main.class.getClassLoader().getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);
		} catch (SecurityException | IOException e) {
			e.printStackTrace(); 
		}
	}
	
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		int port = 6568;
		
		//Swing
		SwingTest.main(args);
		
		// TCP Clients
		ClientGenrator simul = new ClientGenrator(port);
		simul.startSimulation();
		
		// TCP Server
		try {
			TCPServer tcpServer = new TCPServer(port);
			tcpServer.start();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}



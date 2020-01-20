package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import GUI.SwingDemo;
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

	public static void main(String[] args) throws InterruptedException {
		int port = 6568;
		
		//Swing
		SwingDemo swingDemo = new SwingDemo();
		
		while(!swingDemo.isSecondFrameOpened()) {
			Thread.sleep(100);
		}
		
		Integer resources = swingDemo.getGroupAmount();
		Integer capacity = swingDemo.getCapacity();
		Integer timeout = swingDemo.geTimeout();
		Integer clientRate = swingDemo.getGenerateRate();
		Integer slots = swingDemo.slots();
		logger.info(String.format("%d %d %d %d %d", resources, capacity, timeout, clientRate, slots));
		
		// TCP Clients
		ClientGenrator simul = new ClientGenrator(port, clientRate);
		simul.startSimulation();
		
		// TCP Server
		try {
			TCPServer tcpServer = new TCPServer(port, resources, capacity, slots, timeout);
			tcpServer.start();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}



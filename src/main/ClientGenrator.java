package main;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import client.TCPClient;

public class ClientGenrator {
	private static final Logger logger = Logger.getLogger(ClientGenrator.class.getName());
	private static final int CREATE_RATE = 2; // [s]
	private final ScheduledExecutorService exec;
	private int port;
	
	public ClientGenrator(int port) {
		this.port = port;
		exec = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void startSimulation() {
		exec.scheduleAtFixedRate(() -> { 
			try {
				TCPClient client = new TCPClient(port);
				client.reserveTicket();
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}, 1, CREATE_RATE, TimeUnit.SECONDS);
	}
	
	public void abortSimulation() {
		List<Runnable> list = exec.shutdownNow();
		for(Runnable r: list) {
			logger.warning(String.format("Killed %s", r.toString()));
		}
	}
	
	public void stopSimulation() {
		exec.shutdown();
	}
}

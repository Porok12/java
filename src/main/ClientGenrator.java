package main;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import client.TCPClient;
import dao.UserDao;

public class ClientGenrator {
	private static final Logger logger = Logger.getLogger(ClientGenrator.class.getName());
	private final int CREATE_RATE; // [s]
	private final ScheduledExecutorService exec;
	private int port;
	private List<String> userNames;
	
	public ClientGenrator(int port, int clientRate) {
		CREATE_RATE = clientRate;
		
		UserDao userDao = new UserDao();
		userDao.delete();
		userDao.fill();
		userNames = userDao.getAll();
		
		this.port = port;
		exec = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void startSimulation() {
		exec.scheduleAtFixedRate(() -> {
			try {
				String login;
				if (new Random().nextInt(9) != 1) {
					login = userNames.get(new Random().nextInt(userNames.size()));
				} else {
					login = "Random";
				}
				TCPClient client = new TCPClient(port, login);
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

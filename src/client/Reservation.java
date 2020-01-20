package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import model.Group;

public class Reservation implements Callable<Void> {
	private static final Logger logger = Logger.getLogger(Reservation.class.getName());
	private DataOutputStream outToServer;
	private List<Group> groups;
	
	public Reservation(DataOutputStream outToServer, int port, List<Group> groups) 
			throws UnknownHostException, IOException {
		logger.info(String.format("ReserveTicket created %d", port));
		this.outToServer = outToServer;
		this.groups = groups;
	}
	
	@Override
	public Void call() {
		logger.info(groups.toString());
		try {
			Group ticket = groups.get(new Random().nextInt(groups.size()));
			{
				Thread.sleep(1000);
				String writeToServerString = String.format("reserve:%d", ticket.getId());
				outToServer.writeBytes(writeToServerString + '\n');
				logger.info(String.format("Write to server: %s", writeToServerString));
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return null;
	}
}

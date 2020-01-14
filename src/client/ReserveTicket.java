package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ReserveTicket implements Callable<Void> {
	private static final Logger logger = Logger.getLogger(ReserveTicket.class.getName());
	private DataOutputStream outToServer;
	
	public ReserveTicket(DataOutputStream outToServer, int port) throws UnknownHostException, IOException {
		logger.info(String.format("ReserveTicket created %d", port));
		this.outToServer = outToServer;
	}
	
	@Override
	public Void call() throws Exception {
		for (int i = 1; i < 4; i++) {
			Thread.sleep(2000);
			outToServer.writeBytes(String.format("Test%d\n", i));
			logger.info(String.format("Write to server: Test%d", i));
		}
		return null;
	}
}

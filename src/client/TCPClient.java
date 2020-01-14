package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class TCPClient {
	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
	private int port;
	
	public TCPClient(int port) {
		this.port = port;
	}
	
	public void reserveTicket() {
		try {
			Socket clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			outToServer2.writeBytes(String.format("Hello:przemek\n"));
			
//			new Thread(() -> {
//				String fromServer;
//				try {
//					fromServer = inFromServer.readLine();
//					System.out.println(fromServer);
//				} catch (IOException e1) {
//					logger.severe(e1.getMessage());
//				}
//				
//				String stringFromServer;
//				while(true) {
//					try {
//						//inFromServer.lines().forEach((o) -> logger.info("Received from server: " + o));
//						if (true) {
//							stringFromServer = inFromServer.readLine();
//							logger.info(String.format("Received from server: %s", stringFromServer));
//						}
//					} catch (IOException e) {
//						logger.severe(e.getMessage()); 
//					}	
//				}
//			}).start();
			
			ThreadFactory threadFactory = new ReserveTicketFactory();
			ReserveTicket callable = new ReserveTicket(outToServer2, port);
			Task futureTask = new Task(callable);

//			Executors.newSingleThreadExecutor(threadFactory).submit(futureTask);
//			Executors.newSingleThreadExecutor().execute(futureTask);
//			Executors.newSingleThreadExecutor(Executors.defaultThreadFactory()).execute(futureTask);
			Executors.newSingleThreadExecutor(threadFactory).execute(futureTask);
//			Executors.newSingleThreadExecutor(
//					new ReserveTicketFactory()).submit(
//							new Task(new ReserveTicket(outToServer)));
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}





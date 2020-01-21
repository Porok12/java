package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import model.Group;
import model.GroupBuilder;

public class TCPClient {
	
	private volatile boolean waitForReponse = true;

	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
	private static final Pattern groupPattern = Pattern.compile("(?i)Group([0-9]+)");
	private static final Pattern okPattern = Pattern.compile("(?i)ok([0-9]+)");
	private List<Group> groups = new ArrayList<>();
	
	private int port;
	private String login;
	private boolean abort;
	
	public TCPClient(int port, String login) {
		this.port = port;
		this.login = login;
	}
	
	public void reserve() {
		try {
			Socket clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			ThreadFactory threadFactory = new ReservationFactory();
			Reservation callable = new Reservation(outToServer2, port, groups);
			Task futureTask = new Task(callable);
			
			// Send handshake
			outToServer2.writeBytes(String.format("Hello:%s\n", login));
			
//			new Thread(() -> {
//				String fromServer;
//				
//				// Receive all tickets 
//				try {
//					fromServer = inFromServer.readLine();
//					System.out.println(fromServer);
//					Matcher matcher = ticketPattern.matcher(fromServer);
//					
//					while(matcher.find()) {
//						String ticketId = matcher.group(1);
//						System.out.println("id: " + ticketId);
//						TicketBuilder.getBuilder().setId(Integer.valueOf(ticketId));
//					}
//					
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
			
			Executors.newSingleThreadExecutor().execute(() -> {
				String fromServer;
				
				// Receive all groups 
				try {
					fromServer = inFromServer.readLine();
					System.out.println(fromServer);
					Matcher matcher = groupPattern.matcher(fromServer);
					
					abort = true;
					while(matcher.find()) {
						abort = false;
						String groupId = matcher.group(1);
						groups.add(new GroupBuilder().setId(Integer.valueOf(groupId)).build());
					}
					waitForReponse = false;
					
				} catch (IOException e1) {
					logger.severe(e1.getMessage());
				}
				
				String stringFromServer;
				while(!clientSocket.isClosed() && !clientSocket.isInputShutdown()) {
					try {
						if ((stringFromServer = inFromServer.readLine()) != null) {
							logger.info(String.format("Received from server: %s", stringFromServer));
							
							Matcher matcher = okPattern.matcher(stringFromServer);
							if (matcher.find()) {
								int groupId = Integer.valueOf(matcher.group(1));
								outToServer2.writeBytes("bye\n");
							} else {
								System.out.println("Again");
								Executors.newSingleThreadExecutor(threadFactory).execute(futureTask);
							}
						} else {
							break;
						}
					} catch (IOException e) {
						logger.severe(e.getMessage()); 
					}
				}
			});
			
			while (waitForReponse);
			
			if (abort) {
				clientSocket.close();
				return;
			}
			
			Executors.newSingleThreadExecutor(threadFactory).execute(futureTask);
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
}





import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//TODO: Lock
//TODO: W³asne logi - watki !!!

public class TCPServer {
	private Lock lock = new ReentrantLock();
	private List<Ticket> ticketList = new CopyOnWriteArrayList<Ticket>();
	private Ticket[] ticketArray = new Ticket[MAX_TICKETS];
	private int iterator = 0;
	private ServerSocket serverSocket;
	private static final int MAX_TICKETS = 3;
	private static List<ClientThread> clients = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private static int counter = 0;
	
	
	{
//		ReentrantReadWriteLock l = new ReentrantReadWriteLock();
//		l.writeLock();
//		ReentrantLock l = new ReentrantLock();
//		l.newCondition();
	}
	
	public TCPServer(int port) {
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket1").build());
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket2").build());
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket3").build());

		// ReentrantReadWriteLock
		ReentrantLock locks[] = new ReentrantLock[3];
		Arrays.fill(locks, new ReentrantLock());

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) { 
			logger.severe(e.getMessage());
			return;
		}

		logger.info(serverSocket.toString());
		
		while (!serverSocket.isClosed()) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				logger.info("Socket accepted: " + serverSocket.toString());
			} catch (IOException e) {
				logger.info(e.getMessage());
				break;
			}
			ClientThread clientThread = this.new ClientThread(socket);
			clients.add(clientThread);
			clientThread.start();
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
	
	private void reserveTicket(int tickerNumber) {
		if (tickerNumber < MAX_TICKETS && tickerNumber >= 0) {
			lock.lock();
			
			try {
				ticketArray[tickerNumber].setStatus(Status.RESERVED);
			} finally {
				lock.unlock();
			}
		}
	}
	
	class ClientThread extends Thread {
		private int inner;

		ClientThread(Socket socket) {
			inner = counter++;
			this.socket = socket;
			try {
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				logger.warning(e.getMessage());
			}
		}

		@Override
		public void start() {
			super.start();
		}

		@Override
		public void run() {
			try {
				while (true) {
					inFromClient.lines().forEach((o) -> {
						clientSentence = o;
						logger.info("Received from client: " + o);
						SwingTest.myArea.append(String.format("Received from client%d: %s\n", inner, o));
						capitalizedSentence = clientSentence.toUpperCase();
						try {
							if (capitalizedSentence.matches("(?i)HELLO.*")) {
								logger.info("Send back: " + ticketList.toString() + '\n');
								outToClient.writeBytes(ticketList.toString());
								
								Pattern pattern = Pattern.compile("(?:reserve)([0-9]+)");
								Matcher matcher = pattern.matcher(clientSentence);
								if (matcher.matches()) {
									int ticketId = Integer.valueOf(matcher.group(1));
									reserveTicket(ticketId);
								}
								
							} else {
								logger.info("Send back: " + capitalizedSentence);
								outToClient.writeBytes(capitalizedSentence + '\n');
							}
						} catch (IOException | NumberFormatException e) {
							logger.severe(e.getMessage());
						}
					});
				}
			} finally {
				closeSocekt();
			}
		}

		public void send(String msg) {
			try {
				outToClient.writeBytes(msg);
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
		}

		private boolean closeSocekt() {
			try {
				socket.close();
				logger.info("socket closed");
				return true;
			} catch (IOException e) {
				logger.info(e.getMessage());
				return false;
			}
		}

		private String clientSentence;
		private String capitalizedSentence;
		private Socket socket;
		private BufferedReader inFromClient;
		private DataOutputStream outToClient;
	}
}

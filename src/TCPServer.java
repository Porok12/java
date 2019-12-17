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

import javax.net.SocketFactory;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TODO: Lock
//TODO: W³asne logi - watki !!!

public class TCPServer {
	// Methods / Constructors
	
	Lock lock = new ReentrantLock();// utworzenie rygla
	int iterator = 0;
	
	
	
	public TCPServer(int port) {
		List<Ticket> ticketList = new CopyOnWriteArrayList<Ticket>();
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket1").build());
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket2").build());
		ticketList.add(TicketBuilder.getBuilder().setName("Ticket3").build());
		
		ReentrantLock locks[] = new ReentrantLock[3];
		Arrays.fill(locks, new ReentrantLock());
		
		
		//TODO:
		lock.lock();
		lock.unlock();
		
		
		
		
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

	private void getReservationList() {
		
	}

	private void sendReservationList() {
		
	}

	// Variables
	private ServerSocket serverSocket;
	private List<ReservationObject> tickets = new ArrayList<>();
	private static List<ClientThread> clients = new ArrayList<>();
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());

	// Classes
	class ClientThread extends Thread {
		ClientThread(Socket socket) {
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
		@SuppressWarnings("unused")
		public void run() {
			try {
				while (true) {
						inFromClient.lines().forEach((o) -> {
						clientSentence = o;
						logger.info("Received from client: " + o);
						capitalizedSentence = clientSentence.toUpperCase();
						try {
							logger.info("Send back: " + capitalizedSentence);
							outToClient.writeBytes(capitalizedSentence + '\n');
						} catch (IOException e) {
							logger.severe(e.getMessage());
						}
					});
						
					if (false) {
						clientSentence = inFromClient.readLine();
						if (clientSentence != null) {
							logger.info("RECEIVED: " + clientSentence);
							capitalizedSentence = clientSentence.toUpperCase() + '\n';
							outToClient.writeBytes(capitalizedSentence);
						} else {
							logger.info("RECEIVED: NULL");
						}
					}
				}
			} catch (IOException e) {
				logger.severe(e.getMessage());
			} finally {
				if(closeSocekt()) {
					logger.info("socket closed");
				}
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

class ReservationObject {
	public ReservationObject() {
		property1 = new Integer(0);
	}

	@Override
	public String toString() {
		return property1.toString();
	}

	private Object property1;
}

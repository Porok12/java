package server;

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

import main.SwingTest;
import model.Ticket;
import model.TicketBuilder;
import model.Ticket.Status;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TCPServer {
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private static final int MAX_TICKETS = 3;
	private static List<ClientThread> clients = new ArrayList<>();
	private Lock lock = new ReentrantLock();
	private List<Ticket> ticketList = new CopyOnWriteArrayList<Ticket>();
	private Ticket[] ticketArray = new Ticket[MAX_TICKETS];
	private ServerSocket serverSocket;
	
	private static final int MAX_SLOTS = 4;
	private BlockingQueue<ClientThread> clientsQueue = new ArrayBlockingQueue<ClientThread>(MAX_SLOTS);
	
	
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
	}
	
	public void start() {
		while (!serverSocket.isClosed()) {
			try {
				Socket socket = serverSocket.accept();
				logger.info("Socket accepted: " + socket.toString());
				addClient(socket);
				
			} catch (IOException e) {
				logger.info(e.getMessage());
				break;
			}
		}
		
		closeSocket();
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
	
	private void addClient(Socket socket) {
		if (socket != null) {
			new FutureTask<Void>(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					ClientThread clientThread = new ClientThread(socket);
					clientsQueue.add(new ClientThread(socket));
					logger.info("Client added");
					clientThread.start();
					return Void.TYPE.newInstance();
				}
			}).run();
		}
	}
	
	private void closeSocket() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}

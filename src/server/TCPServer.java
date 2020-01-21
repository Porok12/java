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
import java.util.Observable;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.SocketFactory;

import GUI.SwingDemo;
import model.Group;
import model.GroupBuilder;
import model.Group.Status;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TCPServer {
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private final int GROUPS;
	private final int MAX_SLOTS;
	private final int TIMEOUT;
	private Group[] groupArray;
	private ServerSocket serverSocket;
	private BlockingQueue<ClientThread> clientsQueue;
	private ReadWriteLock rwlock;
	private TCPServer server;
	
	public TCPServer(int port, int groups, int capacity, int slots, int timeout) {
		GROUPS = groups;
		MAX_SLOTS = slots;
		TIMEOUT = timeout;
		groupArray = new Group[GROUPS];
		rwlock = new ReentrantReadWriteLock();
		
		clientsQueue = new ArrayBlockingQueue<ClientThread>(MAX_SLOTS);
		for (int i = 0; i < GROUPS; i++) {
			groupArray[i] = new GroupBuilder().setId(i).setCapacity(capacity).buildAndAddToSwing();
		}

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) { 
			logger.severe(e.getMessage());
			return;
		}

		logger.info(serverSocket.toString());
		
		server = this;
	}
	
	public BlockingQueue<ClientThread> getClientsQueue() {
		return clientsQueue;
	}
	
	public void start() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			ClientThread client = clientsQueue.poll(); 
			if (client != null) {
				logger.warning("Client polled");
				client.interrupt();
			}
		}, TIMEOUT, TIMEOUT, TimeUnit.SECONDS);
		
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
	
	public boolean notEmpty(int groupId) {
		Lock readLock = rwlock.readLock();
		readLock.lock();
		try {
			Thread.sleep(1000);
			return groupArray[groupId].getStatus() == Group.Status.AVAILABLE;
		} catch (InterruptedException e) {
			logger.info(e.getMessage());
		} finally {
			readLock.unlock();
		}
		
		return false;
	}
	
	public boolean addUserToGroup(int groupId, String login) {
		Lock writeLock = rwlock.writeLock();
		writeLock.lock();
		try {
			Thread.sleep(1000);
			return groupArray[groupId].addUser(login);
		} catch (InterruptedException e) {
			logger.info(e.getMessage());
		} finally {
			writeLock.unlock();
		}
		
		return false;
	}
	
	private void addClient(Socket socket) {
		if (socket != null) {
			new FutureTask<Void>(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					ClientThread clientThread = new ClientThread(socket, groupArray, server);
					clientsQueue.put(clientThread);
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

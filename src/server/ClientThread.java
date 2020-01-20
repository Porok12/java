package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import GUI.SwingDemo;
import dao.UserDao;
import model.Group;

public class ClientThread extends Thread {
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private int inner;
	private static int counter = 0;
	
	private String clientSentence;
	private Socket socket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	
	private static final Pattern reservePattern = Pattern.compile("(?i)(?:reserve):([0-9]+)");
	private static final Pattern helloPattern = Pattern.compile("(?i)(?:hello):([a-zA-Z]+)");
	private static final Pattern byePattern = Pattern.compile("(?i)bye");
	
	private Group[] groups;
	private TCPServer server;
	private String login;

	ClientThread(Socket socket, Group[] groups, TCPServer tcpServer) {
		inner = counter++;
		this.socket = socket;
		this.groups = groups;
		this.server = tcpServer;
		
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
		Boolean[] close = new Boolean[] { false };
		try {
			while (!close[0]) { // !socket.isClosed()
				inFromClient.lines().forEach((o) -> {
					Matcher matcher;
					
					clientSentence = o;
					logger.info("Received from client: " + o);
					SwingDemo.myArea.append(String.format("Received from client%d: %s\n", inner, o));
					try {
						
						matcher = helloPattern.matcher(clientSentence);
						if (matcher.find()) {
							String login = matcher.group(1);
							logger.info(String.format("login: %s", login));
							
							UserDao userDao = new UserDao();
							if (userDao.getUser(login).isPresent()) {
								Stream<Group> stream = Arrays.stream(groups);
								List<Group> list = stream
										.filter(t -> t.getStatus() == Group.Status.AVAILABLE)
										.collect(Collectors.toList());
								outToClient.writeBytes(Arrays.toString(list.toArray()) + '\n');
								this.login = login;
							} else {
								outToClient.writeBytes("[]\n");
								close[0] = true;
							}
						}
						
						matcher = reservePattern.matcher(clientSentence);
						if (matcher.find()) {
							int groupId = Integer.valueOf(matcher.group(1));
							logger.info(String.format("%d>%d", inner, groupId));
							if (server.notEmpty(groupId)) {
								if (server.addUserToGroup(groupId, login)) {
									outToClient.writeBytes(String.format("ok%d", groupId) + '\n');									
								} else {
									outToClient.writeBytes(String.format("sorry\n"));			
								}
							}
						}
						
						matcher = byePattern.matcher(clientSentence);
						if (matcher.find()) {
							logger.info("bye");
							server.getClientsQueue().remove(this);						
							close[0] = true;
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
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}

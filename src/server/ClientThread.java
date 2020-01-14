package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.SwingTest;

public class ClientThread extends Thread {
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
	private int inner;
	private int iterator = 0;
	private static int counter = 0;
	
	Pattern reservePattern = Pattern.compile("(?i)(?:reserve):([0-9]+)");
	Pattern helloPattern = Pattern.compile("(?i)(?:hello):([a-zA-Z]+)");

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
					Matcher matcher;
					clientSentence = o;
					logger.info("Received from client: " + o);
					SwingTest.myArea.append(String.format("Received from client%d: %s\n", inner, o));
					capitalizedSentence = clientSentence.toUpperCase();
					try {
//						if (capitalizedSentence.matches("(?i)HELLO.*")) {
////							logger.info("Send back: " + ticketList.toString() + '\n');
////							outToClient.writeBytes(ticketList.toString());
//							
//						} else {
////							logger.info("Send back: " + capitalizedSentence);
////							outToClient.writeBytes(capitalizedSentence + '\n');
//						}
						
						matcher = reservePattern.matcher(clientSentence);
						if (matcher.find()) {
							int ticketId = Integer.valueOf(matcher.group(1));
							logger.info(String.format("%d", ticketId));
//							reserveTicket(ticketId);
						}
						
						matcher = helloPattern.matcher(clientSentence);
						if (matcher.find()) {
							String login = matcher.group(1);
							logger.warning(String.format("%s", login));
//							reserveTicket(ticketId);
						}
						
					} catch (NumberFormatException e) { // IOException | 
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

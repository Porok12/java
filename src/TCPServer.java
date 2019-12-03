import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.SocketFactory;

//TODO: Lock
//TODO: W³asne logi - watki
//TODO: w¹tki klientow

public class TCPServer {
	// Methods / Constructors
	
	public TCPServer(int port) {
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
		public void run() {
			try {
				while (true) {
					logger.info("wait");
					clientSentence = inFromClient.readLine();
					logger.info("after-wait");
					if (clientSentence != null) {
						logger.info("RECEIVED: " + clientSentence);
						capitalizedSentence = clientSentence.toUpperCase() + '\n';
						outToClient.writeBytes(capitalizedSentence);
					}
				}
			} catch (IOException e) {
				logger.severe(e.getMessage());
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

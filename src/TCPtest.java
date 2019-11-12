import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TCPtest {
	static {
		InputStream stream = TCPtest.class.getClassLoader().getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(() -> {
			try {
				TCPClient.main(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 1, 5, TimeUnit.SECONDS);

		try {
			TCPServer.main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final Logger logger = Logger.getLogger(TCPtest.class.getName());
}

class TCPServer {
//	static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
	private static List<ClientThread> clients = new ArrayList<>();

	public static void main(String argv[]) throws Exception {
		new TCPServer();
	}

	public TCPServer() {
//		String clientSentence;
//		String capitalizedSentence;
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(6722);
		} catch (IOException e) {
			return;
		}

//		ExecutorService service = Executors.newSingleThreadExecutor();

		while (!welcomeSocket.isClosed()) {
			Socket socket = null;
			try {
				socket = welcomeSocket.accept();
			} catch (IOException e) {
				logger.info(e.getMessage());
				break;
			}
			ClientThread clientThread = this.new ClientThread(socket);
			clients.add(clientThread);
			clientThread.start();

//			clients.add(welcomeSocket.accept());
//			Socket connectionSocket = welcomeSocket.accept();
//			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//			clientSentence = inFromClient.readLine();
//			
//			logger.info("RECEIVED: " + clientSentence);
//			logger.info(Arrays.toString(clientSentence.chars().toArray()));
//			capitalizedSentence = clientSentence.toUpperCase() + '\n';
//			outToClient.writeBytes(capitalizedSentence);
		}

		try {
			welcomeSocket.close();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	class ClientThread extends Thread {
		private String clientSentence;
		private String capitalizedSentence;
		private Socket socket;

		ClientThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void start() {
			super.start();
		}

		@Override
		public void run() {
			while (true) {
				try {
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
					clientSentence = inFromClient.readLine();
					inFromClient.wait(0);
					if (clientSentence != null) {
						logger.info("RECEIVED: " + clientSentence);
						logger.info(Arrays.toString(clientSentence.chars().toArray()));
						capitalizedSentence = clientSentence.toUpperCase() + '\n';
						outToClient.writeBytes(capitalizedSentence);
					}
				} catch (IOException e) {
					logger.info(e.getMessage());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
}

class TCPClient {
	public static void main(String argv[]) throws Exception {
		new Thread(() -> {
			// String sentence;
			// String modifiedSentence;
			try {
				Socket clientSocket = new Socket("localhost", 6722);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				IntStream.range(0, 5).forEach((i) -> {
					try {
						String sentence = "Hello" + i + (char) (new Random().nextInt(24) + 68);
						outToServer.writeBytes(sentence + '\n');
						outToServer.notify();
						String modifiedSentence = inFromServer.readLine();
						logger.info("FROM SERVER: " + modifiedSentence);
						Thread.sleep(100);
					} catch (InterruptedException | IOException e) {

					}
				});
				clientSocket.close();
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}).start();
		;
		return;
	}

	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
}
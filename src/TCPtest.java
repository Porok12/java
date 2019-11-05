import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TCPtest {
	static {
		InputStream stream = TCPtest.class.getClassLoader().getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
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
		}, 1, 2, TimeUnit.SECONDS);
		
		try {
			TCPServer.main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final Logger logger = Logger.getLogger(TCPtest.class.getName());
}

class TCPServer {
	static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
	
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6722);
		queue.add(1);

		while (!welcomeSocket.isClosed()) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			
			logger.info("RECEIVED: " + clientSentence);
			logger.info(Arrays.toString(clientSentence.chars().toArray()));
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
		
		welcomeSocket.close();
	}
	
	class ClientThread extends Thread {
		private Socket socket;
		
		ClientThread(Socket socket) {
			this.socket = socket;
		}
	}
	
	private static final Logger logger = Logger.getLogger(TCPServer.class.getName());
}

class TCPClient {
	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		Socket clientSocket = new Socket("localhost", 6722);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = "Hello" + (char) (new Random().nextInt(24) + 68);
		outToServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		logger.info("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
		return;
	}
	
	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
}
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class TCPClient {
	public static void main(String argv[]) throws Exception {
		new Thread(() -> {
			try {
				Socket clientSocket = new Socket("localhost", 6722);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				IntStream.range(0, 5).forEach((i) -> {
					try {
						String sentence = "Hello" + i + (char) (new Random().nextInt(24) + 68);
						outToServer.writeBytes(sentence + '\n');
						String modifiedSentence = inFromServer.readLine();
						logger.info("FROM SERVER: " + modifiedSentence);
						Thread.sleep(100);
					} catch (InterruptedException | IOException e) {
						logger.warning(e.getMessage());
					}
				});
				
				clientSocket.close();
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}).start();
		
		return;
	}

	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
}
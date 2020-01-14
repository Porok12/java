
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
		int port = 6568;
		SwingTest.main(args);
		
		// TCPClients
		ClientSimulation simul = new ClientSimulation(port);
		simul.startSimulation();
		
		// TCPServer
		try {
			TCPServer tcpServer = new TCPServer(port);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	private static final Logger logger = Logger.getLogger(TCPtest.class.getName());
}




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
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		
		// TCPClients
		exec.scheduleAtFixedRate(() -> {
			try {
				TCPClient.main(null);
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}, 1, 5, TimeUnit.SECONDS);

		// TCPServer
		try {
			TCPServer.main(null);
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
	}

	private static final Logger logger = Logger.getLogger(TCPtest.class.getName());
}



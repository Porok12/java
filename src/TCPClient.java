import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class TCPClient {
	private int port;
	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
	
	public TCPClient(int port) {
		this.port = port;
	}
	
	public void reserveTicket() {
		try {
			Socket clientSocket = new Socket("localhost", port);
			DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			new Thread(() -> {
				String stringFromServer;
				try {
					inFromServer.lines().forEach((o) -> logger.info("FROM: " + o));
					if (false) {
						stringFromServer = inFromServer.readLine();
						logger.info("FROM: "+stringFromServer);
					}
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}).start();
			
			ThreadFactory threadFactory = new ReserveTicketFactory();
			ReserveTicket callable = new ReserveTicket(outToServer2);
			Task futureTask = new Task(callable);

//			Executors.newSingleThreadExecutor(threadFactory).submit(futureTask);
			Executors.newSingleThreadExecutor().execute(futureTask);
//			Executors.newSingleThreadExecutor(Executors.defaultThreadFactory()).execute(futureTask);
			
//			Executors.newSingleThreadExecutor(
//					new ReserveTicketFactory()).submit(
//							new Task(new ReserveTicket(outToServer)));
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	class ReserveTicketFactory implements ThreadFactory {
//		private Socket clientSocket;
		
//		@Override
//		public Thread newThread(Runnable r) {
//			logger.info("Thread created");
//			try {
//				return new T(r);
//			} catch (Exception e) {
//				logger.severe(e.getMessage());
//			}
//			return new Thread(r);
//		}
		
		@Override
		public Thread newThread(Runnable r) {
			logger.info("Thread created");
			try {
				return new T(r);
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
			return new Thread(r);
		}
		
		class T extends Thread {
			private Runnable runnable;
//			private Callable<Void> callable;
			
			public T(Runnable runnable) {
				this.runnable = runnable;
			}
			
//			public T(Callable<Void> callable) {
//				this.callable = callable;
//			}
			
			@Override
			public void start() {
//				runnable.run();
//				if (callable == null) {
//					logger.info("NULL");
//				}
				
				try {
					runnable.run();
//					callable.call();
				} catch (Exception e) {
					logger.severe(e.toString());
				}
			}
		}
	}
	
	class Task extends FutureTask<Void> {
		public Task(Callable<Void> arg0) {
			super(arg0);
			logger.info("Task created");
		}
	}
	
	class ReserveTicket implements Callable<Void> {
		private DataOutputStream outToServer;
		
		public ReserveTicket(DataOutputStream outToServer) throws UnknownHostException, IOException {
			logger.info("ReserveTicket created "+port);
			this.outToServer = outToServer;
		}
		
		@Override
		public Void call() throws Exception {
			for (int i = 1; i < 4; i++) {
				Thread.sleep(300);
				outToServer.writeBytes("Test"+i+"\n");
				logger.info("SEND: Test"+i);
			}
			return null;
		}
	}
}





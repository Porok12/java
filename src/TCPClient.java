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
			
			outToServer2.writeBytes("Hello\n");
			
			new Thread(() -> {
				String fromServer;
				try {
					fromServer = inFromServer.readLine();
					System.out.println(fromServer);
				} catch (IOException e1) {
					logger.severe(e1.getMessage());
				}
				
				String stringFromServer;
				while(true) {
					try {
						//inFromServer.lines().forEach((o) -> logger.info("Received from server: " + o));
						if (true) {
							stringFromServer = inFromServer.readLine();
							logger.info(String.format("Received from server: %s", stringFromServer));
						}
					} catch (IOException e) {
						logger.severe(e.getMessage()); 
					}	
				}
			}).start();
			
			ThreadFactory threadFactory = new ReserveTicketFactory();
			ReserveTicket callable = new ReserveTicket(outToServer2);
			Task futureTask = new Task(callable);

//			Executors.newSingleThreadExecutor(threadFactory).submit(futureTask);
//			Executors.newSingleThreadExecutor().execute(futureTask);
//			Executors.newSingleThreadExecutor(Executors.defaultThreadFactory()).execute(futureTask);
			Executors.newSingleThreadExecutor(threadFactory).execute(futureTask);
//			Executors.newSingleThreadExecutor(
//					new ReserveTicketFactory()).submit(
//							new Task(new ReserveTicket(outToServer)));
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	class ReserveTicketFactory implements ThreadFactory {
		@Override
		public Thread newThread(Runnable r) {
			logger.info("Thread created");
			try {
				Thread t = new T(r);
				t.setName("name:"+new Random().nextInt());
				t.setPriority(Thread.NORM_PRIORITY);
				t.setDaemon(false);
//				new Thread().setName("name:"+new Random().nextInt());
				return new Thread(r);
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
			return new Thread(r);
		}
		
		class T extends Thread {
			private Runnable runnable;
			
			public T(Runnable runnable) {
				this.runnable = runnable;
			}
			
			@Override
			public void start() {
				try {
					runnable.run();
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
			logger.info(String.format("ReserveTicket created %d", port));
			this.outToServer = outToServer;
		}
		
		@Override
		public Void call() throws Exception {
			for (int i = 1; i < 4; i++) {
				Thread.sleep(2000);
				outToServer.writeBytes(String.format("Test%d\n", i));
				logger.info(String.format("Write to server: Test%d", i));
			}
			return null;
		}
	}
}





import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class TCPClient {
	private static final Logger logger = Logger.getLogger(TCPClient.class.getName());
	
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
	
	public TCPClient() {
		
	}
	
	public void reserveTicket() {
//		Executors.newSingleThreadExecutor(
//				new ReserveTicketFactory()).execute(
//						new Task(new ReserveTicket()));
		try {
			Socket clientSocket = new Socket("localhost", 6722);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			Executors.newSingleThreadExecutor(
					new ReserveTicketFactory()).submit(
							new Task(new ReserveTicket(outToServer)));
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	class ReserveTicketFactory implements ThreadFactory {
//		private Socket clientSocket;
		
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
		
		@Override
		protected void finalize() throws Throwable {
//			clientSocket.close();
			super.finalize();
		}
		
		class T extends Thread {
			private Runnable runnable;
			private Callable<Void> callable;
			
			public T(Runnable runnable) {
				this.runnable = runnable;
			}
			
			public T(Callable<Void> callable) {
				this.callable = callable;
			}
			
			@Override
			public void start() {
//				runnable.run();
				try {
					callable.call();
				} catch (Exception e) {
					logger.severe(e.getMessage());
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
		
		public ReserveTicket(DataOutputStream outToServer) {
			logger.info("ReserveTicket created");
			this.outToServer = outToServer;
		}
		
		@Override
		public Void call() throws Exception {
			logger.info("Task called");
			outToServer.writeBytes("Test");
			return null;
		}
	}
}





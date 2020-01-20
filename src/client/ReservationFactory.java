package client;

import java.util.Random;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public class ReservationFactory implements ThreadFactory {
	private static final Logger logger = Logger.getLogger(ReservationFactory.class.getName());
	
	@Override
	public Thread newThread(Runnable r) {
		logger.info("Thread created");
		try {
			Thread t = new T(r);
			t.setName("name: " + new Random().nextInt());
			t.setPriority(Thread.NORM_PRIORITY);
			t.setDaemon(false);
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
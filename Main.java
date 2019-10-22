package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

public class Main {
	public static void main(String[] args) {
		if (args.length > 0) {
			File file = new File("D:\\workspace_przemyslaw_papla\\lab1\\res\\file.txt");
			try (BufferedReader reader = new BufferedReader(new FileReader(file))){
				int l_thread = Integer.valueOf(args[0]);
				logger.info("Number of threads: " + l_thread);

				BlockingQueue<String> queue = new LinkedBlockingQueue<>(1);
				ExecutorService exec = Executors.newFixedThreadPool(l_thread, (r) -> new Thread(r));

				exec.execute(() -> {
					String line = "";
					try {
						while ((line = reader.readLine()) != null) {
							queue.put(line);
							Thread.sleep(350);
						}
					} catch (IOException | InterruptedException e) {
					} finally {
						try {
							reader.close();
						} catch (IOException e) { }
					}
				});

				for (int j = 0; j < 100; j++) {
					exec.execute(() -> {
						try {
							logger.info(String.format("(%d): %s", Thread.currentThread().getId(), queue.take()));
						} catch (InterruptedException e) {
							logger.info(String.format("(%d): interrupted", Thread.currentThread().getId()));
						}
					});
				}

				exec.awaitTermination(3, TimeUnit.SECONDS);
				exec.shutdownNow();

			} catch (NumberFormatException e) {
				logger.log(Level.SEVERE, "NumberFormatException", e);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "InterruptedException", e);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "FileNotFoundException", e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "IOException", e);
			}
		}
	}

	private static Logger logger = Logger.getLogger(Main.class.getName());
}
package client;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

public class Task extends FutureTask<Void> {
	private static final Logger logger = Logger.getLogger(Task.class.getName());
	
	public Task(Callable<Void> arg0) {
		super(arg0);
		logger.info("Task created");
	}
}
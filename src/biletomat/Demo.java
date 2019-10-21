package biletomat;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import model.Bilet;

public class Demo {
	private static final int L_WATKOW = 1;

	public static void main(String[] args) {
		BlockingQueue<Bilet> bilety = new ArrayBlockingQueue<>(10);
		BlockingQueue<Integer> integer = new ArrayBlockingQueue<>(10);
		ExecutorService service = Executors.newFixedThreadPool(L_WATKOW, new SimpleThreadFactory());
		
		MyQueue myQueue = new MyQueue();
		
		service.execute(new MyTask(myQueue));
		service.execute(new MyTask(myQueue));
		service.execute(new MyTask(myQueue));
		service.execute(new MyTask(myQueue));
		service.execute(new MyTask(myQueue));
		service.execute(new MyTask(myQueue));


		service.execute(new FutureTask<Integer>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("1");
				return 1;
			}
		}));

		for (int i = 0; i < 10; i++) {
			service.execute(() -> {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				try {
					integer.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("---");
			});
		}

		service.shutdown();
		System.out.println("shutdown");
	}
}

class MyQueue {
	private final Lock queueLock = new ReentrantLock();

	public void print() {
		queueLock.lock();

		int i = (new Random().nextInt(10) + 5) * 100;
		System.out.println(i);
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		queueLock.unlock();
	}
}

class MyTask implements Runnable {
	public MyTask(MyQueue q) {
		this.q = q;
	}
	
	private MyQueue q;

	@Override
	public void run() {
		q.print();
	}
}

class SimpleThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}
}
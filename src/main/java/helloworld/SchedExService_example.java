package helloworld;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedExService_example {

	/**
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {



		ScheduledExecutorService scheduledExecutorService =
				Executors.newScheduledThreadPool(5);

		ScheduledFuture<String> scheduledFuture =
				scheduledExecutorService.schedule(new Callable<String>() {
					public String call() throws Exception {
						System.out.println("Executed!");
						return "Called!";
					}
				},
				5,
				TimeUnit.SECONDS);

		System.out.println("result = " + scheduledFuture.get());

		scheduledExecutorService.shutdown();




	}

}


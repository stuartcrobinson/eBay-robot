package helloworld;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedExService_repeating_ex{

	/**
	 * @param args
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		ScheduledExecutorService scheduledExecutorService =	Executors.newScheduledThreadPool(5);

		scheduledExecutorService.scheduleAtFixedRate(
				new Runnable() {
					public void run() {
						System.out.println("Executed!");
					}
				},
				2000,
				500, 
				TimeUnit.MILLISECONDS
				);


		scheduledExecutorService.scheduleAtFixedRate(
				new Runnable() {
					public void run() {
						System.out.println("Executed! 2");
					}
				},
				1000,
				200, 
				TimeUnit.MILLISECONDS
				);

		System.out.println("oh hey");

		for (int i = 0; i < 10000; i++){
			System.out.println(i);
			Thread.sleep(100);
		}
		scheduledExecutorService.shutdown();




	}

}

//http://tutorials.jenkov.com/java-util-concurrent/scheduledexecutorservice.html

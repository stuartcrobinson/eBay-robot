package helloworld;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BadAssTask implements Runnable {

        @Override
        public void run() {
                System.out.println("Sleeping ...");
        }

        public static void main(String[] args) {
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                		new BadAssTask(), 1, 1, TimeUnit.SECONDS
                		);
        }

}

//http://code.nomad-labs.com/2011/12/09/mother-fk-the-scheduledexecutorservice/
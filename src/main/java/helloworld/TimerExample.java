package helloworld;


import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public final class TimerExample extends TimerTask {

	/** Construct and use a TimerTask and Timer. 
	 * @throws InterruptedException */
	public static void main (String... arguments ) throws InterruptedException {


		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

		Calendar tomorrow = new GregorianCalendar();
		System.out.println(sdf.format(tomorrow.getTime()));
		tomorrow.add(Calendar.DATE, fONE_DAY);

		System.out.println(sdf.format(tomorrow.getTime()));


		TimerTask fetchMail = new TimerExample();
		//perform the task once a day at 4 a.m., starting tomorrow morning
		//(other styles are possible as well)
		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(fetchMail, getTomorrowMorning4am(), fONCE_PER_DAY);
		timer.schedule(fetchMail, 5*1000);
//		timer.cancel();
		for (int i = 0; i < 10000; i++){
			System.out.println(i);
			Thread.sleep(500);
		}
	}

	/**
	 * Implements TimerTask's abstract run method.
	 */
	@Override public void run(){
		//toy implementation
		System.out.println("Fetching mail...");

		for (int i = 0; i < 5; i++){
			System.out.println("fetcher " + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.exit(0);
	}

	// PRIVATE

	//expressed in milliseconds
	@SuppressWarnings("unused")
	private final static long fONCE_PER_DAY = 1000*60*60*24;

	private final static int fONE_DAY = 1;
	private final static int fFOUR_AM = 4;
	private final static int fZERO_MINUTES = 0;

	@SuppressWarnings("unused")
	private static Date getTomorrowMorning4am(){
		Calendar tomorrow = new GregorianCalendar();
		tomorrow.add(Calendar.DATE, fONE_DAY);
		Calendar result = new GregorianCalendar(
				tomorrow.get(Calendar.YEAR),
				tomorrow.get(Calendar.MONTH),
				tomorrow.get(Calendar.DATE),
				fFOUR_AM,
				fZERO_MINUTES
				);
		return result.getTime();
	}
}


//http://www.javapractices.com/topic/TopicAction.do?Id=54
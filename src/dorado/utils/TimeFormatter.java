package dorado.utils;

public class TimeFormatter {

	/**
	 * Transforms time given in a long number to a human reading format.
	 * 
	 * x mins, y secs, z milis
	 */
	public static String toMSM(long time){
		long min = time/60000;
		time = time - min*60000;
		long sec = time/1000;
		time = time - sec*1000;
		return min+" mins, "+sec+" secs, "+time+" milis";
	}
	
}

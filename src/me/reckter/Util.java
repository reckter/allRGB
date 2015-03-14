package me.reckter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Class containing useful and general functions
 */
public abstract class Util {


	public static void printMemory() {
		Runtime rt = Runtime.getRuntime();
		long totalMem = rt.totalMemory();
		long maxMem = rt.maxMemory();
		long freeMem = rt.freeMemory();
		double megs = 1048576.0;

		Log.info("Total Memory: " + totalMem + " (" + (totalMem / megs) + " MiB)");
		Log.info("Max Memory:   " + maxMem + " (" + (maxMem / megs) + " MiB)");
		Log.info("Free Memory:  " + freeMem + " (" + (freeMem / megs) + " MiB)");

	}

	/**
	 * Print a message to the console and to a log. Guess the module and the title from a stack trace
	 *
	 * @param text The message
	 * @deprecated
	 */
	public static void c_log(String text) {
		Log.info(text);
	}

	public static double peakRandom(Random rnd) {
		return rnd.nextGaussian() + 1;
	}
}
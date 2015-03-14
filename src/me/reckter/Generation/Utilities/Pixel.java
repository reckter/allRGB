package me.reckter.Generation.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pixel implements Comparable<Pixel> {

	public int r;
	public int g;
	public int b;

	public int x;
	public int y;

	public int rShould;
	public int gShould;
	public int bShould;

	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Pixel(int r, int b, int g, int rShould, int bShould, int gShould) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.rShould = rShould;
		this.gShould = gShould;
		this.bShould = bShould;
	}


	@Override
	public int compareTo(Pixel o) {
		if (isChangedBetter(this, o)) {
			return 1;
		}
		return 0;
	}

	private boolean isChangedBetter(Pixel pixel1, Pixel pixel2) {
		if ((getDifferences(pixel1) - getSwitchedDifferences(pixel2, pixel1)) + (getDifferences(pixel2) - getSwitchedDifferences(pixel1, pixel2)) > 0) {
			return true;
		}
		return false;
	}


	private float getDifferences(Pixel pixel) {
		return getSwitchedDifferences(pixel, pixel);
	}

	private float getSwitchedDifferences(Pixel pixel1, Pixel pixel2) {
		return (((float) pixel1.r - (float) pixel2.rShould) / 256 + ((float) pixel1.g - (float) pixel2.gShould) / 256 + ((float) pixel1.b - (float) pixel1.bShould) / 256) / 3;
	}
}

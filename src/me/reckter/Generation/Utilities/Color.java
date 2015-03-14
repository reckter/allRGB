package me.reckter.Generation.Utilities;

/**
 * Created by hannes on 23/12/14.
 */
public class Color {
	public byte r;
	public byte g;
	public byte b;

	public Color(int r, int g, int b) {
		this.r = (byte) r;
		this.g = (byte) g;
		this.b = (byte) b;
	}

	public Color(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public double distance(Color c) {
		return Math.sqrt(distanceSquared(c));
	}

	public double distanceSquared(Color c) {
		double distanceR = this.r - c.r;
		double distanceG = this.g - c.g;
		double distanceB = this.b - c.b;
		return distanceR * distanceR + distanceG * distanceG + distanceB * distanceB;
	}
}

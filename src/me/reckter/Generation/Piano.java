package me.reckter.Generation;

import me.reckter.Generation.Utilities.Coordinates;
import me.reckter.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Piano extends BasicGeneration {

	int[][][] colorNumbers;
	int colorNumber;

	public Piano() {
		colorNumbers = new int[COLORS][COLORS][COLORS];
		colorNumber = 1;

	}

	@Override
	public void render() {
		Log.info("calculating the piano curve....");
		piano(new Coordinates(0, 0, 0), new Coordinates(COLORS, COLORS, COLORS), 0);

		Log.info("drawing...");
		System.exit(1);
	}


	//TODO WTF!?!?!
	public void piano(Coordinates start, Coordinates end, int iteration) {
		if (start.equal(end) || iteration >= 10) {
			if (colorNumbers[start.getX()][start.getY()][start.getZ()] == 0) {
				colorNumbers[start.getX()][start.getY()][start.getZ()] = colorNumber++;
			} else {
				Log.error("This pixel is taken!");
			}
			return;
		}

		//Direction 1
		int step = (int) ((end.getDistance(start)) / 2);
		piano(start.add(0, 0, 0), start.add(0, 0, step), iteration + 1);
		piano(start.add(0, 0, step), start.add(0, step, step), iteration + 1);
		piano(start.add(0, step, step), start.add(0, step, 0), iteration + 1);
		piano(start.add(0, step, 0), start.add(step, step, 0), iteration + 1);
		piano(start.add(step, step, 0), start.add(step, step, step), iteration + 1);
		piano(start.add(step, step, step), start.add(step, 0, step), iteration + 1);
		piano(start.add(step, 0, step), start.add(step, 0, 0), iteration + 1);

	}
}

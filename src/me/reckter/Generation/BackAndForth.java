package me.reckter.Generation;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/11/13
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackAndForth extends BasicGeneration {

	@Override
	public void render() {

		int x, y;
		for (int r = 0; r < COLORS; r++) {
			for (int g = 0; g < COLORS; g++) {
				for (int b = 0; b < COLORS; b++) {
					x = (r * COLORS * COLORS + g * COLORS + b) % SIZE;
					y = (int) ((float) (r * COLORS * COLORS + g * COLORS + b) / (float) SIZE);
					//Util.c_log("(" + x + "|" + y + ")");
					pixel[x][y][R] = (byte) (b % 2 == 0 ? r : 255 - r);
					pixel[x][y][G] = (byte) (r % 2 == 0 ? g : 255 - g);
					pixel[x][y][B] = (byte) (g % 2 == 0 ? b : 255 - b);
				}
			}
		}

	}
}

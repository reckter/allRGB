package me.reckter.Generation.Picture;

import me.reckter.Log;
import me.reckter.Util;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 12:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PictureRandomSort extends BasicPicture {
	protected static final int SWITCH_PIXEL_BARRIER = 0;

	public static final int SCALE = 1;

	private Queue<SwitchPixel> pixelToSwitch = new ArrayBlockingQueue<SwitchPixel>(SIZE * SIZE);

	public PictureRandomSort(String file) {
		super(file);
	}


	@Override
	public void render() {

		randomizePixel();
		Util.c_log("repainting image");

		randomSwapPixels(SIZE, SIZE * SIZE);


	}

	public void randomSwapPixels(int maxIter, int maxStream) {

		new Thread(() -> {
			while (true) {
				if (!pixelToSwitch.isEmpty()) {
					SwitchPixel point = pixelToSwitch.remove();
					switchPixel(point.points[0], point.points[1], point.points[2], point.points[3]);
				}
			}
		}).start();


		long startTime = System.currentTimeMillis();


		for (int i = 0; i < maxIter; i++) {
			while (isSaving) {
				Thread.yield();
			}


			float percent = (((float) i / (float) maxIter) * 100f);
			Log.info("[" + i + "/" + maxIter + "]" + percent + "%(ETA: " + (((float) (System.currentTimeMillis() - startTime) / percent) * (100f - percent)) / 1000f + "s) : " + calculateFittnes());

			Stream.iterate(0, n -> n + 1).limit(maxStream).parallel().map(ignored -> {
				int x1 = random.nextInt(SIZE);
				int y1 = random.nextInt(SIZE);

				int x2 = random.nextInt(SIZE);
				int y2 = random.nextInt(SIZE);
				int ret[] = {x1, y1, x2, y2};
				return ret;
			}).forEach(point -> {
				if (isChangedBetter(point[0], point[1], point[2], point[3])) {
					pixelToSwitch.add(new SwitchPixel(point));
				}
			});
		}
	}

	protected void switchPixel(int x1, int y1, int x2, int y2) {
		while (isSaving) {
			Thread.yield();
		}
		if (x1 == x2 && y1 == y2) {
			return;
		}
		byte tmpR = pixel[x1][y1][R];
		byte tmpG = pixel[x1][y1][G];
		byte tmpB = pixel[x1][y1][B];

		pixel[x1][y1][R] = pixel[x2][y2][R];
		pixel[x1][y1][G] = pixel[x2][y2][G];
		pixel[x1][y1][B] = pixel[x2][y2][B];

		pixel[x2][y2][R] = tmpR;
		pixel[x2][y2][G] = tmpG;
		pixel[x2][y2][B] = tmpB;

	}

	protected long calculateFittnes() {
		long fittnes = 0;
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				fittnes += getDifferences(x, y);
			}
		}
		return fittnes;
	}

	/**
	 * randomizes the pixel. Takes care of allRGB things
	 */
	protected void randomizePixel() {
		Util.c_log("randomizing Pixel");
		int r = 0;
		int g = 0;
		int b = 0;


		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				pixel[x][y][R] = (byte) r;
				pixel[x][y][G] = (byte) g;
				pixel[x][y][B] = (byte) b;

				r += 4096 / SIZE;
				if (r >= 256) {
					r = 0;
					g += 4096 / SIZE;
					if (g >= 256) {
						g = 0;
						b += 4096 / SIZE;
					}
				}
			}
		}

		for (int x = SIZE - 1; x >= 0; x--) {
			for (int y = SIZE - 1; y >= 0; y--) {
				int x2 = random.nextInt(x + 1);
				int y2 = random.nextInt(SIZE);
				if (x2 == x) {
					y2 = random.nextInt(y + 1);
				}

				byte tmpR = pixel[x][y][R];
				byte tmpG = pixel[x][y][G];
				byte tmpB = pixel[x][y][B];

				pixel[x][y][R] = pixel[x2][y2][R];
				pixel[x][y][G] = pixel[x2][y2][G];
				pixel[x][y][B] = pixel[x2][y2][B];

				pixel[x2][y2][R] = tmpR;
				pixel[x2][y2][G] = tmpG;
				pixel[x2][y2][B] = tmpB;
			}
		}

		Util.c_log("finished.");
	}

	/**
	 * Compares if changing is better and returns true if so
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	protected boolean isChangedBetter(int x1, int y1, int x2, int y2) {
		if ((getDifferences(x1, y1) - getSwitchedDifferences(x2, y2, x1, y1)) + (getDifferences(x2, y2) - getSwitchedDifferences(x1, y1, x2, y2)) > SWITCH_PIXEL_BARRIER) {
			return true;
		}
		return false;
	}

	/**
	 * returns the diffrence between pixel[x][y] and pixelshould[x][y]
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	protected int getDifferences(int x, int y) {
		return getSwitchedDifferences(x, y, x, y);
	}


	protected int allign(int i) {
		while (i % SCALE != 0) {
			i--;
		}
		return i;
	}


	/**
	 * returns the diffrence between pixel[x1][y1] and pixelshould[x2][y2]
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	protected int getSwitchedDifferences(int x1, int y1, int x2, int y2) {
		int difr = pixel[x1][y1][R] - pixelShould[allign(x2) / SCALE][allign(y2) / SCALE][R];
		int difg = pixel[x1][y1][G] - pixelShould[allign(x2) / SCALE][allign(y2) / SCALE][G];
		int difb = pixel[x1][y1][B] - pixelShould[allign(x2) / SCALE][allign(y2) / SCALE][B];

		return Math.abs(difr) * Math.abs(difg) * Math.abs(difb);
	}

	private class SwitchPixel {

		int[] points;

		public SwitchPixel(int[] points) {
			this.points = points;
		}
	}
}

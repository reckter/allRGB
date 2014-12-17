package me.reckter.Generation;

import me.reckter.Generation.Utilities.RGB_util;
import me.reckter.Util;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/12/13
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomSorting extends BasicGeneration {

	@Override
	public void render() {
		RGB_util.randomizePixel(this.pixel);
		int k = 0;

		int tileSize = 512;


		for(int xTile = 0; xTile < 4096; xTile += tileSize) {
			if((float) xTile / (float) (SIZE) * 100 > k + 10) {
				k += 10;
				Util.c_log(k + "%");
			}
			for(int yTile = 0; yTile < 4096; yTile += tileSize) {
				for(int xMiniTile1 = xTile; xMiniTile1 < xTile + tileSize; xMiniTile1++) {
					for(int yMiniTile1 = yTile; yMiniTile1 < yTile + tileSize; yMiniTile1++) {
						for(int xMiniTile2 = xTile; xMiniTile2 < xTile + tileSize - 1; xMiniTile2++) {
							for(int yMiniTile2 = yTile; yMiniTile2 < yTile + tileSize - 1; yMiniTile2++) {

								if(getNumberOfColor(pixel[xMiniTile2][yMiniTile2]) > getNumberOfColor(pixel[xMiniTile2 + 1][yMiniTile2 + 1])) {
									byte[] tmp = pixel[xMiniTile2][yMiniTile2];
									pixel[xMiniTile2][yMiniTile2] = pixel[xMiniTile2 + 1][yMiniTile2 + 1];
									pixel[xMiniTile2 + 1][yMiniTile2 + 1] = tmp;
								}
							}
						}
					}
				}
			}
		}
	}

	public int getNumberOfColor(byte[] pixel) {
		return pixel[R] * COLORS * COLORS + pixel[G] * COLORS + pixel[B];
	}
}

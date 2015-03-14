package me.reckter.Generation.Picture;

import me.reckter.Generation.Utilities.Color;
import me.reckter.Log;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by reckter on 19.01.2015.
 */
public class PictureRandomListSort extends PictureRandomSort {
	public PictureRandomListSort(String file) {
		super(file);
	}

	@Override
	public void render() {
		LinkedList<Color> sort = new LinkedList<>();

		Log.info("filling list");
		for (int r = 0; r < 256; r++) {
			for (int g = 0; g < 256; g++) {
				for (int b = 0; b < 256; b++) {
					sort.add(new Color(r, g, b));
				}
			}
		}

		Collections.shuffle(sort);

		Log.info("spreading colors");
		for (int x = 0; x < SIZE; x++) {
			if (x % 100 == 0) {
				Log.info("[" + x + "/" + SIZE + "]");
			}
			for (int y = 0; y < SIZE; y++) {
				Color choosen = sort.pop();
				for (int i = 0; i < 1000 && i < sort.size(); i++) {
					Color c = sort.pop();

					if (getDifferences(x, y, c.r, c.g, c.b) < getDifferences(x, y, choosen.r, choosen.g, choosen.b)) {
						sort.add(choosen);
						choosen = c;
					} else {
						sort.add(c);
					}
				}
				pixel[x][y][R] = choosen.r;
				pixel[x][y][G] = choosen.g;
				pixel[x][y][B] = choosen.b;

			}
		}

		randomSwapPixels(SIZE / 4, SIZE * SIZE);
		Log.info("done");


	}


	public int getDifferences(int x, int y, int r, int g, int b) {
		int difr = pixelShould[x][y][R] - r;
		int difg = pixelShould[x][y][G] - g;
		int difb = pixelShould[x][y][B] - b;

		return Math.abs(difr) * Math.abs(difg) * Math.abs(difb);
	}
}

package me.reckter.Generation;

import me.reckter.Util;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountThrough extends BasicGeneration {

    @Override
    public void render() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                pixel[x][y][R] = (short) (((x * SIZE) + y) % 256);
                pixel[x][y][G] = (short) ((((x * SIZE) + y) / 256) % 256);
                pixel[x][y][B] = (short) ((((x * SIZE) + y) / 256 / 256));
            }
        }
    }
}

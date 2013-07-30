package me.reckter.Generation;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 13.07.13
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public class Random extends BasicGeneration{

    public Random() {
        super();
    }

    @Override
    public void render() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i <= SIZE * SIZE; i++) {
            colors.add(i);
        }

        int random;
        for(int x = 0;x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                random = (int) (Math.random() * colors.size());
                pixel[x][y][R] = (short) (colors.get(random) % 256);
                pixel[x][y][G] = (short) ((colors.get(random) / 256) % 256);
                pixel[x][y][B] = (short) (colors.get(random) / 256 / 256 - 1);
            }
        }
    }
}

package me.reckter.Generation;

import me.reckter.Generation.BasicGeneration;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 13.07.13
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */
public class Scale extends BasicGeneration {

    @Override
    public void render() {
        for(int xTile = 0; xTile < 4096; xTile += 256) {
            for(int yTile = 0; yTile < 4096; yTile += 256) {
                for(int xMiniTile = 0; xMiniTile < 256; xMiniTile += 4) {
                    for(int yMiniTile = 0; yMiniTile < 256; yMiniTile += 4) {
                        for(int x = 0; x < 4; x++) {
                            for(int y = 0; y < 4; y++) {
                                pixel[xTile + xMiniTile + x][yTile + yMiniTile + y][R] = (byte)( (xMiniTile / 4) + x * 32);
                                pixel[xTile + xMiniTile + x][yTile + yMiniTile + y][G] = (byte)( (yMiniTile / 4) + y * 32);
                                pixel[xTile + xMiniTile + x][yTile + yMiniTile + y][B] = (byte)( (xTile / 256) + (yTile / 256) * 16) ;
                            }
                        }
                    }
                }
            }
        }
    }
}

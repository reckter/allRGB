package me.reckter.Generation;

import me.reckter.Util;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PictureAverage extends BasicPicture {
    private boolean[][][] colors;
    public PictureAverage(String file) {
        super(file);
        colors = new boolean[COLORS][COLORS][COLORS];
    }

    @Override
    public void render() {
        int r,g,b;
        Util.c_log("Randomizing pixel...");
        for(int x = 0; x < SIZE; x += 2) {
            for(int y = 0; y < SIZE; y += 2) {
                r = (short) (Math.random() * COLORS);
                g = (short) (Math.random() * COLORS);
                b = (short) (Math.random() * COLORS);
                while(colors[r][g][b]) {
                    r = (short) (Math.random() * COLORS);
                    g = (short) (Math.random() * COLORS);
                    b = (short) (Math.random() * COLORS);
                }
                colors[r][g][b] = true;

                pixel[x][y][R] = (short) r;
                pixel[x][y][G] = (short) g;
                pixel[x][y][B] = (short) b;
            }
        }

        Util.c_log("selecting other pixel to match ...");
        short[][] colorToChoose = new short[3][3];
        boolean colorsAreAvailable;
        int k = 0;

        int[] color = new int[3];

        for(int x = 0; x < SIZE; x += 2) {
            if((float) x / (float) (SIZE) * 100 > k + 1) {
                k += 1;
                Util.c_log(k + "%");
            }
            for(int y = 0; y < SIZE; y += 2) {
                r = (pixelShould[x / 2][y / 2][R] * 4) - pixel[x][y][R];
                g = (pixelShould[x / 2][y / 2][G] * 4) - pixel[x][y][G];
                b = (pixelShould[x / 2][y / 2][B] * 4) - pixel[x][y][B];
                for(int i = 0; i < colorToChoose.length; i++) {
                    color[R] = (int) (Math.random() * ((r < 256)? r : COLORS));
                    color[G] = (int) (Math.random() * ((r < 256)? g : COLORS));
                    color[B] = (int) (Math.random() * ((r < 256)? b : COLORS));
                    while(colors[color[R]][color[G]][color[B]]) {
                        color[R] = (int) (Math.random() * ((r < 256)? r : COLORS));
                        color[G] = (int) (Math.random() * ((r < 256)? g : COLORS));
                        color[B] = (int) (Math.random() * ((r < 256)? b : COLORS));
                    }
                    colors[color[R]][color[G]][color[B]] = true;
                    r -= color[R];
                    g -= color[G];
                    b -= color[B];
                }
            }
        }
    }

}

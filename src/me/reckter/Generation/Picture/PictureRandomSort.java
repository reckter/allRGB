package me.reckter.Generation.Picture;

import me.reckter.Util;

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

    public PictureRandomSort(String file) {
        super(file);
    }


    @Override
    public void render(){
        randomizePixel();
        Util.c_log("repainting image");

        int k = 0;
        for(int i = 0; i < SIZE; i++) {

            Util.c_log( (((float) i / (float)SIZE) * 100f) + "%: " + calculateFittnes());
           for(int j = 0; j < SIZE; j++) {

               Stream.iterate(0, n -> n + 1).limit(SIZE).parallel().map(ignored -> {
                   int x1 = random.nextInt(SIZE);
                   int y1 = random.nextInt(SIZE);

                   int x2 = random.nextInt(SIZE);
                   int y2 = random.nextInt(SIZE);
                   int ret[] = {x1, y1, x2, y2};
                   return ret;
               }).forEach(point -> {
                   if (isChangedBetter(point[0], point[1], point[2], point[3])) {

                       byte tmpR = pixel[point[0]][point[1]][R];
                       byte tmpG = pixel[point[0]][point[1]][G];
                       byte tmpB = pixel[point[0]][point[1]][B];

                       pixel[point[0]][point[1]][R] = pixel[point[2]][point[3]][R];
                       pixel[point[0]][point[1]][G] = pixel[point[2]][point[3]][G];
                       pixel[point[0]][point[1]][B] = pixel[point[2]][point[3]][B];

                       pixel[point[2]][point[3]][R] = tmpR;
                       pixel[point[2]][point[3]][G] = tmpG;
                       pixel[point[2]][point[3]][B] = tmpB;
                   }
               });
           }
        }
    }

    protected long calculateFittnes() {
        long fittnes = 0;
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
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


        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE ; y++) {
                pixel[x][y][R] = (byte) r;
                pixel[x][y][G] = (byte) g;
                pixel[x][y][B] = (byte) b;

                r++;
                if(r == 256) {
                    r = 0;
                    g++;
                    if(g == 256){
                        g = 0;
                        b++;
                    }
                }
            }
        }
        /*
        for(int x = SIZE - 1; x >= 0; x--) {
            for(int y = SIZE - 1; y >= 0; y--) {
                int x2 = random.nextInt(x + 1);
                int y2 = random.nextInt(SIZE);
                if(x2 == x) {
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
        */
        Util.c_log("finished.");
    }

    /**
     * Compares if changing is better and returns true if so
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    protected boolean isChangedBetter(int x1, int y1, int x2, int y2) {
        if( (getDifferences(x1, y1) - getSwitchedDifferences(x2, y2, x1, y1))  +  (getDifferences(x2, y2) - getSwitchedDifferences(x1, y1, x2, y2)) > SWITCH_PIXEL_BARRIER ){
            return true;
        }
        return false;
    }

    /**
     * returns the diffrence between pixel[x][y] and pixelshould[x][y]
     * @param x
     * @param y
     * @return
     */
    protected int getDifferences(int x, int y) {
        return getSwitchedDifferences(x, y, x, y);
    }

    /**
     *  returns the diffrence between pixel[x1][y1] and pixelshould[x2][y2]
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    protected int getSwitchedDifferences(int x1, int y1, int x2, int y2) {
        int difr = pixel[x1][y1][R] - pixelShould[x2][y2][R];
        int difg = pixel[x1][y1][G] - pixelShould[x2][y2][G];
        int difb = pixel[x1][y1][B] - pixelShould[x2][y2][B];

        return difr * difr + difg * difg + difb * difb;
    }

}

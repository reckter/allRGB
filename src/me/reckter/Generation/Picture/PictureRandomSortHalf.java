package me.reckter.Generation.Picture;

/**
 * Created by hannes on 17/12/14.
 */
public class PictureRandomSortHalf extends PictureRandomSort {

    public static int SCALE = 4;
    public PictureRandomSortHalf(String file) {
        super(file);
    }


    protected int allign(int i) {
        while(i % SCALE != 0) {
            i--;
        }
        return i;
    }

    @Override
    protected int getSwitchedDifferences(int x1, int y1, int x2, int y2) {

        int x2Alligned = allign(x2);
        int y2Alligned = allign(y2);

        int shouldR = pixelShould[x2Alligned / SCALE][y2Alligned / SCALE][R];
        int shouldG = pixelShould[x2Alligned / SCALE][y2Alligned / SCALE][G];
        int shouldB = pixelShould[x2Alligned / SCALE][y2Alligned / SCALE][B];

        int isR = 0;
        int isG = 0;
        int isB = 0;

        for(int x = 0; x < SCALE; x++) {
            for(int y = 0; y < SCALE; y++) {
                isR += pixel[x2Alligned + x][y2Alligned + y][R];
                isG += pixel[x2Alligned + x][y2Alligned + y][G];
                isB += pixel[x2Alligned + x][y2Alligned + y][B];
            }
        }

        isR -= pixel[x2][y2][R];
        isG -= pixel[x2][y2][G];
        isB -= pixel[x2][y2][B];

        isR += pixel[x1][y1][R];
        isG += pixel[x1][y1][G];
        isB += pixel[x1][y1][B];

        isR /= SCALE * SCALE;
        isG /= SCALE * SCALE;
        isB /= SCALE * SCALE;


        int difR = isR - shouldR;
        int difG = isG - shouldG;
        int difB = isB - shouldB;

        return difR * difR + difG * difG + difB * difB;
    }
}

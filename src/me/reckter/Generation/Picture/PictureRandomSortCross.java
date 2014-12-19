package me.reckter.Generation.Picture;

/**
 * Created by reckter on 18.12.2014.
 */
public class PictureRandomSortCross extends PictureRandomSort {

    public PictureRandomSortCross(String file) {
        super(file);
    }


    @Override
    protected int getSwitchedDifferences(int x1, int y1, int x2, int y2) {


        int shouldR = 0;
        int shouldG = 0;
        int shouldB = 0;

        int isR = 0;
        int isG = 0;
        int isB = 0;

        for(int x = -1; x <= 1; x++) {
            if(x2 + x < 0 || x2 + x >= SIZE) {
                continue;
            }
            isR += pixel[x2 + x][y2][R];
            isG += pixel[x2 + x][y2][G];
            isB += pixel[x2 + x][y2][B];


            shouldR += pixelShould[x2 + x][y2][R];
            shouldG += pixelShould[x2 + x][y2][G];
            shouldB += pixelShould[x2 + x][y2][B];
        }

        for(int y = -1; y < 1; y++) {
            if(y2 + y < 0 || y2 + y >= SIZE) {
                continue;
            }
            isR += pixel[x2][y2 + y][R];
            isG += pixel[x2][y2 + y][G];
            isB += pixel[x2][y2 + y][B];


            shouldR += pixelShould[x2][y2 + y][R];
            shouldG += pixelShould[x2][y2 + y][G];
            shouldB += pixelShould[x2][y2 + y][B];
        }

        isR -= pixel[x2][y2][R];
        isG -= pixel[x2][y2][G];
        isB -= pixel[x2][y2][B];

        isR += pixel[x1][y1][R];
        isG += pixel[x1][y1][G];
        isB += pixel[x1][y1][B];

        isR /= 5;
        isG /= 5;
        isB /= 5;


        shouldR /= 5;
        shouldG /= 5;
        shouldB /= 5;


        int difR = isR - shouldR;
        int difG = isG - shouldG;
        int difB = isB - shouldB;

        return difR * difR + difG * difG + difB * difB;
    }
}

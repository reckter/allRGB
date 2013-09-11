package me.reckter.Generation;

import me.reckter.Util;

import java.lang.reflect.Array;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Gray extends BasicGeneration {

    private short[] to_gray(int base, int digits, int value)
    {
        short[] gray = new short[digits];
        short[] baseN = new short[digits]; // Stores the ordinary base-N number, one digit per entry
        short i;             // The loop variable

        // Put the normal baseN number into the baseN array. For base 10, 109
        // would be stored as [9,0,1]
        for (i = 0; i < digits; i++) {
            baseN[i] = (short) (value % base);
            value    = value / base;
        }

        // Convert the normal baseN number into the graycode equivalent. Note that
        // the loop starts at the most significant digit and goes down.
        int shift = 0;
        while (i-- > 0) {
            // The gray digit gets shifted down by the sum of the higher
            // digits.
            gray[i] = (short) ((baseN[i] + shift) % base);
            shift += base - gray[i]; // Subtract from base so shift is positive
        }
        return gray;
    }


    @Override
    public void render() {


        for(int i = 0; i <= 999; i++) {
            short[] res = to_gray(255,3,i);
            Util.c_log(i +": " + java.util.Arrays.toString(res));
        }

        int k = 0;
        for(int x = 0; x < SIZE; x++) {
            if((float) x / (float) (BasicGeneration.SIZE) * 100 > k + 10) {
                k += 10;
                Util.c_log(k + "%");
            }
            for (int y = 0; y < SIZE; y++) {
                pixel[x][y] = to_gray(256, 3, (x * SIZE) + y);
               // Util.c_log("(" + x + "|" + y + "):" + ((x * SIZE) + y));
            }
        }
    }
}

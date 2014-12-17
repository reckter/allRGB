package me.reckter.Generation;

import me.reckter.Generation.Utilities.MinimalPixel;
import me.reckter.Util;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by reckter on 27.07.2014.
 */
public class MandelbrotUsing8 extends BasicGeneration {

    @Override
    public void render() {
        int max_betrag_quadrat = 10000;
        int max_iter = 100;
        ArrayList<MinimalPixel> pix = new ArrayList<>(SIZE * SIZE);


        float min_x = -1.5f, min_y = -1f, max_x = .5f, max_y = 1f;
        Util.c_log("aquiring Memory...");
        int k = 0;
        for(short x = 0; x < SIZE; x++) {
            if((float) x / (float) (SIZE) * 100 > k + 10) {
                k += 10;
                Util.c_log(k + "%");
            }
            for(short y = 0; y < SIZE; y++) {
                MinimalPixel actPixel = new MinimalPixel(x, y);

                double cx = min_x + (max_x - min_x) / SIZE * actPixel.x;
                double cy = min_y + (max_y - min_y) / SIZE * actPixel.y;
                float iter = mandeblrot(cx, cy, max_betrag_quadrat, max_iter);
                float saturation = 1;
                if (iter == max_iter) {
                    saturation = 0;
                }
                Color color = Color.getHSBColor((float) (iter / max_iter), saturation, saturation);
                pixel[actPixel.x][actPixel.y][R] = (byte) color.getRed();
                pixel[actPixel.x][actPixel.y][G] = (byte) color.getGreen();
                pixel[actPixel.x][actPixel.y][B] = (byte) color.getBlue();
            }
        }
    }


    public static float mandeblrot(double cx, double cy, int max_betrag_quadrat, int max_iter) {
        double betrag_quadrat = 0;
        double x = 0,y = 0,xt = 0, yt = 0;
        float iter = 0;
        while ( betrag_quadrat <= max_betrag_quadrat && iter < max_iter) {
            xt = x * x - y * y + cx;
            yt = 2 * x * y + cy;
            x = xt;
            y = yt;

            iter++;
            betrag_quadrat = x * x + y * y;
        }
        //iter = iter - (float) (Math.log(Math.log(betrag_quadrat) / Math.log(4)) / Math.log(2));
        // Util.c_log("mandelbrot","iter"," (" + cx + "|" + cy + "): " + iter);
        return iter;
    }
}

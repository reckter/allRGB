package me.reckter.Generation;

import me.reckter.Generation.Utilities.RGB_util;
import me.reckter.Util;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 9/4/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mandelbrot extends BasicGeneration{



    @Override
    public void render(){
        int max_betrag_quadrat = 10000;
        int max_iter = 100;

        String programSource =
                "__kernel void sampleKernel(__global const float *a, __global const float *b, __global float *c){"+
                        ""+
                        "   int gid = get_global_id(0);"+
                        "   float betrag_quadrat = 0;" +
                        "   int iter = 0;"+
                        "   float x = 0,y = 0, xt = 0, yt = 0;"+
                        "   while(betrag_quadrat <= " + max_betrag_quadrat + " && iter < " + max_iter + " ){"+
                        "       xt = x * x - y * y + a[gid];"+
                        "       yt = 2 * x * y + b[gid];" +
                        "       x = xt;"+
                        "       y = yt;"+
                        "       iter = iter + 1;"+
                        "       betrag_quadrat = x * x + y * y;"+
                        "   }"+
                        "   c[gid] = iter;"+
                        "}";

        Util.c_log(programSource);
        float srcArrayA[] = new float[SIZE * SIZE];
        float srcArrayB[] = new float[SIZE * SIZE];
        float dstArray[];
        int x,y;

        Util.c_log("preparing data");
        float min_x = -1.5f, min_y = -1f, max_x = .5f, max_y = 1f;

        for (int i = 0; i < SIZE * SIZE; i++)
        {
            x = i % SIZE;
            y = (int) (i / SIZE);
            srcArrayA[i] = (x * ((max_x - min_x) / (float) SIZE)) + min_x;
            srcArrayB[i] = (y * ((max_y - min_y) / (float) SIZE)) + min_y;
            //Util.c_log("(" + srcArrayA[i] + "|" + srcArrayB[i] + ")");
        }

        Util.c_log("done.");
        dstArray = RGB_util.CalculateByGPU(programSource, srcArrayA, srcArrayB);

        Util.c_log("finalizing data");
        float saturation = 0;
        for(int i = 0; i < dstArray.length; i++) {
            x = i % SIZE;
            y = (int) (i / SIZE);

           // Util.c_log("" + dstArray[i]);
            saturation = 1;
            if(dstArray[i] == max_iter) {
                saturation = 0;
            }
            double iter = dstArray[i];

            Color color = Color.getHSBColor((float) (iter / max_iter), saturation, saturation);

            pixel[x][y][R] = (byte) color.getRed();
            pixel[x][y][G] = (byte) color.getGreen();
            pixel[x][y][B] = (byte) color.getBlue();
        }
    }
}

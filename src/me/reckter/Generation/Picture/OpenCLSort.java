package me.reckter.Generation.Picture;

import me.reckter.Generation.Utilities.RGB_util;

import java.util.Random;

/**
 * Created by reckter on 18.12.2014.
 */
public class OpenCLSort extends BasicPicture {

    public static final int SCALE = 1;
    public OpenCLSort(String file) {
        super(file);
    }

    @Override
    public void render() {

        float[] a = new float[SIZE * SIZE * 3];
        float[] b = new float[SIZE * SIZE * 3];

        RGB_util.randomizePixel(pixel);
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                a[x * SIZE * 3 + y * 3 + R] = pixel[x][y][R];
                a[x * SIZE * 3 + y * 3 + G] = pixel[x][y][G];
                a[x * SIZE * 3 + y * 3 + B] = pixel[x][y][B];


                b[x * SIZE * 3 + y * 3 + R] = pixelShould[x][y][R];
                b[x * SIZE * 3 + y * 3 + G] = pixelShould[x][y][G];
                b[x * SIZE * 3 + y * 3 + B] = pixelShould[x][y][B];
            }
        }

        String programSource =
                        "int getSwitchedDifferences(int x1, int y1, int x2, int y2, __global char *a, __global const char *b) {\n" +
                        "    int difr = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 0] - b[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 0];\n" +
                        "    int difg = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 1] - b[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 1];\n" +
                        "    int difb = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 2] - b[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 2];\n" +
                        "    return difr * difr + difg * difg + difb * difb;\n" +
                        "}\n" +


                        "int getDifferences(int x, int y, __global char *a, __global const char *b) {\n" +
                        "    return getSwitchedDifferences(x, y, x, y, a , b);\n" +
                        "}\n" +

                        "int rand(ulong* seed) {\n" +
                        "    int const a = 16807; //ie 7**5\n" +
                        "    int const m = 2147483647; //ie 2**31-1\n" +
                        "\n" +
                        "    *seed = ((long) (*seed * a))%m;\n" +
                        "    return(*seed);\n" +
                        "}\n" +


                        "int isChangedBetter(int x1, int y1, int x2, int y2, __global char *a, __global const char *b) {\n" +
                        "   if((getDifferences(x1, y1, a, b) - getSwitchedDifferences(x2, y2, x1, y1, a, b))  +  (getDifferences(x2, y2, a, b) - getSwitchedDifferences(x1, y1, x2, y2, a, b)) > 0 ){\n" +
                        "       return 1;\n" +
                        "   }\n" +
                        "   return 0;\n" +
                        "}\n" +


                        "__kernel void sampleKernel(__global char *a, __global const char *b, __global char *c){\n"+
                        ""+
                        "   int gid = get_global_id(0);\n"+
                        "   ulong seed = " + new Random().nextInt() +  "  + gid;\n" +
                        "   for(uint i = 0; i < " + 10 + "; i++) {\n" +
                        "       uint x1 = rand(&seed) / " + SIZE + ";\n" +
                        "       uint y1 = rand(&seed) / " + SIZE + ";\n" +
                        "       uint x2 = rand(&seed) / " + SIZE + ";\n" +
                        "       uint y2 = rand(&seed) / " + SIZE + ";\n" +
                        "       if(isChangedBetter(x1,y1,x2,y2, a, b)) {\n" +
                        "           float r = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 0];" +
                        "           float g = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 1];" +
                        "           float b = a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 2];" +

                        "           a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 0] = a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 0];" +
                        "           a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 1] = a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 1];" +
                        "           a[x1 * " + SIZE + " * 3 + y1 * " + SIZE + " + 2] = a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 2];" +
                        "" +
                        "           a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 0] = r;" +
                        "           a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 1] = g;" +
                        "           a[x2 * " + SIZE + " * 3 + y2 * " + SIZE + " + 2] = b;" +
                        "       }\n" +
                        "   }\n" +
                        "}";



        RGB_util.CalculateByGPU(programSource, a, b);

        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {

                pixel[x][y][R] = (byte) a[x * SIZE * 3 + y * 3 + R];
                pixel[x][y][G] = (byte) a[x * SIZE * 3 + y * 3 + G];
                pixel[x][y][B] = (byte) a[x * SIZE * 3 + y * 3 + B];
            }
        }
    }
}

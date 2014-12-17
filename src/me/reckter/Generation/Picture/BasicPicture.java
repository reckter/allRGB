package me.reckter.Generation.Picture;

import me.reckter.Generation.BasicGeneration;
import me.reckter.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicPicture extends BasicGeneration {

    protected byte[][][] pixelShould;
    protected int height;
    protected int width;


    public BasicPicture(String file) {
        super();
        readImage(file);
    }

    private void readImage(String file) {
        Util.c_log("reading " + file);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            Util.c_log("ERROR: " + e.toString());
        }

        height = img.getHeight();
        width = img.getWidth();


        pixelShould = new byte[height][width][3];
        int[] result = new int[height * width * 3 + 1];

       // img.getData().getPixels(0, 0, height, width, result);
        DataBuffer buffer = img.getData().getDataBuffer();
        short k = 0;

        for(int i = 0; i < height * width; i++) {
            pixelShould[i % height][(int) ((float) i / (float) height)][R] = (byte) buffer.getElem(3 * i + R);
            pixelShould[i % height][(int) ((float) i / (float) height)][G] = (byte) buffer.getElem(3 * i + G);
            pixelShould[i % height][(int) ((float) i / (float) height)][B] = (byte) buffer.getElem(3 * i + B);
            //Log.debug("reading pixel: (" + i % height + "|" + (int) ((float) i / (float) height) + "): [" + buffer.getElem(3 * i + R) + "|" + buffer.getElem(3 * i + G) + "|" + buffer.getElem(3 * i + B) + "]");

            if((float) i / (float) (height * width) * 100 > k + 10) {
                k += 10;
                Util.c_log(k + "%");
            }
        }
        img = null;
        Util.c_log("finished.");
    }

    @Override
    public void render(){
        for(int x = 0;x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                pixel[x][y][R] = pixelShould[x][y][R];
                pixel[x][y][G] = pixelShould[x][y][G];
                pixel[x][y][B] = pixelShould[x][y][B];
            }
        }
    }

}

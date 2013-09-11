package me.reckter.Generation;

import me.reckter.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    protected short[][][] pixelShould;


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

        pixelShould = new short[img.getHeight()][img.getWidth()][3];
        int[] result = new int[img.getHeight() * img.getWidth() * 3 + 1];

        img.getData().getPixels(0, 0, img.getHeight(), img.getWidth(), result);
        short k = 0;
        for(int i = 1; i < img.getHeight() * SIZE; i++) {
            pixelShould[i % img.getHeight()][(int) ((float) i / (float) img.getHeight())][R] = (short) result[3 * i + R];
            pixelShould[i % img.getHeight()][(int) ((float) i / (float) img.getHeight())][G] = (short) result[3 * i + G];
            pixelShould[i % img.getHeight()][(int) ((float) i / (float) img.getHeight())][B] = (short) result[3 * i + B];
            if((float) i / (float) (img.getHeight() * img.getWidth()) * 100 > k + 10) {
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

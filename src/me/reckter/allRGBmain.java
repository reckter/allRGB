package me.reckter;

import me.reckter.Generation.Random;
import me.reckter.Generation.Scale;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 09.07.13
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class allRGBmain {
    public static void main(String[] args) {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Random generation = new Random();
        generation.render();
        generation.writePicture();
        System.exit(0);

        //ImageConvertion imageConvertion = new ImageConvertion();
    }
}

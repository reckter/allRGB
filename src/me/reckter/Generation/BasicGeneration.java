package me.reckter.Generation;

import me.reckter.Log;
import me.reckter.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 13.07.13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasicGeneration {

    public static final int SIZE = 4096;

    public static final int CANVAS_SIZE = 1024;
    public static final int FACTOR = SIZE / CANVAS_SIZE;


    public static final int COLORS = 256;

    public static final int R = 0;
    public static final int G = 1;
    public static final int B = 2;

    public static final int X = 0;
    public static final int Y = 1;
    public Thread showThread;

    private BufferedImage bi;
    private Graphics graphics;

    public static Random random = new Random();

    protected byte[][][] pixel;


    public BasicGeneration() {
        Log.info("allocationg RAM...");
        pixel = new byte[SIZE][SIZE][3];
        Log.info("done");

        showThread = new Thread(new ShowThread());
        showThread.start();

	    Util.c_log("init from BasicGeneration done.");
    }


    abstract public void render();


    public void writePicture() {
	    bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
	    graphics = bi.getGraphics();

        int r,g,b;
        Util.c_log("painting Image");
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {

                r = pixel[y][x][R];
                g = pixel[y][x][G];
                b = pixel[y][x][B];

                r = r >= 0 ? r : 127 - r;
                g = g >= 0 ? g : 127 - g;
                b = b >= 0 ? b : 127 - b;

                graphics.setColor(new Color(r, g, b));
                graphics.drawLine(x, y, x, y);
            }
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("pictures.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Scanner in = new Scanner(br);

        int numPictures = 0;
        numPictures = Integer.parseInt(in.next()) + 1;

        try{
            PrintWriter pWriter = new PrintWriter(new FileWriter("pictures.txt"));
            pWriter.flush();

            pWriter.println(numPictures);
            pWriter.flush();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        Util.c_log("saving Image '" + numPictures + ".png'");
        try {
            ImageIO.write(bi, "PNG", new File("pictures/" + numPictures + ".png"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Util.c_log("finished.");
    }

    public class ShowThread extends JPanel implements Runnable{


        public void run() {
            Util.c_log("started ShowThread");
            JFrame frame = new JFrame("allRGB");
            // frame.setContentPane(new SwingTemplateJPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();             // "this" JFrame packs its components
            // frame.setLocationRelativeTo(null); // center the application window
            frame.setSize(CANVAS_SIZE, CANVAS_SIZE);
            frame.setVisible(true);
            paintComponent(frame.getGraphics());
        }


        public void paintComponent(Graphics graphics) {
            setBackground(Color.BLACK);
            super.paintComponent(graphics);  // paint background

            int r,g,b;
            int rTMP,gTMP,bTMP;

            while(true) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                for(int x = 0; x < CANVAS_SIZE; x ++) {
                    for(int y = 0; y < CANVAS_SIZE; y++) {
                        r = 0;
                        g = 0;
                        b = 0;
                        for(int xi = 0; xi < FACTOR; xi++) {
                            for(int yi = 0; yi < FACTOR; yi++) {
                                rTMP = pixel[x * FACTOR + xi][y * FACTOR + yi][R];
                                gTMP = pixel[x * FACTOR + xi][y * FACTOR + yi][G];
                                bTMP = pixel[x * FACTOR + xi][y * FACTOR + yi][B];


                                rTMP = rTMP >= 0 ? rTMP : 127 - rTMP;
                                gTMP = gTMP >= 0 ? gTMP : 127 - gTMP;
                                bTMP = bTMP >= 0 ? bTMP : 127 - bTMP;

                                r += rTMP;
                                g += gTMP;
                                b += bTMP;
                            }
                        }
                        r /= FACTOR * FACTOR;
                        g /= FACTOR * FACTOR;
                        b /= FACTOR * FACTOR;

                        if(r < 256 && r >= 0 && g < 256 && g >= 0 && b < 256 && b >= 0){
                            graphics.setColor(new Color(r , g, b ));
                            graphics.drawLine(x,y,x,y);
                        } else {
                            Util.c_log("The pixel (" + x + "|" + y + ") is wrong: " + r + "," + g + "," + b);
                        }

                    }
                }
            }
        }
    }
}

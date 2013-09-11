package me.reckter.Generation;

import me.reckter.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 13.07.13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class BasicGeneration {

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

    protected LinkedList<Integer[]> pixelToDraw;

    protected short[][][] pixel;


    public BasicGeneration() {
        pixel = new short[SIZE][SIZE][3];

        pixelToDraw = new LinkedList<Integer[]>();

        showThread = new Thread(new showThread());
        showThread.start();

	    Util.c_log("init from BasicGeneration done.");
    }


    public void render() {
        //render Stuff here;
    }


    public void writePicture() {
	    bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
	    graphics = bi.getGraphics();

        Util.c_log("painting Image");
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                graphics.setColor(new Color(pixel[x][y][R], pixel[x][y][G], pixel[x][y][B]));
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

    public class showThread extends JPanel implements Runnable{


        public void run() {
            Util.c_log("started showThread");
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
                                r += pixel[x * FACTOR + xi][y * FACTOR + yi][R];
                                g += pixel[x * FACTOR + xi][y * FACTOR + yi][G];
                                b += pixel[x * FACTOR + xi][y * FACTOR + yi][B];
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

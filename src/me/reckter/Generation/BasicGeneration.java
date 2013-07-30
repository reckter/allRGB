package me.reckter.Generation;

import me.reckter.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 13.07.13
 * Time: 16:10
 * To change this template use File | Settings | File Templates.
 */
public class BasicGeneration {
    
    public static final int CANVAS_WIDTH = 256;
    public static final int CANVAS_HEIGHT = 256;
    public static final int FACTOR = 16;

    protected static final short SIZE = 4096;

    protected static final int R = 0;
    protected static final int G = 1;
    protected static final int B = 2;

    protected static final int X = 0;
    protected static final int Y = 1;

    private BufferedImage bi;
    private Graphics g;

    short[][][] pixel;


    public BasicGeneration() {
        bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        g = bi.getGraphics();
        pixel = new short[SIZE][SIZE][3];

        Thread showThread = new Thread(new showThread());
        showThread.start();
    }

    public void render() {
        //render Stuff here;
    }

    public void writePicture() {
        Util.c_log("painting Image");
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                g.setColor(new Color(pixel[x][y][R], pixel[x][y][G], pixel[x][y][B]));
                g.drawLine(x, y, x, y);
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
            ImageIO.write(bi, "PNG", new File(numPictures + ".png"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Util.c_log("finished.");
    }

    protected class showThread extends JPanel implements Runnable{
        @Override

        public void run() {
            Util.c_log("started showThread");
            JFrame frame = new JFrame("allRGB");
            // frame.setContentPane(new SwingTemplateJPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();             // "this" JFrame packs its components
            // frame.setLocationRelativeTo(null); // center the application window
            frame.setSize(CANVAS_HEIGHT,CANVAS_WIDTH);
            frame.setVisible(true);
            paintComponent(frame.getGraphics());
        }


        public void paintComponent(Graphics g) {
            super.paintComponent(g);  // paint background
            setBackground(Color.WHITE);


            while(true) {
            for(int x = 0; x < CANVAS_WIDTH; x ++) {
                for(int y = 0; y < CANVAS_HEIGHT; y++) {
                    g.setColor(new Color(pixel[x * FACTOR][y * FACTOR][0],pixel[x * FACTOR][y * FACTOR][1],pixel[x * FACTOR][y * FACTOR][2]));
                    g.drawLine(x,y,x,y);
                }
            }
            }
            // Your custom painting codes
            // ......
        }
    }
}

package me.reckter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 09.07.13
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public class ImageConvertion extends JPanel{

    public static final int CANVAS_WIDTH = 255;
    public static final int CANVAS_HEIGHT = 255;
    public static final int FACTOR = 255;
    public static final String TITLE = "...Title...";


    protected static int START_SIZE_TILE = 16;
    protected static int SIZE = 4096;
    protected static float MIN_PROCENT = 0.3f;

    protected static int R = 0;
    protected static int G = 1;
    protected static int B = 2;

    protected static int X = 0;
    protected static int Y = 1;

    private int[][][] pixel;
    private int[][][] pixelShould;
    Graphics g;

    public ImageConvertion () {

        pixel = new int[SIZE][SIZE][3];
        pixelShould = new int[SIZE][SIZE][3];

        BufferedImage bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        g = bi.getGraphics();

        Thread showThread = new Thread(new showThread());


        showThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            ImageIO.write(bi, "PNG", new File("out.png"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    private void readImage() {
        Util.c_log("reading in.png ...");

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("in.png"));
        } catch (IOException e) {
        }

        int[] result = new int[4];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                img.getData().getPixel(x, y, result);
                pixelShould[x][y][R] = result[R + 1];
                pixelShould[x][y][G] = result[G + 1];
                pixelShould[x][y][B] = result[B + 1];
            }
        }

        Util.c_log("finished.");
    }


    private int[][][] getAllColors() {
        int[][][] ret = new int[256][256][256];
        for(int i = 0; i < 256; i++) {
            for(int j = 0; j < 256; j++) {
                for(int k = 0; k < 256; k++) {
                    ret[i][j][k] = 1;
                }
            }
        }
        return ret;
    }

    private void randomizePixel() {
        Util.c_log("started");
        int r,g,b;
        boolean foundColor = false;
        int[][][] allColor = getAllColors();
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                foundColor = false;
                while(!foundColor) {
                    r = (int) (Math.random() * 255);
                    g = (int) (Math.random() * 255);
                    b = (int) (Math.random() * 255);
                    if(allColor[r][g][b] == 0) {
                        allColor[r][g][b] = 1;
                        foundColor = true;
                        pixel[x][y][0] = r;
                        pixel[x][y][1] = g;
                        pixel[x][y][2] = b;
                    }
                }
            }
        }
    }

    private float getDifferences(int x, int y) {
        return getSwitchedDifferences(x, y, x, y);
    }


    private float getSwitchedDifferences(int x1, int y1, int x2, int y2) {
        return ( (pixel[x1][y1][0] - pixelShould[x2][y2][0]) / 256 + (pixel[x1][y1][1] - pixelShould[x2][y2][1]) / 256 + (pixel[x1][y1][2] - pixelShould[x2][y2][2]) / 256);
    }


    public int[][] getPixelToProcess(int x0, int y0, int size) {
        int[][] ret = new int[size * size][2];
        int numPixelToProxess = 0;
        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                if(getDifferences(x0 + x ,y0 + y) > 0.3f) {
                    ret[numPixelToProxess][0] = x0 + x;
                    ret[numPixelToProxess++][1] = y0 + y;
                }
            }
        }

        if(numPixelToProxess >= ret.length) {
            ret[++numPixelToProxess][0] = -1;
            ret[numPixelToProxess][1] = -1;
        }

        return ret;
    }

    public void processImage(){
        randomizePixel();
        int currentTileSize = START_SIZE_TILE;

        Util.c_log("started Tile rendering.");
        while (currentTileSize < SIZE) {
            Util.c_log("Tilesize: " + currentTileSize);
            for(int x = 0; x < SIZE /currentTileSize; x++) {
                for(int y = 0; y < SIZE /currentTileSize; y++) {
                    processImageTile(x * currentTileSize, y * currentTileSize, currentTileSize);
                }
            }

            currentTileSize *= 2;
        }
    }

    public void processImageTile(int x, int y, int size) {
        if(x + size > SIZE) {
            size = SIZE - x;
        }
        if(y + size > SIZE) {
            size = SIZE - y;
        }

        int[][] processPixel = getPixelToProcess(x, y, size);

        for (int i = 0; i < processPixel.length; i++) {
            if(processPixel[i][0] == -1) {
                break;
            }
            for (int j= 0; j < processPixel.length; j++) {
                if(processPixel[j][0] == -1) {
                    break;
                }
                if(  getSwitchedDifferences(processPixel[i][0], processPixel[i][1],processPixel[j][0], processPixel[j][1]) < getDifferences(processPixel[i][0],processPixel[i][1])
                  && getSwitchedDifferences(processPixel[j][0], processPixel[j][1],processPixel[i][0], processPixel[i][1]) < getDifferences(processPixel[j][0],processPixel[j][1])
                   ) { //TODO more switch cases (?)
                    int r = pixel[processPixel[i][X]][processPixel[i][Y]][R];
                    int g = pixel[processPixel[i][X]][processPixel[i][Y]][G];
                    int b = pixel[processPixel[i][X]][processPixel[i][Y]][B];

                    pixel[processPixel[i][X]][processPixel[i][Y]][R] = pixel[processPixel[j][X]][processPixel[j][Y]][R];
                    pixel[processPixel[i][X]][processPixel[i][Y]][G] = pixel[processPixel[j][X]][processPixel[j][Y]][G];
                    pixel[processPixel[i][X]][processPixel[i][Y]][B] = pixel[processPixel[j][X]][processPixel[j][Y]][B];

                    pixel[processPixel[j][X]][processPixel[j][Y]][R] = r;
                    pixel[processPixel[j][X]][processPixel[j][Y]][G] = g;
                    pixel[processPixel[j][X]][processPixel[j][Y]][B] = b;
                }
            }
        }

    }

    protected class showThread extends JPanel implements Runnable{
        @Override

        public void run() {
            JFrame frame = new JFrame(TITLE);
           // frame.setContentPane(new SwingTemplateJPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();             // "this" JFrame packs its components
            frame.setLocationRelativeTo(null); // center the application window
            frame.setSize(CANVAS_HEIGHT,CANVAS_WIDTH);
            frame.setVisible(true);
        }


        public void paintComponent(Graphics g) {
            super.paintComponent(g);  // paint background
            setBackground(Color.WHITE);



            for(int x = 0; x < CANVAS_WIDTH; x ++) {
                for(int y = 0; y < CANVAS_HEIGHT; y++) {
                    g.setColor(new Color(pixel[x * FACTOR][y * FACTOR][0],pixel[x * FACTOR][y * FACTOR][1],pixel[x * FACTOR][y * FACTOR][2]));
                    g.drawLine(x,y,x,y);
                }
            }
            // Your custom painting codes
            // ......
        }
    }
}
